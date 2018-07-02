package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.WxSyncResp;
import com.heimz.androidwebwechatlib.util.MLog;

import org.json.JSONObject;

public class WeChatSyncMsgListener  implements Listener<JSONObject> {

	
	OnResponseResult<WxSyncResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public WeChatSyncMsgListener(WebWeChatService arg1,
			OnResponseResult<WxSyncResp> arg2) {
		// TODO Auto-generated constructor stub
		this.webWeChatService = arg1;
        this.onResult = arg2;
		
		
	}


	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
	   MLog.d("wechatsync", arg0.toString());
		if(arg0!=null){
			
			//JSONObject v1 = new JSONObject(arg5.a().substring("window.synccheck=".length()));
			WxSyncResp res =   JSON.parseObject(arg0.toString(), WxSyncResp.class);
			
             if(res!=null&&res.BaseResponse.Ret == 0){
				
            	 webWeChatService.mClientData.mSyncKey = res.SyncKey;
				onResult.onSuccess(res);
				return;
			}else{
				onResult.onFail(-1, "object null or fail");
				return;
			}
		}else{
		
		onResult.onFail(-1, "object null or fail");
		return;
		
		}
		
	}
}
