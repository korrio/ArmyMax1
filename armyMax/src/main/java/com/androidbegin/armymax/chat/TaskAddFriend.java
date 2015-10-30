package com.androidbegin.armymax.chat;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ame.armymax.pref.UserHelper;

public class TaskAddFriend extends AsyncTask<Void, Void, Void>{
	
	private boolean addSuccess = false;
	private DialogTask dialog;
	private Context context;
	private OnAddfriendListener callback;
	private String friendID;
	public TaskAddFriend(Context context ,String friendID, OnAddfriendListener callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new DialogTask(context);
		this.callback = callback;
		this.friendID = friendID;
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
		UserHelper user = new UserHelper(context);
		String url = String.format(Link.LINk_ADDFRIEND, user.getUserID() , friendID , "1");
		String resule = Utils.getHttpGet(url);
		try {
			JSONObject jObj = new JSONObject(resule);
			if (jObj.getString("Relation").equalsIgnoreCase("0")) {
				addSuccess = true;
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
		if (addSuccess) {
			callback.addFriendSuccess();
		}
		else {
			callback.addFriendFailed();
		}
		dialog.dismissDialog();
		super.onPostExecute(result);
	}

}
