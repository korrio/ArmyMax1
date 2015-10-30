package com.ame.armymax.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserHelper {
	Context context;
	SharedPreferences sharedPerfs;
	Editor editor;
	
	// Prefs Keys
	static String perfsName = "ArmyMax";
	static int perfsMode = 0;
	
//	static String KEY_STATUS = LoginModel.status;
//	static String KEY_MSG = LoginModel.msg;
	
	static String KEY_TOKEN = "token";
	static String KEY_USERID = "userid";
	static String KEY_USERNAME = "username";
	static String KEY_NAME = "name";
	static String KEY_REMEMBER = "isRemember";
	//static String KEY_NAME = LoginModel.Name;
	
	public UserHelper(Context context) {
		this.context = context;
		this.sharedPerfs = this.context.getSharedPreferences(perfsName, perfsMode);
		this.editor = sharedPerfs.edit();
	}
	
	public void createSession(String token , String userID , String userName, String name, boolean isRemember) {
		
		editor.putBoolean("isLogin", true);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USERID, userID);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_NAME, name);
        editor.putBoolean(KEY_REMEMBER, isRemember);
        editor.commit();
	}
	
	public void deleteSession() {
		editor.clear();
		editor.commit();
	}
	
	public boolean isLogin() {
		return sharedPerfs.getBoolean("isLogin", false);
	}
	
	public String getUserID() {
		return sharedPerfs.getString(KEY_USERID, null);
	}
	public String getToken() {
		return sharedPerfs.getString(KEY_TOKEN, null);
	}
	public String getUserName() {
		return sharedPerfs.getString(KEY_USERNAME, null);
	}
	public String getName() {
		return sharedPerfs.getString(KEY_NAME, null);
	}
	
	public boolean isRemember() {
		return sharedPerfs.getBoolean(KEY_REMEMBER, false);
	}
}
