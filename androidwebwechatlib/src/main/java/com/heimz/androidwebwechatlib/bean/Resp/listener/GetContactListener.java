package com.heimz.androidwebwechatlib.bean.Resp.listener;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.GetContactResp;
import com.heimz.androidwebwechatlib.util.DebugTool;

public class GetContactListener implements Listener<JSONObject> {
	
	OnResponseResult<GetContactResp> onResult;
	WebWeChatService webWeChatService;
	
	
	public GetContactListener(WebWeChatService arg1, OnResponseResult<GetContactResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }

    public void handleResult(JSONObject arg0) {
        //ThreadPoolTool.getInstance().execute(new w(this, arg3));
    	//MLog.d("contact", arg0.toString());
    	if(arg0!=null){
    		GetContactResp res =   JSON.parseObject(arg0.toString(), GetContactResp.class);
			DebugTool.saveFile(arg0.toString());
             if(res!=null){
				
				onResult.onSuccess(res);
				return;
			}else{
				onResult.onFail(-1, "trans object fail");
				return;
			}
			
			
    	}else{
    	   onResult.onFail(-1, "object null or fail");
    	   return;
    	}
    }
    

	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
		  this.handleResult(arg0);
	}
}