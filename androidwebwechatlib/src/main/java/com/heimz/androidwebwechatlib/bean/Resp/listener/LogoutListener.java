package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.LogoutResp;

public class LogoutListener implements Listener<byte[]> {
	
	
	OnResponseResult<LogoutResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public LogoutListener(WebWeChatService arg1, OnResponseResult<LogoutResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }
	public void handleResult(String arg6) {
	      
		//MLog.d("logout", arg6);
		onResult.onSuccess(new LogoutResp());
		
		
     }

	@Override
	public void onResponse(byte[] arg1) {
	        this.handleResult(new String(arg1));
	 }
}
