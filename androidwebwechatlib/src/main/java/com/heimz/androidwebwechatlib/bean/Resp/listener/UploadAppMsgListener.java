package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.UploadFileResp;
import com.heimz.androidwebwechatlib.util.MLog;

import java.io.UnsupportedEncodingException;

public class UploadAppMsgListener implements Listener<byte[]> {

	OnResponseResult<UploadFileResp> onResult;
	WebWeChatService webWeChatService;
	
	public UploadAppMsgListener(WebWeChatService client,
			OnResponseResult<UploadFileResp> onresult) {
		// TODO Auto-generated constructor stub
		  this.webWeChatService = client;
	        this.onResult = onresult;
	}

	@Override
	public void onResponse(byte[] arg0) {
		// TODO Auto-generated method stub
		if(arg0!=null){
			
		String str = null;
		try {
			str = new String(arg0,"UTF-8");
			MLog.e("uploadapp", str);
			UploadFileResp res = JSON.parseObject(str,UploadFileResp.class);
			
			 if(res!=null&&res.BaseResponse.Ret == 0){
					
					onResult.onSuccess(res);
					return;
				}else{
					onResult.onFail(-1, str);
					return;
				}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			onResult.onFail(-1, "object null or fail");
			e.printStackTrace();
			return;
		}
		
		}else{
		
		onResult.onFail(-1, "object null or fail");
	     return;
		}
	}

}
