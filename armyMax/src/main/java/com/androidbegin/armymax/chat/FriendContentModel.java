package com.androidbegin.armymax.chat;

public class FriendContentModel {
	private String UserID;
	private String UserName;
	private String UserFirstName;
	private String UserLastName;
	private String UserAvatarPathMedium;
	private String UserAvatarPath;
	private String UserSex;
	private String UserCountry;
	private String UserPosts;
	private String UserFollowings;
	private String UserFollowers;
	private String Live;
	private FriendContent_follow_Model Follow;
	public FriendContentModel(
							
							String UserID,
							String UserName,
							String UserFirstName,
							String UserLastName,
							String UserAvatarPathMedium,
							String UserAvatarPath,
							String UserSex,
							String UserCountry,
							String UserPosts,
							String UserFollowings,
							String UserFollowers,
							String Live,
							FriendContent_follow_Model Follow) {
		// TODO Auto-generated constructor stub
		this.UserID = UserID;
		this.UserName = UserName;
		this.UserFirstName = UserFirstName;
		this.UserLastName = UserLastName;
		this.UserAvatarPathMedium = UserAvatarPathMedium;
		this.UserAvatarPath = UserAvatarPath;
		this.UserSex = UserSex;
		this.UserCountry = UserCountry;
		this.UserPosts = UserPosts;
		this.UserFollowings = UserFollowings;
		this.UserFollowers = UserFollowers;
		this.Live = Live;
		this.UserFollowers = UserFollowers;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
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
	public String getUserAvatarPathMedium() {
		return UserAvatarPathMedium;
	}
	public void setUserAvatarPathMedium(String userAvatarPathMedium) {
		UserAvatarPathMedium = userAvatarPathMedium;
	}
	public String getUserAvatarPath() {
		return UserAvatarPath;
	}
	public void setUserAvatarPath(String userAvatarPath) {
		UserAvatarPath = userAvatarPath;
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
	public FriendContent_follow_Model getFollow() {
		return Follow;
	}
	public void setFollow(FriendContent_follow_Model follow) {
		Follow = follow;
	}
	
}
