/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ame.armymax.model;

public class DataSearchResult {
	
	private String name;
	private Integer userid;
	private String userName;
	private String imgUrl;
	private int idImg;
	
	public DataSearchResult(String name, Integer userid) {
		this.userid = userid;
		this.name = name;
		
	}
	
	public DataSearchResult(Integer userid, String name, String userName, String imgUrl) {
		this.userid = userid;
		this.name = name;
		this.userName = userName;
		this.imgUrl = imgUrl;
	
	}

	public DataSearchResult(Integer userid, String name, String userName, int idImg) {
		this.userid = userid;
		this.name = name;
		this.userName = userName;
		this.idImg = idImg;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String name) {
		this.userName = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setimgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getUserId() {
		return userid;
	}
	public void setUserId(Integer userid) {
		this.userid = userid;
	}
	public int getIdImg() {
		return idImg;
	}
	public void setIdImg(int idImg) {
		this.idImg = idImg;
	}
	
	
}
