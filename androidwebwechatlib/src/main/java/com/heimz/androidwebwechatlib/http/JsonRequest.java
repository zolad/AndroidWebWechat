

package com.heimz.androidwebwechatlib.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonRequest extends JsonObjectRequest {

	
    public JsonRequest(String url, Listener<JSONObject> listener, ErrorListener errorlistener, String data) {
        super(Method.GET,url, data, listener,  errorlistener);
    }


//    
//    public JsonRequest(int method, String url, String data, Listener<JSONObject> listener, ErrorListener errorlistener) {
//        super(method, url, data, listener,  errorlistener);
//    }
    
    public JsonRequest(int method, String url, JSONObject data, Listener<JSONObject> listener, ErrorListener errorlistener) {
        super(method, url, data == null ? null : data, listener,  errorlistener);
    }
    
//    @Override
//    public Map getHeaders() {
//        HashMap v0 = new HashMap();
//        v0.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//        v0.put("Referer", "https://wx2.qq.com/?&lang=zh_CN");
//        return v0;
//    }
    
    public JsonRequest(int method, String url, String data, Listener<JSONObject> listener, ErrorListener errorlistener) {
        super(method, url, data , listener,  errorlistener);
    }


	@Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	// TODO Auto-generated method stub
    	HashMap<String, String> v0 = new HashMap<String, String>();
    	 v0.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
       v0.put("Referer", "https://wx2.qq.com/?&lang=zh_CN");
       return v0;
    }



	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse arg0) {
		// TODO Auto-generated method stub
		
//	    try {
//			MLog.d("jsonrequest", new String(arg0.data, "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return super.parseNetworkResponse(arg0);
	}
    
//    @Override
//    protected Response<JSONObject> parseNetworkResponse(NetworkResponse arg4) {
//      	
//        Response<JSONObject> v0_2;
//        try {
//            v0_2 = Response.success(new JSONObject(new String(arg4.data, "UTF-8")), HttpHeaderParser
//                    .parseCacheHeaders(arg4));
//        }
//        catch(JSONException v0) {
//            v0_2 = Response.error(new ParseError(((Throwable)v0)));
//        }
//        catch(UnsupportedEncodingException v0_1) {
//            v0_2 = Response.error(new ParseError(((Throwable)v0_1)));
//        }
//
//        return v0_2;
//    }
}

