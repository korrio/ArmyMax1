package com.androidbegin.armymax.chat;

public class Chats_RecentModel {
	private String TIMESTAMPMS;
	private boolean readed;
	private Chats_MyProfileModel MYPROFILE;
	private Chats_FriendProfileModel USERFRIENDPROFILE;
	private Chats_LastMassageModel LASTMESSAGE;
	
	public Chats_RecentModel(
			String TIMESTAMPMS,
			Chats_MyProfileModel MYPROFILE,
			Chats_FriendProfileModel USERFRIENDPROFILE,
			Chats_LastMassageModel LASTMESSAGE,
			boolean readed) {
		// TODO Auto-generated constructor stub
		this.TIMESTAMPMS = TIMESTAMPMS;
		this.MYPROFILE = MYPROFILE;
		this.USERFRIENDPROFILE = USERFRIENDPROFILE;
		this.LASTMESSAGE = LASTMESSAGE;
		this.readed = readed;
	}
	public String getTIMESTAMPMS() {
		return TIMESTAMPMS;
	}
	public void setTIMESTAMPMS(String tIMESTAMPMS) {
		TIMESTAMPMS = tIMESTAMPMS;
	}
	public Chats_MyProfileModel getMYPROFILE() {
		return MYPROFILE;
	}
	public void setMYPROFILE(Chats_MyProfileModel mYPROFILE) {
		MYPROFILE = mYPROFILE;
	}
	public Chats_FriendProfileModel getUSERFRIENDPROFILE() {
		return USERFRIENDPROFILE;
	}
	public void setUSERFRIENDPROFILE(Chats_FriendProfileModel uSERFRIENDPROFILE) {
		USERFRIENDPROFILE = uSERFRIENDPROFILE;
	}
	public Chats_LastMassageModel getLASTMESSAGE() {
		return LASTMESSAGE;
	}
	public void setLASTMESSAGE(Chats_LastMassageModel lASTMESSAGE) {
		LASTMESSAGE = lASTMESSAGE;
	}
	
	public boolean getReaded() {
		return readed;
	}
	
	public void setReaded() {
		readed = true;
	}
	
}
