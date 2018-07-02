package com.heimz.androidwebwechatlib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapTool {
	 public static HashMap<String,String> NormalStringToMap(String arg7) {
	        int v0 = 0;
	        HashMap<String,String> v1 = new HashMap();
	        String[] v2 = arg7.replaceAll(" ", "").replaceAll("\r", "").replace("\n", "").split(";", 2);
	        while(v0 < v2.length) {
	            try {
	                String[] v3_1 = v2[v0].split("=", 2);
	                ((Map)v1).put(v3_1[0], v3_1[1].replaceAll("\"", "").replaceAll(";", ""));
	            }
	            catch(ArrayIndexOutOfBoundsException v3) {
	            }

	            ++v0;
	        }

	        return v1;
	    }

	    public static HashMap<String,String> XmlStringToMap(String arg7) {
	    	HashMap<String,String> v0 = new HashMap();
	        Matcher v1 = Pattern.compile("</(.*?)>").matcher(((CharSequence)arg7));
	       while(v1.find()) {
	            String v2 = v1.group().replace("<", "").replace("/", "").replace(">", "");
	            Matcher v3 = Pattern.compile("<" + v2 + ">(.*?)</" + v2 + ">").matcher(((CharSequence)arg7));            
	                if(v3.find()) {
	                	
	                	 v0.put(v2, v3.group().replace("<" + v2 + ">", "").replace("</" + v2 + ">", ""));
	                }     
	               
	               
	     }
	        

	        return v0;
	    }
}
