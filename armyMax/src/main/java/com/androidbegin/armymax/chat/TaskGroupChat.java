package com.androidbegin.armymax.chat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.graphics.Paint.Join;
import android.os.AsyncTask;

public class TaskGroupChat extends AsyncTask<Void, Void, Void>{
	private ArrayList<ChatGroupListModel> arrData;
	private boolean isSuccess = false;
	private OnLoadFriendGroupSuccess callback;
	private String conversation_id;
	public TaskGroupChat(Context context ,String conversation_id, OnLoadFriendGroupSuccess callback) {
		// TODO Auto-generated constructor stub
		this.callback = callback;
		this.conversation_id = conversation_id;
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		//List<NameValuePair> params1 = new ArrayList<NameValuePair>();
		String url = String.format(Link.LINK_FRIEND_CHATGROUP, conversation_id);
		String result = Utils.getHttpGet(url);
		System.out.println("result : "+result);
		arrData = new ArrayList<ChatGroupListModel>();
		try {
			JSONArray jArr = new JSONArray(result);
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject jObj = jArr.getJSONObject(i);
				arrData.add(new ChatGroupListModel(
						jObj.getString("user_id"),
								jObj.getString("username"),
										jObj.getString("user_avatar"),
												jObj.getString("name"),
														jObj.getString("conversation_id")));
			}
			isSuccess = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			isSuccess = false;
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			callback.loadSucces(arrData);
		}
		else {
			callback.loadFaild();
		}
		super.onPostExecute(result);
	}

}
