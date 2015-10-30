package com.ame.armymax.model;


public class DataSearch {
	
	private String tbUrl;
	private String cName;
	private String userName;
	private String type;

	public DataSearch(String tbUrl,String cName, String userName, String type) {
		this.tbUrl = tbUrl;
		this.cName = cName;
		this.userName = userName;
		this.type = type;
	}
	
	public String getTbUrl() {
		return tbUrl;
	}

	public void setTbUrl(String name) {
		tbUrl = name;
	}
	
	public String getName() {
		return cName;
	}

	public void setName(String name) {
		cName = name;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		userName = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String name) {
		type = name;
	}
	
}
