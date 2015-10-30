package com.androidbegin.armymax.chat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;

import android.content.Context;
import android.os.AsyncTask;

public class TaskCreateGroup extends AsyncTask<Void, Void, Void> {
	private String room_name;
	private String create_by;
	private OnCreateGroup callback;
	private boolean isCreateSuccess = false;
	private String conversation_id;
	private Context context;

	public TaskCreateGroup(String room_name, String create_by, Context context,
			OnCreateGroup onCreateGroup) {
		// TODO Auto-generated constructor stub
		this.room_name = room_name;
		this.create_by = create_by;
		this.callback = onCreateGroup;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		List<NameValuePair> paramsCreateRoom = new ArrayList<NameValuePair>();
		System.out.println("room_name : " + room_name);
		System.out.println("create_by : " + create_by);
		paramsCreateRoom.add(new BasicNameValuePair("room_name", room_name));
		paramsCreateRoom.add(new BasicNameValuePair("create_by", create_by));
		String result = "";
		//if (room_name != null) {
			result = Utils
					.getHttpPost(Link.LINK_CREATE_GROUP, paramsCreateRoom);

			try {
				JSONObject json = new JSONObject(result);
				if (json.has("conversation_id")) { // Chaeck is Paramiter has
													// Creat Success
					conversation_id = json.getString("conversation_id");
					isCreateSuccess = true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//} 
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		if (isCreateSuccess) {
			callback.CreateGroupSuccess(conversation_id);
		} else {
			callback.CreateGroupFailed();
		}
		super.onPostExecute(result);
	}

}
