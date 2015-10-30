package com.ame.armymax.model;

import java.util.ArrayList;
import java.util.List;

public class DataPeople {
	/*
	 * static final String BASE = "http://i.imgur.com/"; static final String EXT
	 * = ".jpg";
	 */
	static final String BASE = "https://www.vdomax.com/photo/";
	static final String EXT = "";

	private String id; 
	private String name;
	private String username;
	private String avatar; 

	public DataPeople(String id,String name, String username, String avatar) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.avatar = avatar;
	}

	public void setUsername(String name) {
		username = name;
	}

	public String getUsername() {
		return username;
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		id = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String name) {
		avatar = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clearAll() {
		
	}
}
