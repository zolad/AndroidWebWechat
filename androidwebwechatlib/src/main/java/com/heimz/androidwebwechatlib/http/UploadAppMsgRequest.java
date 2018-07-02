package com.heimz.androidwebwechatlib.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class UploadAppMsgRequest extends Request<byte[]> {
	

	private final Listener listener;
	public ByteArrayOutputStream stream;
	
	
	 public UploadAppMsgRequest(int method, String url, Listener listenr,
				ErrorListener errorlistner,ByteArrayOutputStream arg6) {
			 
		
			this(method, url, listenr, errorlistner);
			 this.stream = arg6;
			// TODO Auto-generated constructor stub
		}

	  //method  url  listener
    public UploadAppMsgRequest(int method, String url, Listener listenr, ErrorListener errorlistner) {
        super(method, url, errorlistner);
        this.listener = listenr;
    }



    public UploadAppMsgRequest(String url, Listener listenr, ErrorListener errorlistner) {
        this(Method.GET,  url, listenr, errorlistner);
    }
    
    
    protected void onResponse(byte[] res) {
        this.listener.onResponse(res);
    }
    
    @Override
    protected void deliverResponse(byte[] arg1) {
        this.onResponse(arg1);
    }
	

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	// TODO Auto-generated method stub
    	
    	
    	return super.getHeaders();
    }
    
    
	  @Override
	    public byte[] getBody() {
		  return this.stream.toByteArray();
	    }

	@Override
	protected Response<byte[]> parseNetworkResponse(NetworkResponse arg0) {
		// TODO Auto-generated method stub
		return Response.success(arg0.data, HttpHeaderParser.parseCacheHeaders(arg0));
	}

//       @Override
//	  public byte[] getBody() {
//	        return this.stream.toByteArray();
//	  }
	
	
	
	

}
