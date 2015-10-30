package com.ame.armymax.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataFeedEverything implements Parcelable {

	/*
	private static ArrayList<String> postIds = new ArrayList<String>();
	private static ArrayList<String> postTypes = new ArrayList<String>();
	private static ArrayList<String> tbUrls = new ArrayList<String>();
	private static ArrayList<String> names = new ArrayList<String>();
	private static ArrayList<String> agos = new ArrayList<String>();
	private static ArrayList<String> statuss = new ArrayList<String>();
	private static ArrayList<String> contentTbUrls = new ArrayList<String>();
	private static ArrayList<String> contentNames = new ArrayList<String>();
	private static ArrayList<String> contentDescs = new ArrayList<String>();
	private static ArrayList<String> contentMetas = new ArrayList<String>();
	private static ArrayList<String> loveCounts = new ArrayList<String>();
	private static ArrayList<String> commentCounts = new ArrayList<String>();
	private static ArrayList<String> videoSources = new ArrayList<String>();
	*/
	
	private String postId;
	private String postType;
	private String tbUrl;
	private String name;
	private String ago;
	private String status;
	private String contentTbUrl;
	private String contentName;
	private String contentDesc;
	private String contentMeta;
	private String loveCount;
	private String commentCount;
	private String videoSource;
	private String loveUrl;
	private String loveStatus;
	private String userId;
	
	public DataFeedEverything(Parcel in) {
		this.postId = in.readString();
		this.postType = in.readString();
		this.tbUrl = in.readString();
		this.name = in.readString();
		this.ago = in.readString();
		this.status = in.readString();
		this.contentTbUrl = in.readString();
		this.contentName = in.readString();
		this.contentDesc = in.readString();
		this.contentMeta = in.readString();
		this.loveCount = in.readString();
		this.commentCount = in.readString();
		this.videoSource = in.readString();
		this.loveUrl = in.readString();
		this.loveStatus = in.readString();
		this.userId = in.readString();
	}

	public DataFeedEverything(String postId, String postType, String tbUrl,
			String name, String ago, String status, String contentTbUrl,
			String contentName, String contentDesc, String contentMeta,
			String loveCount, String commentCount, String videoSource,String loveUrl,String loveStatus, String userId) {
		
		this.postId = postId;
		this.postType = postType;
		this.tbUrl = tbUrl;
		this.name = name;
		this.ago = ago;
		this.status = status;
		this.contentTbUrl = contentTbUrl;
		this.contentName = contentName;
		this.contentDesc = contentDesc;
		this.contentMeta = contentMeta;
		this.loveCount = loveCount;
		this.commentCount = commentCount;
		this.videoSource = videoSource;
		this.loveUrl = loveUrl;
		this.loveStatus = loveStatus;
		this.userId = userId;

	}
	
	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String name) {
		this.postId = name;
	}
	
	public String getPostType() {
		return this.postType;
	}

	public void setPostType(String name) {
		this.postType = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTbUrl() {
		return this.tbUrl;
	}

	public void setTbUrl(String name) {
		this.tbUrl = name;
	}

	public String getAgo() {
		return this.ago;
	}

	public void setAgo(String name) {
		this.ago = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String name) {
		this.status = name;
	}

	public String getContentTbUrl() {
		return this.contentTbUrl;
	}

	public void setContentTbUrl(String name) {
		this.contentTbUrl = name;
	}

	public String getContentName() {
		return this.contentName;
	}

	public void setContentName(String name) {
		this.contentName = name;
	}

	public String getContentDesc() {
		return this.contentDesc;
	}

	public void setContentDesc(String name) {
		this.contentDesc = name;
	}

	public String getContentMeta() {
		return this.contentMeta;
	}

	public void setContentMeta(String name) {
		this.contentMeta = name;
	}

	public String getLoveCount() {
		return this.loveCount;
	}

	public void setLoveCount(String name) {
		this.loveCount = name;
	}

	public String getCommentCount() {
		return this.commentCount;
	}

	public void setCommentCount(String name) {
		this.commentCount = name;
	}

	public String getVideoSource() {
		return this.videoSource;
	}

	public void setVideoSource(String name) {
		this.videoSource = name;
	}
	
	public String getLoveUrl() {
		return this.loveUrl;
	}

	public void setLoveUrl(String name) {
		this.loveUrl = name;
	}
	
	public String getLoveStatus() {
		return this.loveStatus;
	}

	public void setLoveStatus(String name) {
		this.loveStatus = name;
	}
	
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String name) {
		this.userId = name;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(postId);
		dest.writeString(postType);
		dest.writeString(tbUrl);
		dest.writeString(name);
		dest.writeString(ago);
		dest.writeString(status);
		dest.writeString(contentTbUrl);
		dest.writeString(contentName);
		dest.writeString(contentDesc);
		dest.writeString(contentMeta);
		dest.writeString(loveCount);
		dest.writeString(commentCount);
		dest.writeString(videoSource);
		dest.writeString(loveUrl);
		dest.writeString(loveStatus);
		dest.writeString(userId);
	}

	public static final Parcelable.Creator<DataFeedEverything> CREATOR = new Parcelable.Creator<DataFeedEverything>() {

		public DataFeedEverything createFromParcel(Parcel in) {
			return new DataFeedEverything(in);
		}

		public DataFeedEverything[] newArray(int size) {
			return new DataFeedEverything[size];
		}
	};

	@Override
	public int describeContents() {
		return this.hashCode();
	}
	
	/*

	public static String getPostId(int i) {
		return postIds.get(i);
	}

	public static void setPostId(String name) {
		postIds.add(name.toString());
	}

	public static String getPostType(int i) {
		return postTypes.get(i);
	}

	public static void setPostType(String name) {
		postTypes.add(name.toString());
	}

	public static String getName(int i) {
		return names.get(i);
	}

	public static void setName(String name) {
		names.add(name.toString());
	}

	public static String getTbUrl(int i) {
		return tbUrls.get(i);
	}

	public static void setTbUrl(String name) {
		tbUrls.add(name.toString());
	}

	public static String getAgo(int i) {
		return agos.get(i);
	}

	public static void setAgo(String name) {
		agos.add(name.toString());
	}

	public static String getStatus(int i) {
		return statuss.get(i);
	}

	public static void setStatus(String name) {
		statuss.add(name.toString());
	}

	public static String getContentTbUrl(int i) {
		return contentTbUrls.get(i);
	}

	public static void setContentTbUrl(String name) {
		contentTbUrls.add(name.toString());
	}

	public static String getContentName(int i) {
		return contentNames.get(i);
	}

	public static void setContentName(String name) {
		contentNames.add(name.toString());
	}

	public static String getContentDesc(int i) {
		return contentDescs.get(i);
	}

	public static void setContentDesc(String name) {
		contentDescs.add(name.toString());
	}

	public static String getContentMeta(int i) {
		return contentMetas.get(i);
	}

	public static void setContentMeta(String name) {
		contentMetas.add(name.toString());
	}

	public static String getLoveCount(int i) {
		return loveCounts.get(i);
	}

	public static void setLoveCount(String name) {
		loveCounts.add(name.toString());
	}

	public static String getCommentCount(int i) {
		return commentCounts.get(i);
	}

	public static void setCommentCount(String name) {
		commentCounts.add(name.toString());
	}

	public static String getVideoSource(int i) {
		return videoSources.get(i);
	}

	public static void setVideoSource(String name) {
		videoSources.add(name.toString());
	}
	*/

}
