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

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.androidquery.util.AQUtility;

public class IntentUtility {
	
	public static boolean openMarket(Activity act){
		
		return openBrowser(act, getMarketUrl(act));
		
	}
	
	
    public static String getMarketUrl(Activity act){
    	return "market://details?id=" + getAppId(act);    	
    }
    
    
    public static String getWebMarketUrl(Activity act){
    	
    	Locale locale = Locale.getDefault();    
    	String lang = locale.getLanguage();    
    	
    	if("zh".equals(lang)){
    		lang = "zh_tw";
    	}
    	
    	return "https://market.android.com/details?id=" + getAppId(act) + "&hl=" + lang;    	 	
    
    }
	
    
	private static String getAppId(Activity act){
		return act.getApplicationInfo().packageName;
	}
    
    public static boolean openBrowser(Activity act, String url) {
    
    	
    	try{
   
	    	if(url == null) return false;
	    	
	    	Uri uri = Uri.parse(url);
	    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);	    	
	    	act.startActivity(intent);
    	
	    	return true;
    	}catch(Exception e){
    		AQUtility.report(e);
    		return false;
    	}
    }
    
    /*
    public static void sendEmail(Activity act){
    	
    	Intent i = new Intent(Intent.ACTION_SEND);
    	
    	i.setType("text/plain");
    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@vikispot.com"});
    	i.putExtra(Intent.EXTRA_SUBJECT, "Feedback from User");
    	
    	try {
    	    act.startActivity(Intent.createChooser(i, "Send feedback email with..."));
    	} catch (android.content.ActivityNotFoundException ex) {
    	    //Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    	}
    }
    */
    
    public static void launchActivity(Activity act, String url){
    	
    	
    	AQUtility.debug("clicked", url);
    	
    	if(ParseUtility.isYT(url)){
    		IntentUtility.openBrowser(act, url);
    	}else if("photo".equals("")){
    		
    	}else if("video".equals("")){
    		
    		IntentUtility.openBrowser(act, url);
    	}else if(!url.contains("facebook.com")){
    		IntentUtility.openBrowser(act, url);
    	}
    	
    }
    
    
    public static void sendEmail(Activity act, String title){
    	
    	Intent i = new Intent(Intent.ACTION_SEND);
    	
    	i.setType("text/plain");
    	i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tinyeeliu@gmail.com"});
    	i.putExtra(Intent.EXTRA_SUBJECT, title);
    	
    	String select = "Send email !+!";
    	
    	try {
    	    act.startActivity(Intent.createChooser(i, select));
    	}catch(Exception ex) {
    	    //Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public static void sendShare(Activity act, String title, String text){
    	
    	
    	Intent i = new Intent(Intent.ACTION_SEND);
    	
    	i.setType("text/plain");
    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	
    	i.putExtra(Intent.EXTRA_SUBJECT, title);
    	i.putExtra(Intent.EXTRA_TEXT, text);
    	
    	String select = "Send share !+!";
    	
    	try {
    		AQUtility.debug("share act start");
    	    act.startActivity(Intent.createChooser(i, select));
    	}catch(Exception ex) {
    		AQUtility.report(ex);
    	    //Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    	}
    }
}
