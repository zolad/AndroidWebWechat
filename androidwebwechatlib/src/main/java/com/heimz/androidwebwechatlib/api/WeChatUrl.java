package com.heimz.androidwebwechatlib.api;

import android.text.TextUtils;
import android.util.Log;

import com.heimz.androidwebwechatlib.bean.ClientData;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Random;


public class WeChatUrl {
    private ClientData clientData;
    // private String syncCheckHost="";

    private  final static String[] syncCheckHostList = new String[]{
            "webpush.","webpush2.","webpush.","webpush2."
    };


    public WeChatUrl(ClientData arg1) {
        super();
        this.clientData = arg1;
    }


    //第一次登陆，获取uuid
    public String getNewLoginUuid() {
        return "https://login.weixin.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_="
                + System.currentTimeMillis();
    }

    public String SyncCheck(String synckey,int retrytimes) {


        int v4 = 15;
        String deviceId = TextUtils.isEmpty(clientData.mDeviceId)?getRandomNumString(15):clientData.mDeviceId;
        String v0 = clientData.mHost.equals("web.wechat.com") ? "https://webpush.wechat.com/cgi-bin/mmwebwx-bin/synccheck?r="
                + System.currentTimeMillis() + "&skey=" + clientData.mWxSkey + "&sid=" +
                this.encodeUTF8(clientData.mWxSid) + "&uin=" + clientData.mWxUin + "&deviceid=e" + deviceId + "&synckey="
                + this.encodeUTF8(synckey) + "&pass_ticket=" + this.encodeUTF8(clientData.mPassTicket)
                // +"&r=" + System.currentTimeMillis() +"&_=" + System.currentTimeMillis()

                :

                "https://"+syncCheckHostList[retrytimes] +
                        clientData.mHost+
                        // clientData.mHost.replace("wx", "").replace(".qq.com", "") + "." +
                        //syncCheckHostList[0]+
                        "/cgi-bin/mmwebwx-bin/synccheck?r="
                        + System.currentTimeMillis() + "&skey=" + clientData.mWxSkey + "&sid=" +
                        this.encodeUTF8(clientData.mWxSid)+ "&uin=" + clientData.mWxUin + "&deviceid=e" + deviceId + "&synckey="
                        + this.encodeUTF8(synckey) + "&pass_ticket=" + this.encodeUTF8(clientData.mPassTicket)
                // + "&r=" + System.currentTimeMillis() +"&_=" + System.currentTimeMillis()

                ;

        Log.d("syncurlcheck", "https://webpush." +clientData.mHost);
        return v0;
    }

    public String getUserHeadImage(String username,String chatroomid) {

        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxgeticon?seq="+0+"&username=" + username + "&skey="
                + clientData.mWxSkey+"&chatroomid="+chatroomid;
    }

    //查询手机端是否点击了确认登陆
    public String IsPhoneConfirm() {
        //j.a("muuid: " + clientData.mUUID); 保存日志到本地
        return "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=" + clientData
                .mUUID + "&tip=0&r=" + this.getNOTTimeStamp() +"&_=" +
                System.currentTimeMillis()+ "&lang=zh_CN";
    }

    //获取语音
    public String getVoice(String msgid) {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxgetvoice?msgid=" + msgid + "&skey="
                + clientData.mWxSkey;
    }

    // 微信初始化，返回一系列信息

    //this.encodeUTF8()
    public String WebWxInitInform() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxinit?&lang=zh_CN&pass_ticket="
                + this.encodeUTF8(clientData.mPassTicket) +"&lang=zh_CN"+  "&r=" + getNOTTimeStamp()+"&_=" +
                System.currentTimeMillis();
    }

    //获取头像图片
    public String getGroupHeadImage(String username) {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxgetheadimg?seq=627090357&username="
                + username + "&skey=" + clientData.mWxSkey;
    }


    private String encodeUTF8(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8");
        }
        catch(Exception v0) {
        }

        return str;
    }

    public String WxStatusNotify() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxstatusnotify?lang=zh_CN&pass_ticket="
                + this.encodeUTF8(clientData.mPassTicket);
    }

    //获取联系人
    public String getContact() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxgetcontact?lang=zh_CN&pass_ticket=" + this
                .encodeUTF8(clientData.mPassTicket) + "&r=" + System.currentTimeMillis() + "&seq=0&skey=" + clientData.mWxSkey;
    }

    //同步最新消息
    public String SyncMsg() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxsync?sid=" + this.encodeUTF8(clientData.mWxSid)
                + "&skey=" + clientData.mWxSkey;// + "&pass_ticket=" + this.encodeUTF8(clientData.mPassTicket);
    }

    //群组列表
    public String getBatchContact() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxbatchgetcontact?type=ex&r=" +
                System.currentTimeMillis() + "&lang=zh_CN&pass_ticket=" + this.encodeUTF8(clientData.mPassTicket);
    }

    //发送消息
    public String SendTextMsg() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket="
                + this.encodeUTF8(clientData.mPassTicket);
    }

    //发送消息图片
    public String SendMsgImg() {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxsendmsgimg?fun=async&f=json&pass_ticket="
                + this.encodeUTF8(clientData.mPassTicket);
    }

    //登出
    public String Logout(int type) {
        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxlogout?redirect=1&type="+type+"&skey="
                + clientData.mWxSkey;
    }




    public String upLoad() {
        return "http://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxpreview?fun=upload";
    }

    public String upLoadAppMsg() {
        return "https://file." + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
    }

    //第二线路上传文件
    public String upLoadAppMsg2() {
        return "https://file2." + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
    }

    public String SendAppMsg(){

        return "https://" + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxsendappmsg?fun=async&f=json";
    }

    public String DonwloadAppMsg(String mediaId,String fileName,String WebwxDataTicket){

        return "https://file." + clientData.mHost + "/cgi-bin/mmwebwx-bin/webwxgetmedia?mediaid="+mediaId+"&filename="+fileName+

                "&pass_ticket="+this.encodeUTF8(clientData.mPassTicket)+"&webwx_data_ticket="+WebwxDataTicket;
    }


    public JSONObject getIdentifyParams() {
        // JSONObject v0 = new JSONObject();
        JSONObject v1 = new JSONObject();
        try {
            String deviceId = TextUtils.isEmpty(clientData.mDeviceId)?getRandomNumString(15):clientData.mDeviceId;
            String br = "{\"Uin\":"+clientData.mWxUin+
                    ",\"Sid\":\""+clientData.mWxSid+
                    "\",\"Skey\":\""+clientData.mWxSkey+
                    "\",\"DeviceID\":\"e" + deviceId+"\"}";


//            v0.put("Uin", Long.parseLong(clientData.mWxUin));
//            v0.put("Sid", clientData.mWxSid);
//            v0.put("Skey", clientData.mWxSkey);
//            v0.put("DeviceID", "e" + this.getRandomNumString(15));
            v1.put("BaseRequest", br);
        }
        catch(Exception v0_1) {
            v0_1.printStackTrace();
        }

        return v1;
    }

    public String getNOTTimeStamp() {
        long curtime =  System.currentTimeMillis();
        int notTime =  ~((int)curtime);

        return String.valueOf(notTime);
    }

    public String getRandomNumString(int count) {
        StringBuilder v1 = new StringBuilder();
        Random v2 = new Random();
        int v0;
        for(v0 = 0; v0 < count; ++v0) {
            v1.append(v2.nextInt(10));
        }

        if(v1.charAt(0) == '0')
            v1.setCharAt(0, '6');

        return v1.toString();
    }
}

