package com.heimz.androidwebwechatlib.util;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

public class DebugTool {
	public static void saveFile(String str) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "debug.txt";
		} else
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "debug.txt";
	   
	
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(str.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void writeDataIntoFile(String str) {
		String filePath = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "initdebug.txt";
		} else
			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "initdebug.txt";
	   
	
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file,true);
			outStream.write(str.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
