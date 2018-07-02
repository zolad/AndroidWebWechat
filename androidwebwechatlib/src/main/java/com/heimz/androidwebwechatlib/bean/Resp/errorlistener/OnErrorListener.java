

package com.heimz.androidwebwechatlib.bean.Resp.errorlistener;

import com.android.volley.NoConnectionError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.util.MLog;

public class OnErrorListener implements ErrorListener {
	
    private OnResponseResult onError;
    public static final int UNKNOWN_ERROR = Integer.MIN_VALUE;

    public OnErrorListener(OnResponseResult arg1) {
        super();
        this.onError = arg1;
    }

    public OnErrorListener() {
        super();
    }

    public void onErrorResponse(VolleyError arg5) {
    	if (arg5 instanceof NoConnectionError) {
			MLog.i("Garment0929", "NoConnectionError");
		}
    	if (arg5 instanceof TimeoutError) {
			MLog.i("Garment0929", "TimeoutError");
		}
    	if (arg5 instanceof ServerError) {
			MLog.i("Garment0929", "ServerError");
		}
        int v0 = arg5.networkResponse == null ? UNKNOWN_ERROR : arg5.networkResponse.statusCode;
        if (arg5.networkResponse != null) {
            MLog.i("Garment0929", "OnErrorResponse content:" + new String(arg5.networkResponse.data));
		}
        
//        if(((arg5 instanceof NoConnectionError)) || ((arg5 instanceof TimeoutError))) {
//            v0 = 0;
//        }

        this.onError.onFail(v0, arg5.getClass() + "::" + arg5.getCause() + "::" + arg5.getMessage());
    }
}

