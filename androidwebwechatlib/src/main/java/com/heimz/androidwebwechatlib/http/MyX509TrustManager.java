

package com.heimz.androidwebwechatlib.http;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class MyX509TrustManager implements X509TrustManager {
	
	//OkHurlStack okHurlStack;
	
	public MyX509TrustManager() {
		
        super();
       // this.okHurlStack  = okHurlStack;
    }

    public void checkClientTrusted(X509Certificate[] arg1, String arg2) {
    }

    public void checkServerTrusted(X509Certificate[] arg1, String arg2) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

