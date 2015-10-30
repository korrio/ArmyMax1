package com.androidbegin.armymax.chat;

import java.io.Serializable;

public class ChatGroupModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 890701844746506217L;
	private String id;
	private String name;
	private String type;
	private String create_by;
	private String user_id_one;
	private String user_id_two;
	private String live_user_id;
	private String timestamp;
	private String status;
	private String active;
	private String password;
	private String invite_only_flag;
	private String stream;
	private String remark;
	private String delete_flag;
	public ChatGroupModel(
			String id,
			String name,
			String type,
			String create_by,
			String user_id_one,
			String user_id_two,
			String live_user_id,
			String timestamp,
			String status,
			String active,
			String password,
			String invite_only_flag,
			String stream,
			String remark,
			String delete_flag) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
		this.type = type;
		this.create_by = create_by;
		this.user_id_one = user_id_one;
		this.user_id_two = user_id_two;
		this.live_user_id = live_user_id;
		this.timestamp = timestamp;
		this.status = status;
		this.active = active;
		this.password = password;
		this.invite_only_flag = invite_only_flag;
		this.stream = stream;
		this.remark = remark;
		this.delete_flag = delete_flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public String getUser_id_one() {
		return user_id_one;
	}
	public void setUser_id_one(String user_id_one) {
		this.user_id_one = user_id_one;
	}
	public String getUser_id_two() {
		return user_id_two;
	}
	public void setUser_id_two(String user_id_two) {
		this.user_id_two = user_id_two;
	}
	public String getLive_user_id() {
		return live_user_id;
	}
	public void setLive_user_id(String live_user_id) {
		this.live_user_id = live_user_id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getInvite_only_flag() {
		return invite_only_flag;
	}
	public void setInvite_only_flag(String invite_only_flag) {
		this.invite_only_flag = invite_only_flag;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDelete_flag() {
		return delete_flag;
	}
	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}
	
}
