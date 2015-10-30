package com.ame.armymax.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataFeedVideo implements Parcelable {

	private String postId;
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

	public DataFeedVideo(Parcel in) {
		this.postId = in.readString();
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
	}

	public DataFeedVideo(String postId, String tbUrl, String name, String ago, String status,
			String contentTbUrl, String contentName, String contentDesc,
			String contentMeta, String loveCount, String commentCount,
			String videoSource) {
		this.postId = postId;
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
	}

	public String getPostId() {
		return this.postId;
	}

	public void setPostId(String name) {
		this.postId = name;
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

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(postId);
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

	}

	public static final Parcelable.Creator<DataFeedVideo> CREATOR = new Parcelable.Creator<DataFeedVideo>() {

		public DataFeedVideo createFromParcel(Parcel in) {
			return new DataFeedVideo(in);
		}

		public DataFeedVideo[] newArray(int size) {
			return new DataFeedVideo[size];
		}
	};

}
