package com.heimz.androidwebwechatlib.util;

import com.google.gson.Gson;

public class GsonTool {
	
	private static  final Gson gson =  new Gson();
	
	public static  Gson getInstance(){
		
		return gson;
	}

}
