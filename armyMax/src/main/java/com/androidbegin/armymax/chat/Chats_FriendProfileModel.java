package com.androidbegin.armymax.chat;

public class Chats_FriendProfileModel {
	private String FRIENDID;
	private String FRIENDNAME;
	private String USERFIRSTNAME;
	private String USERLASTNAME;
	private String FRIENDIMAGE;
	private String USERTOKEN;
	private String USERTOKENMOBILE;
	private String STATUS;
	public Chats_FriendProfileModel(
			 String FRIENDID,
			 String FRIENDNAME,
			 String USERFIRSTNAME,
			 String USERLASTNAME,
			 String FRIENDIMAGE,
			 String USERTOKEN,
			 String USERTOKENMOBILE,
			 String STATUS) {
		// TODO Auto-generated constructor stub
		this.FRIENDID = FRIENDID;
		this.FRIENDNAME = FRIENDNAME;
		this.USERFIRSTNAME = USERFIRSTNAME;
		this.USERLASTNAME = USERLASTNAME;
		this.FRIENDIMAGE = FRIENDIMAGE;
		this.USERTOKEN = USERTOKEN;
		this.USERTOKENMOBILE = USERTOKENMOBILE;
		this.STATUS = STATUS;
	}
	public String getFRIENDID() {
		return FRIENDID;
	}
	public void setFRIENDID(String fRIENDID) {
		FRIENDID = fRIENDID;
	}
	public String getFRIENDNAME() {
		return FRIENDNAME;
	}
	public void setFRIENDNAME(String fRIENDNAME) {
		FRIENDNAME = fRIENDNAME;
	}
	public String getUSERFIRSTNAME() {
		return USERFIRSTNAME;
	}
	public void setUSERFIRSTNAME(String uSERFIRSTNAME) {
		USERFIRSTNAME = uSERFIRSTNAME;
	}
	public String getUSERLASTNAME() {
		return USERLASTNAME;
	}
	public void setUSERLASTNAME(String uSERLASTNAME) {
		USERLASTNAME = uSERLASTNAME;
	}
	public String getFRIENDIMAGE() {
		return FRIENDIMAGE;
	}
	public void setFRIENDIMAGE(String fRIENDIMAGE) {
		FRIENDIMAGE = fRIENDIMAGE;
	}
	public String getUSERTOKEN() {
		return USERTOKEN;
	}
	public void setUSERTOKEN(String uSERTOKEN) {
		USERTOKEN = uSERTOKEN;
	}
	public String getUSERTOKENMOBILE() {
		return USERTOKENMOBILE;
	}
	public void setUSERTOKENMOBILE(String uSERTOKENMOBILE) {
		USERTOKENMOBILE = uSERTOKENMOBILE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	
}
