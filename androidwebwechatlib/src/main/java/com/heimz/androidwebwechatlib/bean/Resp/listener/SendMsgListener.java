package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.SendAppMsgResp;
import com.heimz.androidwebwechatlib.util.MLog;

import org.json.JSONObject;

public class SendMsgListener implements Listener<JSONObject> {
	
	OnResponseResult<SendAppMsgResp> onResult;
	WebWeChatService webWeChatService;
	
	
	public SendMsgListener(WebWeChatService arg1, OnResponseResult<SendAppMsgResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }

    public void handleResult(JSONObject arg0) {
        //ThreadPoolTool.getInstance().execute(new w(this, arg3));
    	MLog.d("sendappmsgres", arg0.toString());
    	if(arg0!=null){
    		SendAppMsgResp res =   JSON.parseObject(arg0.toString(), SendAppMsgResp.class);
			//DebugTool.saveFile(arg0.toString());
             if(res!=null&&res.BaseResponse.Ret == 0){
				
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
