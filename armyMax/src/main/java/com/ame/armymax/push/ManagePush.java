package com.ame.armymax.push;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ame.armymax.ConferenceActivity;
import com.ame.armymax.R;
import com.ame.armymax.RouteActivity;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.service.DialogService;
import com.ame.armymax.xwalk.XWalkActivity;
import com.ame.armymax.xwalk.XWalkChatRoomActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class ManagePush extends Activity {

	private String TAG = "MANAGE PUSH";
	// public static $hostPath = "https://www.vdomax.com/noti/";
	// message category
	public static int CATEGORYS_common = 0;
	public static int CATEGORYS_chat = 1;
	// message type
	public static int TYPES_likeFeed = 100;
	public static int TYPES_commentFeed = 101;
	public static int TYPES_liveNow = 200;
	public static int TYPES_followedYou = 300;
	public static int TYPES_chatMessage = 500;
	public static int TYPES_chatSticker = 501;
	public static int TYPES_chatFile = 502;
	public static int TYPES_chatLocation = 503;
	public static int TYPES_chatFreeCall = 504;
	public static int TYPES_chatVideoCall = 505;
	public static int TYPES_chatInviteGroup = 506;
	public static int TYPES_confInvite = 600;
	public static int TYPES_confCreate = 601;
	public static int TYPES_confJoin = 602;

	// message format
	// public static $NOTIFORMATS = array(
	// "100" => "{0} like your post",
	// "101" => "{0} comment on {1} post {2}",
	// "200" => "{0} live now",
	// "300" => "{0} has followed you",
	// "500" => "{0}: {1}",
	// "501" => "{0} sent you a sticker",
	// "502" => "{0} sent you a file ",
	// );

	private String fromId;
	private String fromName;
	private String roomName;
	private String postId;
	private String freeCallUrl = "http://www.armymax.com/RTCMultiConnection/demos/phone/audio.html?&userid=";
	private String videoCallUrl = "http://150.107.31.11/r/";
	private String groupChatUrl = "https://www.armymax.com/mobile.php?id="
			+ DataUser.VM_USER_ID + "&token=" + DataUser.VM_USER_TOKEN
			+ "&type=group&rid=";
	private String chatUrl = "https://www.armymax.com/mobile.php?id="
			+ DataUser.VM_USER_ID + "&token=" + DataUser.VM_USER_TOKEN
			+ "&type=user&fid=";
	AQuery aq;
	String currentActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		this.startService(new Intent(this, ManagePush.class));
		aq = new AQuery(this);
		currentActivity = DataUser.context.getClass()
				.getSimpleName();
		getPush();
		
	}

	boolean mIsBound = false;
	private DialogService mBoundService;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mBoundService = ((DialogService.LocalBinder) service).getService();
			mBoundService.createDialogIn(2000);
			doUnbindService();
		}

		public void onServiceDisconnected(ComponentName className) {
			mBoundService = null;
		}
	};

	void doBindService() {
		if (!mIsBound) {
			bindService(new Intent(ManagePush.this, DialogService.class),
					mConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}

	void doUnbindService() {
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindService();
	}

	void getPush() {

		//Intent intent = getIntent();
		try {
			// String action = intent.getAction();
			// String channel =
			// intent.getExtras().getString("com.parse.Channel");
			// JSONObject json = new
			// JSONObject(intent.getExtras().getString("com.parse.Data"));

			Intent intent2 = getIntent();
			Bundle extras = intent2.getExtras();
			String jsonData = extras.getString("com.parse.Data");
			System.out.println("JSON DATA : " + jsonData);
            Log.e("HEYHEYHEY",jsonData);

			JSONObject json = new JSONObject(jsonData);
			String type = json.getString("type");

			if (type.equals(TYPES_chatMessage + "")
					|| type.equals(TYPES_chatSticker + "")
					|| type.equals(TYPES_chatFile + "")
					|| type.equals(TYPES_chatLocation + "")) {
				fromId = json.getString("from_id");
				fromName = json.getString("from_name");
				chatUrl += fromId;
				String m = fromName + "%20ได้ส่งข้อความหาคุณ";
				String notiUrl = "https://api.vdomax.com/noti/index.php?a=insert&f="
						+ fromId
						+ "&n="
						+ fromName
						+ "&t="
						+ json.getString("to_id")
						+ "&msg="
						+ m
						+ "&type="
						+ type;
				aq.ajax(notiUrl, JSONObject.class, this, "notiCb");
				int n = DataUser.VM_CHAT_N;
				n++;
				DataUser.VM_CHAT_N = n;
			} else if (type.equals(TYPES_confInvite + "")) {
				roomName = json.getString("room_name");
			} else if (type.equals(TYPES_confCreate + "")
					|| type.equals(TYPES_confJoin + "")) {
				roomName = json.getString("room_name");
			} else if (type.equals(TYPES_liveNow + "")) {
				fromId = json.getString("from_id");
				fromName = json.getString("from_name");
				postId = json.getString("post_id");
			} else if (type.equals(TYPES_commentFeed + "")
					|| type.equals(TYPES_likeFeed + "")) {
				fromId = json.getString("from_id");
				fromName = json.getString("from_name");
				postId = json.getString("post_id");
			} else if (type.equals(TYPES_chatInviteGroup + "")) {
				groupChatUrl += json.getString("cid");
				fromName = json.getString("extra");
				Log.e("mylog", groupChatUrl);
			} else if (type.equals(TYPES_chatVideoCall + "")) {
				videoCallUrl += json.getString("from_id") + "to" + DataUser.VM_USER_ID;
				fromName = json.getString("from_name");
			} else if (type.equals(TYPES_chatFreeCall + "")) {
				freeCallUrl += json.getString("from_id") + "&r="
						+ json.getString("from_id") + "&session=" + json.optString("session") ;
				fromName = json.getString("from_name");
			}
			
			

			intentManage(Integer.parseInt(type));

			// Log.d(TAG, "got action " + action + " on channel " + channel
			// + " with:");
			// Iterator itr = json.keys();
			// while (itr.hasNext()) {
			// String key = (String) itr.next();
			// Log.d(TAG, "..." + key + " => " + json.getString(key));
			// }
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}

	}

	public void notiCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

	}

	void intentManage(int type) {
		Intent toDetail = null;
		if (type == CATEGORYS_common) {
			toDetail = new Intent(ManagePush.this, null);
		} else if (type == CATEGORYS_chat) {
			toDetail = new Intent(ManagePush.this, null);
		} else if (type == TYPES_likeFeed) {
			Intent routeIntent = new Intent(this, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			startActivity(routeIntent);
		} else if (type == TYPES_commentFeed) {
			Intent routeIntent = new Intent(this, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			startActivity(routeIntent);
		} else if (type == TYPES_liveNow) {
			Intent routeIntent = new Intent(this, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			routeIntent.putExtra("user_id", fromId + "");
			startActivity(routeIntent);
			/*
			 * toDetail = new Intent(ManagePush.this, PlayActivity.class);
			 * toDetail.putExtra("isPlay", "1"); toDetail.putExtra("roomId",
			 * fromName); toDetail.putExtra("roomTag", "0");
			 * startActivity(toDetail);
			 */

		} else if (type == TYPES_followedYou) {
			Intent profileIntent = new Intent(this, RouteActivity.class);
			profileIntent.putExtra("type", "profile");
			profileIntent.putExtra("user_id", fromId + "");
			startActivity(profileIntent);

		} else if (type == TYPES_chatMessage || type == TYPES_chatSticker
				|| type == TYPES_chatFile || type == TYPES_chatLocation) {
			toDetail = new Intent(ManagePush.this, XWalkActivity.class);
			// toDetail.putExtra("userId", fromId);
			// toDetail.putExtra("friendName", fromName);
			
			toDetail.putExtra("url", chatUrl);
			toDetail.putExtra("title", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_confCreate || type == TYPES_confJoin
				|| type == TYPES_confInvite) {
			// intent Lobby
			toDetail = new Intent(ManagePush.this,
					ConferenceActivity.class);
			toDetail.putExtra("roomName", roomName);
			startActivity(toDetail);

		} else if (type == TYPES_chatFreeCall) {
			// intent XWalk
			toDetail = new Intent(ManagePush.this, XWalkChatRoomActivity.class);
			toDetail.putExtra("roomUrl", freeCallUrl);
			toDetail.putExtra("friendName", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_chatVideoCall) {
			// intent XWalk
			toDetail = new Intent(ManagePush.this, XWalkChatRoomActivity.class);
			toDetail.putExtra("roomUrl", videoCallUrl);
			toDetail.putExtra("friendName", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_chatInviteGroup) {
			toDetail = new Intent(ManagePush.this, XWalkActivity.class);
			toDetail.putExtra("url", groupChatUrl);
			toDetail.putExtra("title", fromName);
			startActivity(toDetail);
		}

		this.finish();
	}
}
