package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ame.armymax.pref.UserHelper;

public class TaskSearchFriend extends AsyncTask<Void, Void, Void>{
	private UserHelper user;
	private Context context;
	private DialogTask dialog;
	private OnSearchFriendListener callback;
	private boolean isSuccess = false;
	private ArrayList<FriendSearchModel> arrSearchFriend;
	private String textForSearch;
	public TaskSearchFriend(Context context ,String textForSearch, OnSearchFriendListener callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new DialogTask(context);
		this.callback = callback;
		this.textForSearch = textForSearch;
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
		String urlFriend = String.format(Link.LINK_SEARCHFRIEND, this.textForSearch);//String.format(Link.LINK_SEARCHFRIEND,user.getToken(),user.getToken());
		Utils.showLog("urlFriend ", urlFriend);
		String jsonResule = Utils.getHttpGet(urlFriend);
		arrSearchFriend = new ArrayList<FriendSearchModel>();
		try {
			JSONObject jObjMain = new JSONObject(jsonResule);
			
			if (jObjMain.getString("msg").equalsIgnoreCase("Success")) {
				JSONArray jArrData = jObjMain.getJSONObject("people").getJSONArray("data");
				//JSONArray jArrContent = jObjMain.getJSONArray("Content");
				for (int i = 0; i < jArrData.length(); i++) {
					
					JSONObject jObjData = jArrData.getJSONObject(i);
					arrSearchFriend.add(new FriendSearchModel(
							jObjData.getString("UserID"),
							jObjData.getString("UserEmail"), 
							jObjData.getString("UserName"), 
							jObjData.getString("UserFirstName"),
							jObjData.getString("UserLastName"),
							jObjData.getString("UserAvatarPath"),
							jObjData.getString("UserAvatarPathMedium"),
							jObjData.getString("UserAvatarPathSmall"), 
							jObjData.getString("UserSex"),
							jObjData.getString("UserCountry"),
							jObjData.getString("UserPosts"),
							jObjData.getString("UserFollowings"),
							jObjData.getString("UserFollowers"),
							jObjData.getString("Live")));
//					arrFriends.add(new FriendContentModel(
//							jObjData.getString("UserID"),
//							jObjData.getString("UserName"),
//							jObjData.getString("UserFirstName"),
//							jObjData.getString("UserLastName"),
//							jObjData.getString("UserAvatarPathMedium"),
//							jObjData.getString("UserAvatarPath"),
//							jObjData.getString("UserSex"),
//							jObjData.getString("UserCountry"),
//							jObjData.getString("UserPosts"),
//							jObjData.getString("UserFollowings"),
//							jObjData.getString("UserFollowers"),
//							jObjData.getString("Live"),
//							new FriendContent_follow_Model(
//									jObjFrien.getJSONObject("Follow").getString("status"),
//									jObjFrien.getJSONObject("Follow").getString("msg"),
//									jObjFrien.getJSONObject("Follow").getString("Following_command"))));
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
			callback.SearchFriendSuccess(arrSearchFriend);
		}
		dialog.dismissDialog();
		super.onPostExecute(result);
	}

}
