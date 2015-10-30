package com.ame.armymax.model;


public class DataNoti {

	private String notiId;
	private String type;
	private String myName;
	private String fromName;
	private String myId;
	private String fromId;
	private String postId;
	private String extra;
	private String ago;
	private String msg;
	private boolean readed;

	public DataNoti(String notiId,String type,String myName,String fromName,String myId,String fromId,String postId,String extra,String ago,String msg,boolean readed) {
		this.notiId = notiId;
		this.type = type;
		this.myName = myName;
		this.fromName = fromName;
		this.myId = myId;
		this.fromId = fromId;
		this.postId = postId;
		this.extra = extra;
		this.ago = ago; 
		this.readed = readed;
		this.msg = msg;
	}
	
	public String getNotiId() {
		return notiId;
	}

	public void setNotiId(String name) {
		this.notiId = name;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String name) {
		this.extra = name;
	}
	
	public String getPostId() {
		return postId;
	}

	public void setPostId(String name) {
		this.postId = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String name) {
		type = name;
	}

	public String getMyId() {
		return myId;
	}

	public void setMyId(String name) {
		myId = name;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String name) {
		fromId = name;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String name) {
		myName = name;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String name) {
		fromName = name;
	}
	
	public boolean getReaded() {
		return readed;
	}

	public void setReaded(boolean name) {
		readed = name;
	}

	public String getAgo() {
		return ago;
	}

	public void setAgo(String name) {
		ago = name;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String name) {
		msg = name;;
	}

	public void clearAll() {

	}
}
