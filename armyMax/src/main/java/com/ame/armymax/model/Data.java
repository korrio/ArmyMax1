package com.ame.armymax.model;

import java.util.ArrayList;

public class Data {

	public static final String BASE = "https://www.armymax.com/photo/";
	public static final String LIVE = "150.107.31.13";
	public static final String VOD = "150.107.31.7";
	
	private static ArrayList<String> URLS = new ArrayList<String>();
	private static ArrayList<String> NAMES = new ArrayList<String>();

	public Data() {

	}
	
	public static String getUrl(int i) {
		return URLS.get(i);
	}

	public static void setUrl(String name) {
		Data.URLS.add(name.toString());
	}

	public static String getName(int i) {
		return NAMES.get(i);
	}

	public static void setName(String name) {
		Data.NAMES.add(name.toString());
	}

	public static int getURLSize() {
		return Data.URLS.size();
	}

	public static int getNAMESize() {
		return Data.NAMES.size();
	}

	public static ArrayList<String> getNAMES() {
		return Data.NAMES;
	}

	public static ArrayList<String> getURLS() {
		return Data.URLS;
	}
	
	public static void clearAll() {
		Data.URLS.clear();
		Data.NAMES.clear();
	}
}
