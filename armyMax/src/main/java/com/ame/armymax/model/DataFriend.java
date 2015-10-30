package com.ame.armymax.model;

import java.util.ArrayList;
import java.util.List;

public class DataFriend {
	/*
	 * static final String BASE = "http://i.imgur.com/"; static final String EXT
	 * = ".jpg";
	 */
	static final String BASE = "https://www.vdomax.com/photo/";
	static final String EXT = "";

	
	private static ArrayList<String> URLS = new ArrayList<String>();
	private static ArrayList<String> NAMES = new ArrayList<String>();
	private static ArrayList<String> USERNAMES = new ArrayList<String>();
	private static ArrayList<String> IDS = new ArrayList<String>();

	public DataFriend() {

	}
	
	public static void setUsername(String name) {
		USERNAMES.add(name.toString());
	}
	
	public static String getUsername(int i) {
		return USERNAMES.get(i);
	}
	
	public static String getId(int i) {
		return IDS.get(i);
	}

	public static void setId(String name) {
		IDS.add(name.toString());
	}
	
	public static String getUrl(int i) {
		return URLS.get(i);
	}

	public static void setUrl(String name) {
		DataFriend.URLS.add(name.toString());
	}

	public static String getName(int i) {
		return NAMES.get(i);
	}

	public static void setName(String name) {
		DataFriend.NAMES.add(name.toString());
	}

	public static int getURLSize() {
		return DataFriend.URLS.size();
	}

	public static int getNAMESize() {
		return DataFriend.NAMES.size();
	}

	public static ArrayList<String> getNAMES() {
		return DataFriend.NAMES;
	}

	public static ArrayList<String> getURLS() {
		return DataFriend.URLS;
	}
	
	public static void clearAll() {
		DataFriend.URLS.clear();
		DataFriend.NAMES.clear();
	}
}
