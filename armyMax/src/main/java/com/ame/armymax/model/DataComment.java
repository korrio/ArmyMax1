package com.ame.armymax.model;


public class DataComment {
	
	private String tbUrl;
	private String cName;
	private String ago;
	private String comment;

	public DataComment(String tbUrl,String cName, String ago, String comment) {
		this.tbUrl = tbUrl;
		this.cName = cName;
		this.ago = ago;
		this.comment = comment;
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
	
	public String getAgo() {
		return ago;
	}

	public void setAgo(String name) {
		ago = name;
	}
	
	public String getCommentText() {
		return comment;
	}

	public void setCommentText(String name) {
		comment = name;
	}
	
}
