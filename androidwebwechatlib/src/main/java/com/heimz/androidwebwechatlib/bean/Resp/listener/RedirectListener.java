package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.NewLoginPageResp;
import com.heimz.androidwebwechatlib.util.MLog;
import com.heimz.androidwebwechatlib.util.MapTool;

import java.util.HashMap;

public class RedirectListener implements Listener<String> {
	
	
	OnResponseResult<NewLoginPageResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public RedirectListener(WebWeChatService arg1, OnResponseResult<NewLoginPageResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }


	public void handleResult(String arg6) {
		
		
		MLog.e("redirect", arg6);
        try {
        	 HashMap<String,String> v1 = MapTool.XmlStringToMap(arg6);
            NewLoginPageResp v2 = new NewLoginPageResp();
            
            v2.ret = Integer.parseInt(v1.get("ret"));
            if(v2.ret == 0) {
                v1 = MapTool.XmlStringToMap(v1.get("error"));
                v2.skey = v1.get("skey");
                v2.wxsid = v1.get("wxsid");
                v2.wxuin = v1.get("wxuin");
                v2.pass_ticket = v1.get("pass_ticket");
                
                MLog.e("redirect",v2.skey+" "+v2.wxsid+" "+ v2.wxuin);
                
                if(v1.containsKey("isgrayscale"))
                    v2.isgrayscale = Integer.parseInt(v1.get("isgrayscale"));
                v2.message = v1.get("message");
                this.webWeChatService.mClientData.mWxSkey = v2.skey;
                this.webWeChatService.mClientData.mWxSid = v2.wxsid;
                this.webWeChatService.mClientData.mWxUin = v2.wxuin;
                this.webWeChatService.mClientData.mPassTicket = v2.pass_ticket;
            }

            this.onResult.onSuccess(v2);
            return;
        }
        catch(Exception v0) {
        	MLog.e("redirect", v0.toString());
            this.onResult.onFail(-1, v0.getMessage() + ", raw=" + arg6);
       
            v0.printStackTrace();
            return;
        }
    }
	
	
	
	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		this.handleResult(arg0);
		
	}

}
