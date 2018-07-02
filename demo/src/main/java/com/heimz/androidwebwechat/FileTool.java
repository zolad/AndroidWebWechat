package com.heimz.androidwebwechat;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class FileTool {
	
	
	public static boolean saveFile(byte[] data,String name) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator +"wechatFile"+ File.separator + name+".mp3";
		} else
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator +"wechatFile"+File.separator + name+".mp3";
	   
	
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(data);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
