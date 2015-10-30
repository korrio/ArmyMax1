package com.androidbegin.armymax.chat;

public class FriendContent_follow_Model {
	//private String status ;
	private String msg;
	private String Following_command;
	public FriendContent_follow_Model( String msg , String Following_command) {
		// TODO Auto-generated constructor stub
	//	this.status = status;
		this.msg = msg;
		this.Following_command = Following_command;
	}
//	public String getStatus() {
//		return status;
//	}
//	public void setStatus(String status) {
//		this.status = status;
//	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getFollowing_command() {
		return Following_command;
	}
	public void setFollowing_command(String following_command) {
		Following_command = following_command;
	}
	
}
