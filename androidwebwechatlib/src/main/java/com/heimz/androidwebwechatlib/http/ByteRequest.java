

package com.heimz.androidwebwechatlib.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.UnsupportedEncodingException;

public class ByteRequest extends Request<byte[]> {
    private final Listener listener;
    private String params;

    //method  url  listener
    public ByteRequest(int method, String url, Listener listenr, ErrorListener errorlistner) {
        super(method, url, errorlistner);
        this.listener = listenr;
    }

    public ByteRequest(int method, String url, String params, Listener listenr, ErrorListener errorlistner) {
        //this(arg1, arg2, arg4, arg5);
        this(method, url,listenr, errorlistner);
        this.params = params;
    }

    public ByteRequest(String url, Listener listenr, ErrorListener errorlistner) {
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
    public byte[] getBody() {
        byte[] v0 = null;
        try {
            if(this.params == null) {
                return v0;
            }

            v0 = this.params.getBytes("utf-8");
        }
        catch(UnsupportedEncodingException v1) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", new Object
                    []{this.params, "utf-8"});
        }

        return v0;
    }

	@Override
	protected Response<byte[]> parseNetworkResponse(NetworkResponse arg0) {
		// TODO Auto-generated method stub
		return Response.success(arg0.data, HttpHeaderParser.parseCacheHeaders(arg0));
	}

//	@Override
//	protected Response<Object> parseNetworkResponse(NetworkResponse arg0) {
//		// TODO Auto-generated method stub
//		return Response.success(arg0.data, HttpHeaderParser.parseCacheHeaders(arg0));
//	}

	

	
 
	
	
}

