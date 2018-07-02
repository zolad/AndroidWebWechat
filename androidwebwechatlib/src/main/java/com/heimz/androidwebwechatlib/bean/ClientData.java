package com.heimz.androidwebwechatlib.bean;

import java.io.Serializable;

public class ClientData implements Serializable {
    	 public  String mRedirectUrl;
        public transient SyncKeyEntity mSyncKey;
        public String mHost;
        public String mPassTicket;
        public String mUUID;
        public String mWxSid;
        public String mWxSkey;
        public String mWxUin;
        public String mWebwxDataTicket;
        public String mDeviceId;

        public ClientData() {
            super();
            this.mHost = "wx.qq.com";
        }

        public void clear() {
            this.mHost = "wx.qq.com";
            this.mRedirectUrl = null;
            this.mUUID = null;
            this.mWxUin = null;
            this.mWxSkey = null;
            this.mWxSid = null;
            this.mPassTicket = null;
            this.mSyncKey = null;
            this.mWebwxDataTicket = null;
        }

        public String toString() {
            return "ClientData{mHost=\'" + this.mHost + '\'' + ", mRedirectUrl=\'" + this.mRedirectUrl + '\'' + ", mUUID=\'"
                     + this.mUUID + '\'' + ", mWxUin=\'" + this.mWxUin + '\'' + ", mWxSkey=\'" + this.mWxSkey
                     + '\'' + ", mWxSid=\'" + this.mWxSid + '\'' + ", mPassTicket=\'" + this.mPassTicket
                     + '\'' + ", mSyncKey=" + this.mSyncKey +  ", mWebwxDataTicket=" + this.mWebwxDataTicket +'}';
        }
    

}
