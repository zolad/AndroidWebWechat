

package com.heimz.androidwebwechatlib.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.heimz.androidwebwechatlib.util.MLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersistentCookieStore implements CookieStore {
	
	
	private static final String LOG_TAG = "PersistentCookieStore";
	private final HashMap<String, ConcurrentHashMap<String, HttpCookie>> cookies;
    private final SharedPreferences sp;
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    
    
    public PersistentCookieStore(Context context) {
        sp = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new HashMap<String, ConcurrentHashMap<String, HttpCookie>>();

        // Load any previously stored cookies into the store
        Map<String, ?> prefsMap = sp.getAll();
        for(Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            if (((String)entry.getValue()) != null && !((String)entry.getValue()).startsWith(COOKIE_NAME_PREFIX)) {
                String[] cookieNames = TextUtils.split((String)entry.getValue(), ",");
                for (String name : cookieNames) {
                    String encodedCookie = sp.getString(COOKIE_NAME_PREFIX + name, null);
                    if (encodedCookie != null) {
                        HttpCookie decodedCookie = decodeCookie(encodedCookie);
                        if (decodedCookie != null) {
                            if(!cookies.containsKey(entry.getKey()))
                                cookies.put(entry.getKey(), new ConcurrentHashMap<String, HttpCookie>());
                            cookies.get(entry.getKey()).put(name, decodedCookie);
                        }
                    }
                }

            }
        }
    }
    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    protected HttpCookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        HttpCookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
        } catch (IOException e) {
            MLog.d(LOG_TAG, "IOException in decodeCookie");
        } catch (ClassNotFoundException e) {
            MLog.d(LOG_TAG, "ClassNotFoundException in decodeCookie");
        }

        return cookie;
    }
    
//    protected HttpCookie decodeCookie(String cookieString) {
//        byte[] bytes = hexStringToByteArray(cookieString);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        HttpCookie cookie = null;
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).getCookie();
//        } catch (IOException e) {
//            MLog.d(LOG_TAG, "IOException in decodeCookie", e);
//        } catch (ClassNotFoundException e) {
//            MLog.d(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
//        }
//
//        return cookie;
//    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    protected String encodeCookie(SerializableHttpCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            MLog.d(LOG_TAG, "IOException in encodeCookie");
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * Using some super basic byte array <-> hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }


    protected String getCookieToken(URI uri, HttpCookie cookie) {
        return cookie.getName() + cookie.getDomain();
    }
    
    @Override
    public void add(URI uri, HttpCookie cookie) {

        // Save cookie into local store, or remove if expired
        if (!cookie.hasExpired()) {
            if(!cookies.containsKey(cookie.getDomain()))
                cookies.put(cookie.getDomain(), new ConcurrentHashMap<String, HttpCookie>());
            cookies.get(cookie.getDomain()).put(cookie.getName(), cookie);
        } else {
            if(cookies.containsKey(cookie.getDomain()))
                cookies.get(cookie.getDomain()).remove(cookie.getDomain());
        }

        // Save cookie into persistent store
        Editor prefsWriter = sp.edit();
        prefsWriter.putString(cookie.getDomain(), TextUtils.join(",", cookies.get(cookie.getDomain()).keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + cookie.getName(), encodeCookie(new SerializableHttpCookie(cookie)));
        prefsWriter.commit();
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        ArrayList<HttpCookie> ret = new ArrayList<HttpCookie>();
        for (String key:cookies.keySet()){
            if (uri.getHost().contains(key)){
                ret.addAll(cookies.get(key).values());
            }
        }
        return ret;
    }


    public void  clearCookies(){
    	sp.edit().clear().commit(); 
    	cookies.clear();
    }
    
    
    @Override
    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList<URI>();
        for (String key : cookies.keySet())
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return ret;
    }


    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        String name = getCookieToken(uri, cookie);

        if(cookies.containsKey(uri.getHost()) && cookies.get(uri.getHost()).containsKey(name)) {
            cookies.get(uri.getHost()).remove(name);

            Editor prefsWriter = sp.edit();
            if(sp.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);
            }
            prefsWriter.putString(uri.getHost(), TextUtils.join(",", cookies.get(uri.getHost()).keySet()));
            prefsWriter.commit();

            return true;
        } else {
            return false;
        }
    }

    public boolean removeAll() {
        Editor v0 = this.sp.edit();
        v0.clear();
        v0.commit();
        this.cookies.clear();
        return true;
    }
    
    //һ������
    //����cookie��
    
    public String getWebwxDataTicketFromCookie(){
    	
    	 for (String key : cookies.keySet()){
    		
    		 
              // if(cookies.get(key).values().contains("webwx_data_ticket")){
               
            	   Iterator<HttpCookie> iter =cookies.get(key).values().iterator();
            	   
            	   while(iter.hasNext()){
            		   HttpCookie str =  iter.next();
            		   
            		   
            		   //MLog.d("uploadapp", " "+str.toString());
            		   
            		   if(str.getName().contains("webwx_data_ticket")){
            			   
            			  // MLog.d("uploadapp", " "+str.getValue().replace("webwx_data_ticket=", ""));
            			   return str.getValue().replace("webwx_data_ticket=", "");
            		   }
            		   
            		   //System.out.println(str);
            	   }
    		// }
    	 }
    	 
    	 return "";
    }
    
    @Override
    public List<HttpCookie> getCookies() {
        ArrayList<HttpCookie> ret = new ArrayList<HttpCookie>();
        for (String key : cookies.keySet()){
        	
            ret.addAll(cookies.get(key).values());
        }
        return ret;
    }
}

