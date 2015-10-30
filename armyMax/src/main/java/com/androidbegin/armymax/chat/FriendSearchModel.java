package com.androidbegin.armymax.chat;

public class FriendSearchModel {
	private String UserID;
	private String UserEmail;
	private String UserName;
	private String UserFirstName;
	private String UserLastName;
	private String UserAvatarPath;
	private String UserAvatarPathMedium;
	private String UserAvatarPathSmall;
	private String UserSex;
	private String UserCountry;
	private String UserPosts;
	private String UserFollowings;
	private String UserFollowers;
	private String Live;
	public FriendSearchModel(
			String UserID,
			String UserEmail,
			String UserName,
			String UserFirstName,
			String UserLastName,
			String UserAvatarPath,
			String UserAvatarPathMedium,
			String UserAvatarPathSmall,
			String UserSex,
			String UserCountry,
			String UserPosts,
			String UserFollowings,
			String UserFollowers,
			String Live) {
		// TODO Auto-generated constructor stub
		this.UserID = UserID; 
		this.UserEmail = UserEmail; 
		this.UserName = UserName; 
		this.UserFirstName = UserFirstName; 
		this.UserLastName = UserLastName; 
		this.UserAvatarPath = UserAvatarPath; 
		this.UserAvatarPathMedium = UserAvatarPathMedium; 
		this.UserAvatarPathSmall = UserAvatarPathSmall; 
		this.UserSex = UserSex; 
		this.UserCountry = UserCountry; 
		this.UserPosts = UserPosts; 
		this.UserFollowings = UserFollowings; 
		this.UserFollowers = UserFollowers; 
		this.Live = Live; 
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getUserEmail() {
		return UserEmail;
	}
	public void setUserEmail(String userEmail) {
		UserEmail = userEmail;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserFirstName() {
		return UserFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		UserFirstName = userFirstName;
	}
	public String getUserLastName() {
		return UserLastName;
	}
	public void setUserLastName(String userLastName) {
		UserLastName = userLastName;
	}
	public String getUserAvatarPath() {
		return UserAvatarPath;
	}
	public void setUserAvatarPath(String userAvatarPath) {
		UserAvatarPath = userAvatarPath;
	}
	public String getUserAvatarPathMedium() {
		return UserAvatarPathMedium;
	}
	public void setUserAvatarPathMedium(String userAvatarPathMedium) {
		UserAvatarPathMedium = userAvatarPathMedium;
	}
	public String getUserAvatarPathSmall() {
		return UserAvatarPathSmall;
	}
	public void setUserAvatarPathSmall(String userAvatarPathSmall) {
		UserAvatarPathSmall = userAvatarPathSmall;
	}
	public String getUserSex() {
		return UserSex;
	}
	public void setUserSex(String userSex) {
		UserSex = userSex;
	}
	public String getUserCountry() {
		return UserCountry;
	}
	public void setUserCountry(String userCountry) {
		UserCountry = userCountry;
	}
	public String getUserPosts() {
		return UserPosts;
	}
	public void setUserPosts(String userPosts) {
		UserPosts = userPosts;
	}
	public String getUserFollowings() {
		return UserFollowings;
	}
	public void setUserFollowings(String userFollowings) {
		UserFollowings = userFollowings;
	}
	public String getUserFollowers() {
		return UserFollowers;
	}
	public void setUserFollowers(String userFollowers) {
		UserFollowers = userFollowers;
	}
	public String getLive() {
		return Live;
	}
	public void setLive(String live) {
		Live = live;
	}
	
}
