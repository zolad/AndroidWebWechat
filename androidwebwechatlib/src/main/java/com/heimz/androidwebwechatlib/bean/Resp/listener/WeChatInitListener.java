package com.heimz.androidwebwechatlib.bean.Resp.listener;

import org.json.JSONObject;


import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.InitResp;

public class WeChatInitListener  implements Listener<JSONObject> {

	
	OnResponseResult<InitResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public WeChatInitListener(WebWeChatService arg1,
			OnResponseResult<InitResp> arg2) {
		// TODO Auto-generated constructor stub
		this.webWeChatService = arg1;
        this.onResult = arg2;
		
		
	}


	@Override
	public void onResponse(JSONObject arg0) {
		// TODO Auto-generated method stub
	//	MLog.d("wechat", arg0.toString());
		if(arg0!=null){
			
			
	//		MLog.d("initres", arg0.toString());
	//		long st = System.currentTimeMillis();
			InitResp res =   JSON.parseObject(arg0.toString(), InitResp.class);
			webWeChatService.mClientData.mSyncKey = res.SyncKey;
			webWeChatService.mClientData.mDeviceId = webWeChatService.mUrlInterface.getRandomNumString(15);
			onResult.onSuccess(res);
			return;
			
			
			
			//DebugTool.saveFile(arg0.toString());
					
					//GsonTool.getInstance().fromJson(arg0.toString(), WebWxInitResp.class);
			
			//MLog.d("wechat", GsonTool.getInstance().toJson(res).toString());
			//Toast.makeText(ApplicationContext.getContext(), "init���ݽ���ʱ�䣺"+(System.currentTimeMillis()-st)/1000+"��",
				//	Toast.LENGTH_LONG).show();
//			if(res!=null&&res.BaseResponse.Ret == 0){
//				webWeChatService.mClientData.mSyncKey = res.SyncKey;
//				
//				onResult.onSuccess(res);
//				return;
//			}else
//				onResult.onFail(-1, "object null or fail");
			
			
		}else{
		
	    	onResult.onFail(-1, "object null or fail");
	        return;
		}
	}



}
