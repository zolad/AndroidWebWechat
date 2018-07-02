package com.heimz.androidwebwechatlib.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.util.MLog;

import java.util.Map;

public class InitRequest extends  StringRequest  {

//    public InitRequest(int method, String url, JSONObject data,
//			Listener<JSONObject> listener, ErrorListener errorlistener) {
//		super(method, url, data, listener, errorlistener);
//		// TODO Auto-generated constructor stub
//	}

	public InitRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(Method.GET,url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}
	

     @Override
    protected Response<String> parseNetworkResponse(NetworkResponse arg4) {
    	// TODO Auto-generated method stub
    	 
    	 Map<String, String> responseHeaders = arg4.headers; 
    	 
    	 MLog.d("setCookie", responseHeaders.toString());
    	 
         if(responseHeaders.containsKey("webwx_data_ticket")){
    		 
        	  WebWeChatService.getInstance().mClientData.mWebwxDataTicket = responseHeaders.get("webwx_data_ticket");
        	 
    	 }else  if(responseHeaders.containsKey("Set-Cookie")){
    		
             String rawCookies = responseHeaders.get("Set-Cookie");  
             MLog.d("setCookie", rawCookies);
             
             try{
            	 
	             int start = rawCookies.indexOf("webwx_data_ticket=")+18;
	             int end = rawCookies.indexOf(";", start);
	             
	             String str = rawCookies.substring(start, end);
	             
	             MLog.d("webwx_data_ticket=", str);
	             WebWeChatService.getInstance().mClientData.mWebwxDataTicket = str;
	             
             }catch(Exception e){
            	 
            	 MLog.e("getsetCookie", e.toString());
            	 
             }
    	 }
    	 
    	 
     	  
    	 
    	return super.parseNetworkResponse(arg4);
    }
	

//	@Override
//	protected Response<String> parseNetworkResponse(NetworkResponse arg4) {
//		
//		  Response<String> v0_2;
//	        try {
//	        	
//	        	if(arg4.headers.containsKey("webwx_data_ticket"))
//	        	   WebWeChatService.getInstance().mClientData.mWebwxDataTicket = arg4.headers.get("webwx_data_ticket");
//	        	
//	            v0_2 = Response.success(new String(arg4.data, "UTF-8"), HttpHeaderParser
//	                    .parseCacheHeaders(arg4));
//	        }
//	        catch(Exception v0) {
//	            v0_2 = Response.error(new ParseError(((Throwable)v0)));
//	        }
//	
//	        return v0_2;
//	}

}
