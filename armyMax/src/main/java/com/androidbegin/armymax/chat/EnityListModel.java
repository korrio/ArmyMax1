package com.androidbegin.armymax.chat;

public class EnityListModel implements FrienListModel{
	private FriendContentModel friendContent;
	private ChatGroupModel chatGroup;
	private boolean isChatgroup = true ;
	public EnityListModel(FriendContentModel friendContent ,ChatGroupModel chatGroup, boolean isChatgroup) {
		// TODO Auto-generated constructor stub
		this.friendContent = friendContent;
		this.isChatgroup = isChatgroup;
		this.chatGroup = chatGroup;
	}
	public FriendContentModel getFriendContent() {
		return friendContent;
	}
	public void setFriendContent(FriendContentModel friendContent) {
		this.friendContent = friendContent;
	}
	public ChatGroupModel getChatGroup() {
		return chatGroup;
	}
	public void setChatGroup(ChatGroupModel chatGroup) {
		this.chatGroup = chatGroup;
	}
	public boolean isChatgroup() {
		return isChatgroup;
	}
	public void setChatgroup(boolean isChatgroup) {
		this.isChatgroup = isChatgroup;
	}
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
