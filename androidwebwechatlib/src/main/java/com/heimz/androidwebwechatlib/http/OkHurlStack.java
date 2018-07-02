

package com.heimz.androidwebwechatlib.http;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class OkHurlStack extends HurlStack {
    OkUrlFactory okurlfactory;
    private OkHttpClient okhttpclient;

    public OkHurlStack(OkHttpClient arg1) {
        super();
        this.okhttpclient = arg1;
        this.init();
    }

    public OkHurlStack() {
        this(new OkHttpClient());
        this.init();
    }

    private void init() {
        try {
        	MyX509TrustManager v0_1 = new MyX509TrustManager();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{v0_1}, null);
            this.okhttpclient.setSslSocketFactory(sslContext.getSocketFactory());
        }
        catch(Exception v0) {
            throw new AssertionError();
        }

        this.okurlfactory = new OkUrlFactory(this.okhttpclient);
    }

    protected HttpURLConnection createConnection(URL arg4) {
        HttpURLConnection v0 = new OkUrlFactory(this.okhttpclient).open(arg4);
        
       // Mozilla/5.0 (Windows NT 5.1; rv:48.0) Gecko/20100101 Firefox/48.0 
        
        v0.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:48.0) Gecko/20100101 Firefox/48.0");
        v0.addRequestProperty("Referer", "https://wx.qq.com/?&lang=zh_CN");
        v0.addRequestProperty("Accept", "*/*");
        v0.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        v0.addRequestProperty("Connection", "keep-alive");
   //     v0.addRequestProperty("DNT", "1");
    //    v0.setRequestProperty("User-Agent", "QMO Uploader");
    //    v0.setRequestProperty("Pragma", "no-cache");
        v0.setRequestProperty("Content-Type", "multipart/form-data; boundary=---------------------------acebdf13572468");
        v0.setInstanceFollowRedirects(true);
        return v0;
    }
}

