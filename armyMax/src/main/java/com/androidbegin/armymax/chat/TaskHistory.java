package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ame.armymax.pref.UserHelper;

public class TaskHistory extends AsyncTask<Void, Void, Void> {
	private UserHelper user;
	private Context context;
	private DialogTask dialog;
	private OnTaskChatsListener callback;
	private boolean isSuccess = false;
	private ArrayList<ChatsModel> arrChats;

	public TaskHistory(Context context, OnTaskChatsListener callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new DialogTask(context);
		this.callback = callback;
		user = new UserHelper(context);
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

		// System.out.println("user : "+user.getUserID());
		// String String urlFriend =
		// String.format(Link.RECENT_CHATS,user.getUserID());
		String urlFriend = String.format(Link.RECENT_CHATS, user.getUserID());
		Utils.showLog("getUserID : ", "user.getUserID() : " + user.getUserID());
		String jsonResule = Utils.getHttpGet(urlFriend);
		System.out.println("urlFriend : " + urlFriend);
		System.out.println("jsonResule : " + jsonResule);
		arrChats = new ArrayList<ChatsModel>();
		try {
			JSONObject jObjMain = new JSONObject(jsonResule);
			JSONArray jArrData = jObjMain.getJSONArray("data");
			for (int i = 0; i < jArrData.length(); i++) {
				String TIMESTAMPMS = jArrData.getJSONObject(i).getString(
						"TIMESTAMPMS");
				String CID = jArrData.getJSONObject(i).getString("CID");

				// ******************************************************
				String TIMESTAMPMSRECENTCHAT = jArrData.getJSONObject(i)
						.getJSONObject("DATARECENTCHAT")
						.getString("TIMESTAMPMS");
				JSONObject jObjRECENTCHAT = jArrData.getJSONObject(i)
						.getJSONObject("DATARECENTCHAT")
						.getJSONObject("MYPROFILE");
				String USERID = jObjRECENTCHAT.getString("USERID");
				String USERNAME = jObjRECENTCHAT.getString("USERNAME");
				String USERFIRSTNAME = jObjRECENTCHAT
						.getString("USERFIRSTNAME");
				String USERLASTNAME = jObjRECENTCHAT.getString("USERLASTNAME");
				String USERIMAGE = jObjRECENTCHAT.getString("USERIMAGE");
				String USERTOKEN = jObjRECENTCHAT.getString("USERTOKEN");
				String USERTOKENMOBILE = jObjRECENTCHAT
						.getString("USERTOKENMOBILE");
				String STATUS = jObjRECENTCHAT.getString("STATUS");

				Chats_MyProfileModel profileModel = new Chats_MyProfileModel(
						USERID, USERNAME, USERFIRSTNAME, USERLASTNAME,
						USERIMAGE, USERTOKEN, USERTOKENMOBILE, STATUS);

				// **********************************************************************
				JSONObject jObjUSERFRIENDPROFILE = jArrData.getJSONObject(i)
						.getJSONObject("DATARECENTCHAT")
						.getJSONObject("USERFRIENDPROFILE");
				String FRIEND_FRIENDID = jObjUSERFRIENDPROFILE
						.getString("FRIENDID");
				String FRIEND_FRIENDNAME = jObjUSERFRIENDPROFILE
						.getString("FRIENDNAME");
				String FRIEND_USERFIRSTNAME = jObjUSERFRIENDPROFILE
						.getString("USERFIRSTNAME");
				String FRIEND_USERLASTNAME = jObjUSERFRIENDPROFILE
						.getString("USERLASTNAME");
				String FRIEND_FRIENDIMAGE = jObjUSERFRIENDPROFILE
						.getString("FRIENDIMAGE");
				String FRIEND_USERTOKEN = jObjUSERFRIENDPROFILE
						.getString("USERTOKEN");
				String FRIEND_USERTOKENMOBILE = jObjUSERFRIENDPROFILE
						.getString("USERTOKENMOBILE");
				String FRIEND_STATUS = jObjUSERFRIENDPROFILE
						.getString("STATUS");

				Chats_FriendProfileModel frindModel = new Chats_FriendProfileModel(
						FRIEND_FRIENDID, FRIEND_FRIENDNAME,
						FRIEND_USERFIRSTNAME, FRIEND_USERLASTNAME,
						FRIEND_FRIENDIMAGE, FRIEND_USERTOKEN,
						FRIEND_USERTOKENMOBILE, FRIEND_STATUS);

				// *****************************************************************
				JSONObject jObjLASTMESSAGE = jArrData.getJSONObject(i)
						.getJSONObject("DATARECENTCHAT")
						.getJSONObject("LASTMESSAGE");
				String LASTMESSAGE_RID = jObjLASTMESSAGE.getString("RID");
				String LASTMESSAGE_CONVERSATION_ID = jObjLASTMESSAGE
						.getString("CONVERSATION_ID");
				String LASTMESSAGE_SENDERID = jObjLASTMESSAGE
						.getString("SENDERID");
				String LASTMESSAGE_SENDERUSERNAME = jObjLASTMESSAGE
						.getString("SENDERUSERNAME");
				String LASTMESSAGE_SENDERIMAGE = jObjLASTMESSAGE
						.getString("SENDERIMAGE");
				String LASTMESSAGE_RECEIVERID = jObjLASTMESSAGE
						.getString("RECEIVERID");
				String LASTMESSAGE_MESSAGETYPE = jObjLASTMESSAGE
						.getString("MESSAGETYPE");
				String LASTMESSAGE_MESSAGECHAT = jObjLASTMESSAGE
						.getString("MESSAGECHAT");
				String LASTMESSAGE_TATTOOCODE = jObjLASTMESSAGE
						.getString("TATTOOCODE");
				String LASTMESSAGE_TATTOOURL = jObjLASTMESSAGE
						.getString("TATTOOURL");
				String LASTMESSAGE_IMAGEURL = jObjLASTMESSAGE
						.getString("IMAGEURL");
				String LASTMESSAGE_IMAGETYPE = jObjLASTMESSAGE
						.getString("IMAGETYPE");
				String LASTMESSAGE_IMAGENAME = jObjLASTMESSAGE
						.getString("IMAGENAME");
				String LASTMESSAGE_VIDEOURL = jObjLASTMESSAGE
						.getString("VIDEOURL");
				String LASTMESSAGE_VIDEOIMAGE = jObjLASTMESSAGE
						.getString("VIDEOIMAGE");
				String LASTMESSAGE_LOCATION_LATITUDE = jObjLASTMESSAGE
						.getString("LOCATION_LATITUDE");
				String LASTMESSAGE_LOCATION_LONGTITUDE = jObjLASTMESSAGE
						.getString("LOCATION_LONGTITUDE");
				String LASTMESSAGE_LOCATIONDETAIL = jObjLASTMESSAGE
						.getString("LOCATIONDETAIL");
				String LASTMESSAGE_CONTACTNAME = jObjLASTMESSAGE
						.getString("CONTACTNAME");
				String LASTMESSAGE_CONTACTDETAILS = jObjLASTMESSAGE
						.getString("CONTACTDETAILS");
				String LASTMESSAGE_IP = jObjLASTMESSAGE.getString("IP");
				String LASTMESSAGE_TIMESTAMP = jObjLASTMESSAGE
						.getString("TIMESTAMP");
				String LASTMESSAGE_TIMESTAMPMS = jObjLASTMESSAGE
						.getString("TIMESTAMPMS");
				String LASTMESSAGE_STATUS = jObjLASTMESSAGE.getString("STATUS");
				boolean readed;
				if (LASTMESSAGE_STATUS.equals("1")) {
					readed = false;
				} else {
					readed = true;
				}

				Chats_LastMassageModel lastMassageModel = new Chats_LastMassageModel(
						LASTMESSAGE_RID, LASTMESSAGE_CONVERSATION_ID,
						LASTMESSAGE_SENDERID, LASTMESSAGE_SENDERUSERNAME,
						LASTMESSAGE_SENDERIMAGE, LASTMESSAGE_RECEIVERID,
						LASTMESSAGE_MESSAGETYPE, LASTMESSAGE_MESSAGECHAT,
						LASTMESSAGE_TATTOOCODE, LASTMESSAGE_TATTOOURL,
						LASTMESSAGE_IMAGEURL, LASTMESSAGE_IMAGETYPE,
						LASTMESSAGE_IMAGENAME, LASTMESSAGE_VIDEOURL,
						LASTMESSAGE_VIDEOIMAGE, LASTMESSAGE_LOCATION_LATITUDE,
						LASTMESSAGE_LOCATION_LONGTITUDE,
						LASTMESSAGE_LOCATIONDETAIL, LASTMESSAGE_CONTACTNAME,
						LASTMESSAGE_CONTACTDETAILS, LASTMESSAGE_IP,
						LASTMESSAGE_TIMESTAMP, LASTMESSAGE_TIMESTAMPMS,
						LASTMESSAGE_STATUS);
				Chats_RecentModel recentModel = new Chats_RecentModel(
						TIMESTAMPMSRECENTCHAT, profileModel, frindModel,
						lastMassageModel, readed);
				arrChats.add(new ChatsModel(TIMESTAMPMS, CID, recentModel));
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
		// System.out.println();s
		if (isSuccess) {
			callback.loadDataChatsSuccess(arrChats);
		}
		dialog.dismissDialog();
		super.onPostExecute(result);
	}

}
