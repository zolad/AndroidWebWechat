package com.heimz.androidwebwechatlib.bean;

public interface OnResponseResult<T> {
	
	  void onFail(int tag, String msg);

	  void onSuccess(T result);

}

