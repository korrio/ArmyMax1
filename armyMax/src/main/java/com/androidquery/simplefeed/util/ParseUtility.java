/*******************************************************************************
 * Copyright 2012 AndroidQuery (tinyeeliu@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Additional Note:
 * 1. You cannot use AndroidQuery's Facebook app account in your own apps.
 * 2. You cannot republish the app as is with advertisements.
 ******************************************************************************/
package com.androidquery.simplefeed.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidquery.util.AQUtility;

public class ParseUtility {

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	public static long parseTime(String time){
		
		if(time == null) return 0;
		
		Date date;
		try {
			date = df.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			AQUtility.report(e);
		}
		
		return 0;
	}
	
	public static boolean isYT(String url){
		
		if(url == null) return false;
		
		return url.startsWith("http://www.youtube.com") || url.startsWith("http://youtu.be");
		
	}
	
	public static String profileTb(String id){
    	//String url = "https://api.vdomax.com/chat/?action=getprofilepic&UserID=" + id + "&token="
		//		+ AppUtility.VM_USER_TOKEN;
    	String url = "debug";
    	return url;
    }
	
	public static boolean isNumber(String str){
		
		return toNumber(str) != null;
		
	}
	
	public static Long toNumber(String str){
		
		try{
			return new Long(str);
		}catch(Exception e){
			return null;
		}
		
	}
	
	public static String resolveId(String id){
		if(id == null) return null;
		if(isNumber(id)) return id;
		return "@" + id;
		
	}
	
	public static Map<String, List<String>> getQueryParams(String url) {
	    try {
	        Map<String, List<String>> params = new HashMap<String, List<String>>();
	        String[] urlParts = url.split("\\?");
	        if (urlParts.length > 1) {
	            String query = urlParts[1];
	            for (String param : query.split("&")) {
	                String[] pair = param.split("=");
	                String key = URLDecoder.decode(pair[0], "UTF-8");
	                String value = "";
	                if (pair.length > 1) {
	                    value = URLDecoder.decode(pair[1], "UTF-8");
	                }

	                List<String> values = params.get(key);
	                if (values == null) {
	                    values = new ArrayList<String>();
	                    params.put(key, values);
	                }
	                values.add(value);
	            }
	        }

	        return params;
	    } catch (UnsupportedEncodingException ex) {
	        throw new AssertionError(ex);
	    }
	}
	
}
