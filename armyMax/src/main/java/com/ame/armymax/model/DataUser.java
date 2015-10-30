package com.ame.armymax.model;

import java.util.ArrayList;

import android.content.Context;

public class DataUser {

	public static String BASE = "https://www.armymax.com/photo/";
	public static String STREAMER = "rtmp://"+Data.LIVE+":1935/live/";

	public static String VM_USER_ID = "";
	public static String VM_USER_NAME = "";
	public static String VM_USER_PASS = "";
	public static String VM_NAME = "";
	public static String VM_USER_TOKEN = "";
	public static String VM_USER_AVATAR = "";
	public static String VM_USER_FB = "";
	public static String VM_USER_FB_TOKEN = "";
	public static String VM_USER_FB_ID = "";
	public static String VM_USER_FB_EMAIL = "";
	public static String VM_USER_FB_FNAME = "";
	public static String VM_USER_FB_LNAME = "";
	public static String VM_USER_FLING_COUNT = "";
	public static String VM_USER_FLER_COUNT = "";
	public static int VM_CHAT_N = 0;
	public static int VM_NOTI_N = 0;
	public static Context context;
	public static int countN = 0;

	public static String VM_USER_STREAM = STREAMER + VM_USER_NAME;
	public static boolean VM_IS_AUTHED = false;

	public static ArrayList<String> VM_USER_INFO = new ArrayList<String>();
	
	public static int chatLastTab = 0;
	
	public DataUser() {
		VM_USER_INFO.add(VM_USER_ID);
		VM_USER_INFO.add(VM_USER_NAME);
		VM_USER_INFO.add(VM_USER_PASS);
		VM_USER_INFO.add(VM_NAME);
		VM_USER_INFO.add(VM_USER_TOKEN);
		VM_USER_INFO.add(VM_USER_AVATAR);
		VM_USER_INFO.add(VM_USER_FB);
		VM_USER_INFO.add(VM_USER_FB_TOKEN);
		VM_USER_INFO.add(VM_USER_FB_ID);
		VM_USER_INFO.add(VM_USER_FB_EMAIL);
		VM_USER_INFO.add(VM_USER_FB_FNAME);
		VM_USER_INFO.add(VM_USER_FB_LNAME);
		VM_USER_INFO.add(VM_USER_FLING_COUNT);
		VM_USER_INFO.add(VM_USER_FLER_COUNT);	
	}
	
	public static void setContext(Context ctx){
        context = ctx;
    }

	public static void clearAll() {
		VM_USER_ID = "";
		VM_USER_NAME = "";
		VM_USER_PASS = "";
		VM_NAME = "";
		VM_USER_TOKEN = "";
		VM_USER_AVATAR = "";
		VM_USER_FB = "";
		VM_USER_FB_ID = "";
		VM_USER_FB_EMAIL = "";
		VM_USER_FB_FNAME = "";
		VM_USER_FB_LNAME = "";
		VM_USER_FLING_COUNT = "";
		VM_USER_FLER_COUNT = "";
		VM_USER_STREAM = "";
		VM_IS_AUTHED = false;
	}
}
