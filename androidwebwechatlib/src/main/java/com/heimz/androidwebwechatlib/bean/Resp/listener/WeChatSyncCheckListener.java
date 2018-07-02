package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.SyncCheckResp;
import com.heimz.androidwebwechatlib.util.MLog;

import org.json.JSONException;
import org.json.JSONObject;

public class WeChatSyncCheckListener implements Listener<String> {
	
	
	OnResponseResult<SyncCheckResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public WeChatSyncCheckListener(WebWeChatService arg1, OnResponseResult<SyncCheckResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }

    public void handleResult(String arg6) {
        
    	if(arg6!=null){
    		 try {
				JSONObject v1 = new JSONObject(arg6.substring("window.synccheck=".length()));
				MLog.d("synccheck", v1.toString());
				SyncCheckResp res =   JSON.parseObject(v1.toString(), SyncCheckResp.class);
				onResult.onSuccess(res);
			     return;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				onResult.onFail(-1, "result trans fail");
			     
				e.printStackTrace();
				return;
			}
    		 
    		 
    		 
    		 
    	}else{
    		onResult.onFail(-1, "result null");
    		return;
    	}
    }
    
    @Override
    public void onResponse(String arg1) {
        this.handleResult(arg1);
    }
}