package com.heimz.androidwebwechatlib.bean.Resp.listener;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.BatchGetContactResp;
import com.heimz.androidwebwechatlib.util.DebugTool;

public class GetBatchContactListener implements Listener<JSONObject> {
	
	OnResponseResult<BatchGetContactResp> onResult;
	WebWeChatService webWeChatService;
	
	
	public GetBatchContactListener(WebWeChatService arg1, OnResponseResult<BatchGetContactResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }

    public void handleResult(JSONObject arg0) {
        //ThreadPoolTool.getInstance().execute(new w(this, arg3));
    	DebugTool.saveFile(arg0.toString());
    	if(arg0!=null){
			BatchGetContactResp res =   JSON.parseObject(arg0.toString(), BatchGetContactResp.class);
			
             if(res!=null){
				
				onResult.onSuccess(res);
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


