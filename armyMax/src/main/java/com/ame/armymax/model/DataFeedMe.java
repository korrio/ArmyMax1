package com.ame.armymax.model;

import java.util.ArrayList;
import java.util.List;

public class DataFeedMe {
	/*
	 * static final String BASE = "http://i.imgur.com/"; static final String EXT
	 * = ".jpg";
	 */
	public static final String BASE = "https://www.vdomax.com/photo/";
	static final String EXT = "";

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
	
	public DataFeedMe() {
		
	}
	
	public static ArrayList<String> getNames() {
		return names;
	}

	public static void clearAll() {
		tbUrls.clear();
		names.clear();
		agos.clear();
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
	

}
