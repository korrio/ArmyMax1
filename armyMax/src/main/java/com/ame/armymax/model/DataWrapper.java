package com.ame.armymax.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<DataFeedVideo> myVideoList;

	public DataWrapper(List<DataFeedVideo> myVideoList) {
		this.myVideoList = (ArrayList<DataFeedVideo>) myVideoList;
	}

	public ArrayList<DataFeedVideo> getDataFeedVideos() {
		return this.myVideoList;
	}

}
