package com.heimz.androidwebwechatlib.bean.Resp.listener;

import com.android.volley.Response.Listener;
import com.heimz.androidwebwechatlib.api.WebWeChatService;
import com.heimz.androidwebwechatlib.bean.OnResponseResult;
import com.heimz.androidwebwechatlib.bean.Resp.JsLoginResp;
import com.heimz.androidwebwechatlib.util.MLog;
import com.heimz.androidwebwechatlib.util.MapTool;

import java.util.HashMap;



public class NewLoginListener implements Listener<String> {
	
	
	OnResponseResult<JsLoginResp>  onResult;
	WebWeChatService webWeChatService;
	
	
	public NewLoginListener(WebWeChatService arg1, OnResponseResult<JsLoginResp> arg2) {
        this.webWeChatService = arg1;
        this.onResult = arg2;
       
    }
	
	

    public void handleResult(String arg6) {
        String v4 = null;
        int v3 = -1;
        try {
            HashMap<String,String> v1 = MapTool.NormalStringToMap(arg6);
            JsLoginResp v2 = new JsLoginResp();
            if(v1.containsKey("window.QRLogin.code")) {
                v2.window_QRLogin_code = Integer.parseInt( v1.get("window.QRLogin.code"));
                v2.window_QRLogin_uuid =  v1.get("window.QRLogin.uuid");
                if(v2.window_QRLogin_code == 200) {
                    this.webWeChatService.mClientData.mUUID = v2.window_QRLogin_uuid;
                }

              this.onResult.onSuccess(v2);
                return;
            }else{

               this.onResult.onFail(-1, null);
              return;
            }
        }
        catch(Exception v0) {
            MLog.e("get qrcode resp error: ", v0.getMessage());
            this.onResult.onFail(v3, v4);

            v0.printStackTrace();
            return;
        }
    }
    
    @Override
    public void onResponse(String arg1) {
        this.handleResult(arg1);
    }
}
