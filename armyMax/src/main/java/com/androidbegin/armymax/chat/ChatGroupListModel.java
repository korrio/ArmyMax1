package com.androidbegin.armymax.chat;

public class ChatGroupListModel {
	private String user_id;
	private String username;
	private String user_avatar;
	private String name;
	private String conversation_id;
	public ChatGroupListModel(
			String user_id,
			String username,
			String user_avatar,
			String name,
			String conversation_id) {
		// TODO Auto-generated constructor stub
		this.user_id = user_id;
		this.username = username;
		this.user_avatar = user_avatar;
		this.name = name;
		this.conversation_id = conversation_id;
		
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_avatar() {
		return user_avatar;
	}
	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConversation_id() {
		return conversation_id;
	}
	public void setConversation_id(String conversation_id) {
		this.conversation_id = conversation_id;
	}
	
}
