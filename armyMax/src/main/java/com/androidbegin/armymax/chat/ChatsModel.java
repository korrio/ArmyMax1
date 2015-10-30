package com.androidbegin.armymax.chat;

public class ChatsModel {
	private String TIMESTAMPMS;
	private String CID;
	private Chats_RecentModel recentModel;
	public ChatsModel(
			String TIMESTAMPMS,
			String CID,
			Chats_RecentModel recentModel) {
		// TODO Auto-generated constructor stub
		this.TIMESTAMPMS = TIMESTAMPMS;
		this.CID = CID;
		this.recentModel = recentModel;
	}
	public String getTIMESTAMPMS() {
		return TIMESTAMPMS;
	}
	public void setTIMESTAMPMS(String tIMESTAMPMS) {
		TIMESTAMPMS = tIMESTAMPMS;
	}
	public String getCID() {
		return CID;
	}
	public void setCID(String cID) {
		CID = cID;
	}
	public Chats_RecentModel getRecentModel() {
		return recentModel;
	}
	public void setRecentModel(Chats_RecentModel recentModel) {
		this.recentModel = recentModel;
	}
	
}
