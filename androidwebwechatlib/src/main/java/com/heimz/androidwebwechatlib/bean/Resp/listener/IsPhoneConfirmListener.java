package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.LoginResp;
import com.heimz.androidwebwechatlib.util.MLog;
import com.heimz.androidwebwechatlib.util.MapTool;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsPhoneConfirmListener implements Listener<String> {
	
	
	OnResponseResult<LoginResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public IsPhoneConfirmListener(WebWeChatService arg1, OnResponseResult<LoginResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }
	public void handleResult(String arg6) {
	        try {
	            HashMap<String,String> v1 = MapTool.NormalStringToMap(arg6);
	            LoginResp v2 = new LoginResp();
	            MLog.i("login resp: ",arg6);
	            v2.window_code = Integer.parseInt(v1.get("window.code"));
	            v2.window_redirect_uri = v1.get("window.redirect_uri") + "&fun=new&version=v2";
	            v2.window_user_avatar = v1.get("window.userAvatar");
	            MLog.i("window_code = ", ""+v2.window_code);
	            if(v2.window_code == 200) {
	                Matcher v0_1 = Pattern.compile("https://[\\w\\.]+/").matcher(v2.window_redirect_uri);
	                if(v0_1.find()) {
	                	this.webWeChatService.mClientData.mHost = v0_1.group().replace("https:/", "").replace("/", "");
	                	 MLog.i("WebWeChatService::login, host changed, newHost=" ,
	                			 this.webWeChatService.mClientData.mHost);
	                }

	                this.webWeChatService.mClientData.mRedirectUrl= v2.window_redirect_uri;
	            }

	            this.onResult.onSuccess(v2);
	            return;
	        }
	        catch(Exception v0) {
	        	 this.onResult.onFail(-1, null);
	            v0.printStackTrace();
	            return;
	        }
	    }

	@Override
	    public void onResponse(String arg1) {
	        this.handleResult(arg1);
	    }
}
