package com.heimz.androidwebwechatlib.api;

import android.content.Context;
import android.os.Environment;

import com.heimz.androidwebwechatlib.bean.ContactEntity;
import com.heimz.androidwebwechatlib.bean.Resp.InitResp;

import java.io.File;
import java.util.ArrayList;

public class ApplicationContext {
	
	    private static Context context;
	    private static boolean isInitial  = false;
	    public  static InitResp initResult = null;
	    public static  ArrayList<ContactEntity>  contactList = null;
	    public static int  FileIndex = 0;



	    public static void init(Context arg2) {
	    	context = arg2.getApplicationContext();
	    	//isInitial = "com.cld.interphone".equals(context.getApplicationInfo().packageName);
	    	File file = new File(Environment.getExternalStorageDirectory()+"/wechatFile");
			if(!file.exists())
				file.mkdirs();
			file = null;
	    }

	    public static boolean isInitial() {
	        return isInitial;
	    }

	    public static Context getContext() {
	        if(context == null) {
	            throw new IllegalStateException("you have not yet initialized the sdk context !");
	        }

	        return context;
	    }
	
}
