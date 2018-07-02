

package com.heimz.androidwebwechatlib.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.heimz.androidwebwechatlib.util.MLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequest extends JsonRequest<JSONObject> {

	
    public JsonObjectRequest(String url, Listener<JSONObject> listener, ErrorListener errorlistener, String data) {
        super(url, data, listener,  errorlistener);
    }


    
    public JsonObjectRequest(int method, String url, JSONObject data, Listener<JSONObject> listener, ErrorListener errorlistener) {
        super(method, url, data == null ? null : data.toString(), listener,  errorlistener);
    }

    public JsonObjectRequest(int method, String url, String data, Listener<JSONObject> listener, ErrorListener errorlistener) {
        super(method, url, data == null ? null : data, listener,  errorlistener);
    }
    
//    @Override
//    public Map getHeaders() {
//        HashMap v0 = new HashMap();
//        v0.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//        v0.put("Referer", "https://wx2.qq.com/?&lang=zh_CN");
//        return v0;
//    }
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	// TODO Auto-generated method stub
    	HashMap<String, String> v0 = new HashMap<String, String>();
    	 v0.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
       v0.put("Referer", "https://wx2.qq.com/?&lang=zh_CN");
       return v0;
    }
    
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse arg4) {
        Response<JSONObject> v0_2;
        
        try {
			MLog.e("wechatinit",new String(arg4.data, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            v0_2 = Response.success(new JSONObject(new String(arg4.data, "UTF-8")), HttpHeaderParser
                    .parseCacheHeaders(arg4));
        }
        catch(JSONException v0) {
            v0_2 = Response.error(new ParseError(((Throwable)v0)));
        }
        catch(UnsupportedEncodingException v0_1) {
            v0_2 = Response.error(new ParseError(((Throwable)v0_1)));
        }

        return v0_2;
    }
}

