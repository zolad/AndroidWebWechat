package com.heimz.androidwebwechatlib.bean.Resp.listener;

import android.os.Environment;
import android.text.TextUtils;

import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.DownLoadFileResp;
import com.heimz.androidwebwechatlib.util.FileTool;

import java.io.File;

public class DownLoadAppMsgListener implements Listener<byte[]> {
	
	
	OnResponseResult<DownLoadFileResp>  onResult;
	WebWeChatService webWeChatService;
	String MsgId;
	String fromUserName;
	String MemberUserName;
	String memberNickName;
	String fileName;
	
	
	public DownLoadAppMsgListener(WebWeChatService arg1, OnResponseResult<DownLoadFileResp> arg2, String mediaId, String fromUserName, String MemberUserName, String memberNickName, String fileName) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
        this.MsgId = mediaId;
        this.fromUserName = fromUserName;
        this.MemberUserName = MemberUserName;
        this.memberNickName = memberNickName;
		this.fileName = fileName;
    }
	
	@Override
	public void onResponse(byte[] arg1) {
	      if(arg1!=null){
	    	  
	    	  DownLoadFileResp  res = new DownLoadFileResp();
	    	//  res.data = arg1;
	    	  res.fromUserName = fromUserName;
	    	  res.fromMemberUserName = MemberUserName;
	    	  res.fromMemberNickName = memberNickName;


	    	  res.filepath = FileTool.saveFile(arg1, TextUtils.isEmpty(this.fileName)? MsgId:this.fileName)?
	    			  Environment.getExternalStorageDirectory().toString() + File.separator +"wechatFile"+ File.separator +  (TextUtils.isEmpty(this.fileName)? MsgId:this.fileName)
	    			  :""
	    			;
	    	  
	    	  onResult.onSuccess(res);
	    	  return;
	    	  
	      }else{
	    	onResult.onFail(-1, "object null");
	    	return;
	      }
	 }
	
//	public void handleResult(String arg6) {
//	      
//		//MLog.d("logout", arg6);
//		//onResult.onSuccess(new LogoutResp());
//		
//		
//     }

	
}