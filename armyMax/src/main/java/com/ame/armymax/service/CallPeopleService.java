package com.ame.armymax.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.ame.armymax.R;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFollower;
import com.ame.armymax.model.DataFollowing;
import com.ame.armymax.model.DataFriend;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

public class CallPeopleService extends AsyncTask<Context, Integer, String> {
	String s = "";
	AQuery aq;
	View view;

	public CallPeopleService(View view) {
		this.view = view;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	protected String doInBackground(Context... params) {
		aq = new AQuery(view);
		ajaxPeople();
		ajaxFriend();
		ajaxFollower();
		ajaxFollowing();
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private void ajaxFollower() {
		String urlFollower = "https://api.vdomax.com/service/getFollow/mobile/?token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="+DataUser.VM_USER_ID+"&type=2&startPoint=0&sizePage=100";
		aq.progress(R.id.progress).ajax(urlFollower, JSONObject.class, this,
				"followerCb");
	}

	private void ajaxFollowing() {
		String urlFollowing = "https://api.vdomax.com/service/getFollow/mobile/?token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="+DataUser.VM_USER_ID+"&type=1&startPoint=0&sizePage=100";
		aq.progress(R.id.progress).ajax(urlFollowing, JSONObject.class, this,
				"followingCb");
	}

	private void ajaxFriend() {
		String urlFriend = "https://api.vdomax.com/service/getFriends/mobile/?token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="+DataUser.VM_USER_ID+"&startPoint=0&sizePage=100";
		aq.progress(R.id.progress).ajax(urlFriend, JSONObject.class, this,
				"friendCb");
	}

	private void ajaxPeople() {
		String urlPeople = "https://api.vdomax.com/service/getPeople/mobile/?token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="+DataUser.VM_USER_ID+"&startPoint=0&sizePage=100";
		aq.progress(R.id.progress).ajax(urlPeople, JSONObject.class, this,
				"peopleCb");
	}

	public void peopleCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		AQUtility.debug("jo", jo);

		if (jo != null) {
			
			JSONArray ja = jo.getJSONArray("Content");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject user = ja.getJSONObject(i);
				String name = user.getString("UserFirstName") + " "
						+ user.getString("UserLastName");
				String avatar = user.getString("UserAvatarPath");
				
				
				/*
				DataPeople.setId(user.getString("UserID"));
				DataPeople.setUsername(user.getString("UserName"));
				DataPeople.setName(name);
				Log.d("asdf", name);
				if (avatar.toLowerCase().contains("facebook"))
					DataPeople.setUrl(avatar);
				else
					DataPeople.setUrl(Data.BASE + avatar);
					*/
			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void friendCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		AQUtility.debug("jo", jo);

		if (jo != null) {
			DataFriend.clearAll();
			JSONArray ja = jo.getJSONArray("Content");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject user = ja.getJSONObject(i);
				String name = user.getString("UserFirstName") + " "
						+ user.getString("UserLastName");
				String avatar = user.getString("UserAvatarPath");
				DataFriend.setId(user.getString("UserID"));
				DataFriend.setUsername(user.getString("UserName"));
				DataFriend.setName(name);
				Log.d("asdf", name);
				if (avatar.toLowerCase().contains("facebook"))
					DataFriend.setUrl(avatar);
				else
					DataFriend.setUrl(Data.BASE + avatar);
			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void followerCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		AQUtility.debug("jo", jo);

		if (jo != null) {
			DataFollower.clearAll();
			JSONArray ja = jo.getJSONArray("Content");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject user = ja.getJSONObject(i);
				String name = user.getString("UserFirstName") + " "
						+ user.getString("UserLastName");
				String avatar = user.getString("UserAvatarPath");
				DataFollower.setId(user.getString("UserID"));
				DataFollower.setUsername(user.getString("UserName"));
				DataFollower.setName(name);
				Log.d("asdf", name);
				if (avatar.toLowerCase().contains("facebook"))
					DataFollower.setUrl(avatar);
				else
					DataFollower.setUrl(Data.BASE + avatar);
			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void followingCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		AQUtility.debug("jo", jo);

		if (jo != null) {
			DataFollowing.clearAll();
			JSONArray ja = jo.getJSONArray("Content");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject user = ja.getJSONObject(i);
				String name = user.getString("UserFirstName") + " "
						+ user.getString("UserLastName");
				String avatar = user.getString("UserAvatarPath");
				DataFollowing.setId(user.getString("UserID"));
				DataFollowing.setUsername(user.getString("UserName"));
				DataFollowing.setName(name);
				Log.d("asdf", name);
				if (avatar.toLowerCase().contains("facebook"))
					DataFollowing.setUrl(avatar);
				else
					DataFollowing.setUrl(Data.BASE + avatar);
			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}
}