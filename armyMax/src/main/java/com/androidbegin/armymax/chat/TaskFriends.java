package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ame.armymax.model.DataUser;
import com.ame.armymax.pref.UserHelper;

public class TaskFriends extends AsyncTask<Void, Void, Void>{
	private UserHelper user;
	private Context context;
	private DialogTask dialog;
	private OnTaskFriendListener callback;
	private boolean isSuccess = false;
	private ArrayList<FriendContentModel> arrFriends;
	private ArrayList<ChatGroupModel> arrChatGroup;
	public TaskFriends(Context context , OnTaskFriendListener callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new DialogTask(context);
		this.callback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		dialog.showDialog();
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		user = new UserHelper(context);
		String urlFriend = String.format(Link.FRIENDS,user.getToken(),user.getUserID());
		
		String jsonResule = Utils.getHttpGet(urlFriend);
		//System.out.println("jsonResule : "+jsonResule);
		arrFriends = new ArrayList<FriendContentModel>();
		try {
			JSONObject jObjMain = new JSONObject(jsonResule);
			if (jObjMain.optString("msg").equalsIgnoreCase("Load people success.")) {
				JSONArray jArrContent = jObjMain.getJSONArray("Content");
				for (int i = 0; i < jArrContent.length(); i++) {
					JSONObject jObjFrien = jArrContent.getJSONObject(i);
					arrFriends.add(new FriendContentModel(
							jObjFrien.optString("UserID"),
							jObjFrien.optString("UserName"),
							jObjFrien.optString("UserFirstName"),
							jObjFrien.optString("UserLastName"),
							jObjFrien.optString("UserAvatarPathMedium"),
							jObjFrien.optString("UserAvatarPath"),
							jObjFrien.optString("UserSex"),
							jObjFrien.optString("UserCountry"),
							jObjFrien.optString("UserPosts"),
							jObjFrien.optString("UserFollowings"),
							jObjFrien.optString("UserFollowers"),
							jObjFrien.optString("Live"),
							new FriendContent_follow_Model(
									//jObjFrien.getJSONObject("Follow").optString("status"),
									jObjFrien.getJSONObject("Follow").optString("Relation"),
									jObjFrien.getJSONObject("Follow").optString("Following_command"))));
				}
				arrChatGroup = new ArrayList<ChatGroupModel>();
				String resultGroupChat = Utils.getHttpGet(Link.LINK_GROUPCHAT + "&user_id=" + DataUser.VM_USER_ID);
				JSONArray jArrGropChat = new JSONArray(resultGroupChat);
				for (int i = 0; i < jArrGropChat.length(); i++) {
				 	JSONObject jObjGroupChat = jArrGropChat.getJSONObject(i);
				 	arrChatGroup.add(new ChatGroupModel(
				 			jObjGroupChat.optString("conversation_id"),
				 			jObjGroupChat.optString("name"),
				 			jObjGroupChat.optString("type"),
				 			jObjGroupChat.optString("create_by"),
				 			jObjGroupChat.optString("user_id_one"),
				 			jObjGroupChat.optString("user_id_two"),
				 			jObjGroupChat.optString("live_user_id"),
				 			jObjGroupChat.optString("timestamp"),
				 			jObjGroupChat.optString("status"),
				 			jObjGroupChat.optString("active"),
				 			jObjGroupChat.optString("password"),
				 			jObjGroupChat.optString("invite_only_flag"),
				 			jObjGroupChat.optString("stream"),
				 			jObjGroupChat.optString("remark"),
				 			jObjGroupChat.optString("delete_flag")));
				 	
				}
				isSuccess = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		//System.out.println();
		if (isSuccess) {
			callback.loadTaskFriendSuccess(arrFriends , arrChatGroup);
		}
		dialog.dismissDialog();
		super.onPostExecute(result);
	}

}
