package com.androidbegin.armymax.chat;

public class LoginModel {
	public static String status;
	public static String msg;
	public static String token;
	public static String userID;
	public static String username;
	public static String Name;
	public LoginModel(String status ,
					  String msg ,
					  String token ,
					  String userID ,
					  String username ,
					  String Name) {
		// TODO Auto-generated constructor stub
		this.status = status;
		this.msg = msg;
		this.token = token;
		this.userID = userID;
		this.username = username;
		this.Name = Name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
