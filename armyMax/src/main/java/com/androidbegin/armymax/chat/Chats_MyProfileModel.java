package com.androidbegin.armymax.chat;

public class Chats_MyProfileModel {
	private String USERID;
	private String USERNAME;
	private String USERFIRSTNAME;
	private String USERLASTNAME;
	private String USERIMAGE;
	private String USERTOKEN;
	private String USERTOKENMOBILE;
	private String STATUS;
	public Chats_MyProfileModel(
			String USERID,
			String USERNAME,
			String USERFIRSTNAME,
			String USERLASTNAME,
			String USERIMAGE,
			String USERTOKEN,
			String USERTOKENMOBILE,
			String STATUS) {
		// TODO Auto-generated constructor stub
		this.USERID = USERID;
		this.USERNAME =USERNAME;
		this.USERFIRSTNAME = USERFIRSTNAME;
		this.USERLASTNAME = USERLASTNAME;
		this.USERIMAGE = USERIMAGE;
		this.USERTOKEN = USERTOKEN;
		this.USERTOKENMOBILE = USERTOKENMOBILE;
		this.STATUS = STATUS;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
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
	public String getUSERIMAGE() {
		return USERIMAGE;
	}
	public void setUSERIMAGE(String uSERIMAGE) {
		USERIMAGE = uSERIMAGE;
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
