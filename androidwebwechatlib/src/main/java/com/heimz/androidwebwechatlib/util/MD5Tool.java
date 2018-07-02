package com.heimz.androidwebwechatlib.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Tool {

	
	public static String getFileMD5(File file) {
		  if (!file.isFile()) {
		   return null;
		  }
		  MessageDigest digest = null;
		  FileInputStream in = null;
		  byte buffer[] = new byte[1024];
		  int len;
		  try {
		   digest = MessageDigest.getInstance("MD5");
		   in = new FileInputStream(file);
		   while ((len = in.read(buffer, 0, 1024)) != -1) {
		    digest.update(buffer, 0, len);
		   }
		   in.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		   return null;
		  }finally{
			  
		  }
		  BigInteger bigInt = new BigInteger(1, digest.digest());
		  return bigInt.toString(16);
	}
}
