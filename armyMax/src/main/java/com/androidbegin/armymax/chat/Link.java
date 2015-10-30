package com.androidbegin.armymax.chat;

import com.ame.armymax.model.DataUser;

public class Link {
	public static String LOGIN = "https://www.armymax.com/api/?action=signin";
	public static String FRIENDS = "http://www.armymax.com/api/?action=getPeople&token=%s&userID=%s&startPoint=0&sizePage=100";
	// S1 = token s2 = userid 
	
	public static String RECENT_CHATS = "http://www.armymax.com/api/chat.php?action=recentchat&USERID=%s";   
	//s1 userID
	
	
	public static String LINK_GROUPCHAT = "https://www.armymax.com/api/chat.php?action=listgroupchat";
	
	public static String LINK_CHATS = "https://www.armymax.com/mobile.php?token=%s&id=%s&fid=%s&type=user";
	// s0 =token  , s1 = my UserID   , s2 = friend UserId

	public static String LINK_CHAT_GROUP = "https://www.armymax.com/mobile.php?id=%s&token=%s&type=group&rid=%s";
	//s1 = userId , s2 token , s3 rid 
	
	public static String LINK_PHOTO = "https://www.armymax.com/photo/";
	
	public static String LINK_SEARCHFRIEND = "http://www.armymax.com/api/?action=search&txt=%s&startPoint=0&sizePage=50";   
	//s1 = text For Search

	public static String LINk_ADDFRIEND = "http://armymax.com/api/?action=addFriend&id=%s&fid=%s&flag=%s"; 
	// s1 = myUserId , s2 friendID ,  s3 flag 1 = addUser 0 = deleteuser

	public static String LINK_CREATE_GROUP = "https://www.armymax.com/api/chat.php?action=creategroupchat";
	//post room_name,create_by
	
	public static String LINK_FRIEND_CHATGROUP = "https://www.armymax.com/api/chat.php?action=roommember&conversation_id=%s&active=ALL";
	// s1 = conversationID 
	
	public static String getLinkChat(String fid) {
		return "https://www.armymax.com/mobile.php?token="+DataUser.VM_USER_TOKEN+"&id="+DataUser.VM_USER_ID+"&fid="+fid+"&type=user";
	}
	
	public static String getLinkGroupChat(String rid) {
		return "https://www.armymax.com/mobile.php?token="+DataUser.VM_USER_TOKEN+"&id="+DataUser.VM_USER_ID+"&rid="+rid+"&type=group";
	}
	
}
