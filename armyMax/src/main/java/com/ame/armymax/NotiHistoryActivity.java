package com.ame.armymax;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ame.armymax.adapter.NotiAdapter;
import com.ame.armymax.model.DataNoti;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkActivity;
import com.ame.armymax.xwalk.XWalkChatRoomActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

@SuppressLint("ValidFragment")
public class NotiHistoryActivity extends Activity {

	AQuery aq;

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

	private String fromId;
	private String fromName;
	private String roomName;
	private String postId;
	long session = System.currentTimeMillis() / 1000L;
	
	
	private String freeCallUrl = "http://www.armymax.com/RTCMultiConnection/demos/phone/audio.html?session="+session+"&userid="
			+ DataUser.VM_USER_ID + "&r=" + DataUser.VM_USER_ID;;
	private String videoCallUrl = "http://www.armymax.com/RTCMultiConnection/demos/AppRTC-Look.html?";
	private String groupChatUrl = "https://www.armymax.com/mobile.php?id="
			+ DataUser.VM_USER_ID + "&token=" + DataUser.VM_USER_TOKEN
			+ "&type=group&rid=";
	private String chatUrl = "https://www.armymax.com/mobile.php?id="
			+ DataUser.VM_USER_ID + "&token=" + DataUser.VM_USER_TOKEN
			+ "&type=user&fid=";

	Context context;
	ListView listView;
	NotiAdapter adapter;
	long refresh = -1;

	public ArrayList<DataNoti> notiList = new ArrayList<DataNoti>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_feed_pull);
		context = this;
		DataUser.context = context;
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.feed_listview_pull);
		//listView.onRefreshComplete();
		/*
		 * listView.setOnLoadMoreListener(new OnLoadMoreListener() { public void
		 * onLoadMore() { // Do the work to load more items at the end of list
		 * here refreshList(refresh); } });
		 */

		aq = new AQuery(context);

		refreshList(refresh);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long id) {
				DataNoti noti = notiList.get(i);

				String type = Integer.parseInt(noti.getType()) + "";
				if (type.equals(TYPES_chatMessage + "")
						|| type.equals(TYPES_chatSticker + "")
						|| type.equals(TYPES_chatFile + "")
						|| type.equals(TYPES_chatLocation + "")) {
					fromId = noti.getFromId();
					fromName = noti.getFromName();
					chatUrl += fromId;
					int n = DataUser.VM_CHAT_N;
					n = n--;
					if (n >= 0) {
						DataUser.VM_CHAT_N = n;
					}

				} else if (type.equals(TYPES_confInvite + "")
						|| type.equals(TYPES_confCreate + "")
						|| type.equals(TYPES_confJoin + "")) {
					roomName = noti.getExtra();
				} else if (type.equals(TYPES_liveNow + "")) {
					fromId = noti.getFromId();
					fromName = noti.getFromName();
					postId = noti.getFromId();
				} else if (type.equals(TYPES_commentFeed + "")
						|| type.equals(TYPES_likeFeed + "")) {
					fromId = noti.getFromId();
					fromName = noti.getFromName();
					postId = noti.getPostId();
				} else if (type.equals(TYPES_chatInviteGroup + "")) {
					groupChatUrl += noti.getPostId();
					roomName = noti.getExtra();
				} else if (type.equals(TYPES_chatVideoCall + "")) {
					videoCallUrl += DataUser.VM_USER_ID + "to" + fromId;
					fromName = noti.getFromName();
					fromId = noti.getFromId();
				} else if (type.equals(TYPES_chatFreeCall + "")) {
					
					fromName = noti.getFromName();
					fromId = noti.getFromId();
				}

				intentManage(Integer.parseInt(type));

				read(noti.getNotiId());

			}

		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long arg3) {

				AlertDialog.Builder adb = new AlertDialog.Builder(context);
				adb.setTitle("Delete?");
				adb.setMessage("ต้องการลบการแจ้งเตือนหรือไม่");
				final int positionToRemove = position;
				adb.setNegativeButton("ยกเลิก", null);
				adb.setPositiveButton("ลบ", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Toast.makeText(context, positionToRemove + "",
						// Toast.LENGTH_SHORT).show();
						DataNoti noti = notiList.get(positionToRemove);
						// listView.removeViewAt(positionToRemove);
						delete(noti.getNotiId());
						adapter.remove(noti);
						adapter.notifyDataSetChanged();

					}
				});
				adb.show();

				return true;
			}

		});

        /*
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				notiList.clear();

				refreshList(-1);
				listView.onRefreshComplete();
			}
		});
		*/

	}

	public void read(String id) {
		String readUrl = "http://armymax.com/api/noti/noti.php?a=read&noti_id="
				+ id;
		aq.ajax(readUrl, JSONObject.class, context, "readCb");
	}

	public void delete(String id) {
		String readUrl = "http://armymax.com/api/noti/noti.php?a=delete&noti_id="
				+ id;
		Log.e("remove", readUrl);
		aq.ajax(readUrl, JSONObject.class, context, "deleteCb");
		refreshList(-1);
	}

	public void readCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// refreshList(-1);
	}

	public void deleteCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

	}

	@Override
	public void onResume() {
		// refreshList(true);
		super.onResume();
	}

	public void refreshList(long refresh) {
		this.refresh = refresh;
		String cbName;


		String surl = "https://www.armymax.com/api/noti/noti.php?a=list&user_id="
				+ DataUser.VM_USER_ID;
		cbName = "feedNotiCb";

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Loading...");
		dialog.setMessage("กำลังโหลดการแจ้งเตือน");

		aq.progress(dialog).ajax(surl, JSONObject.class, refresh, this, cbName);

	}

	public void feedNotiCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Log.e("check", jo.toString());
			JSONArray ja = jo.optJSONArray("data");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject noti = ja.getJSONObject(i);
				String notiId = noti.getString("id");

				// String myName = noti.getString("owner_username");
				String fromId = noti.getString("from_id");
				String fromName = noti.getString("from_name");
				String toId = noti.getString("to_id");
				String type = noti.getString("type");
				String postId = noti.getString("post_id");
				String msg = noti.getString("msg");
				String extra = noti.optString("extra");
				boolean readed = (boolean) "1".equals(noti.getString("readed"));
				String lastupdate = noti.getString("time_received");

				PrettyTime p = new PrettyTime();
				long agoLong = Integer.parseInt(lastupdate);
				Date timeAgo = new java.util.Date((long) agoLong * 1000);
				String ago = p.format(timeAgo);

				// DataNoti(String notiId,String type,String myName,String
				// friendName,String myId,String friendId,String postId,String
				// ago,String msg,boolean readed)
				// if(to_id.equals(DataUser.VM_USER_ID)) {
				DataNoti notiFeed = new DataNoti(notiId, type,
						DataUser.VM_NAME, fromName, DataUser.VM_USER_ID,
						fromId, postId, extra, ago, msg, readed);
				notiList.add(notiFeed);
				// }

			}

			adapter = new NotiAdapter(notiList, context);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			//listView.onRefreshComplete();
			//listView.onLoadMoreComplete();

		} else {
			DataNoti notiFeed = new DataNoti("", "0", DataUser.VM_NAME, "",
					DataUser.VM_USER_ID, "", "", "", "",
					"ยังไม่มีการแจ้งเตือน", true);
			notiList.add(notiFeed);
			adapter = new NotiAdapter(notiList, context);

			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			//listView.onRefreshComplete();

		}
	}

	void intentManage(int type) {
		Intent toDetail = null;
		if (type == TYPES_likeFeed) {
			Intent routeIntent = new Intent(context, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			startActivity(routeIntent);
		} else if (type == TYPES_commentFeed) {
			Intent routeIntent = new Intent(context, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			startActivity(routeIntent);
		} else if (type == TYPES_liveNow) {
			Intent routeIntent = new Intent(context, RouteActivity.class);
			routeIntent.putExtra("type", "post");
			routeIntent.putExtra("post_id", postId + "");
			routeIntent.putExtra("user_id", fromId + "");
			startActivity(routeIntent);

		} else if (type == TYPES_followedYou) {
			Intent profileIntent = new Intent(context, RouteActivity.class);
			profileIntent.putExtra("type", "profile");
			profileIntent.putExtra("user_id", fromId + "");
			startActivity(profileIntent);

		} else if (type == TYPES_chatMessage || type == TYPES_chatSticker
				|| type == TYPES_chatFile || type == TYPES_chatLocation) {
			toDetail = new Intent(context, XWalkActivity.class);
			// toDetail.putExtra("userId", fromId);
			// toDetail.putExtra("friendName", fromName);
			toDetail.putExtra("url", chatUrl);
			toDetail.putExtra("title", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_confCreate || type == TYPES_confJoin
				|| type == TYPES_confInvite) {
			// intent Lobby
			toDetail = new Intent(context, ConferenceActivity.class);
			toDetail.putExtra("roomName", roomName);
			startActivity(toDetail);

		} else if (type == TYPES_chatFreeCall) {
			// intent XWalk
			String app = "ArmyMax%20AudioCall";
			String m = "";
			String notiUrl = "http://www.armymax.com/api/noti/?title=" + app
					+ "&m=" + m + "&f=" + DataUser.VM_USER_ID + "&n="
					+ DataUser.VM_NAME + "&t=" + fromId + "&type="
					+ TYPES_chatFreeCall + "&extra=callBack";
			aq.ajax(notiUrl, JSONObject.class, this, "notiCb");
			
			toDetail = new Intent(context, XWalkChatRoomActivity.class);
			toDetail.putExtra("roomUrl", freeCallUrl);
			toDetail.putExtra("friendName", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_chatVideoCall) {
			// intent XWalk
			String app = "ArmyMax%20VideoCall";
			String m = "";
			String notiUrl = "http://www.armymax.com/api/noti/?title=" + app
					+ "&m=" + m + "&f=" + DataUser.VM_USER_ID + "&n="
					+ DataUser.VM_NAME + "&t=" + fromId + "&type="
					+ TYPES_chatVideoCall + "&extra=callBack";
			aq.ajax(notiUrl, JSONObject.class, this, "notiCb");
			toDetail = new Intent(context, XWalkChatRoomActivity.class);
			toDetail.putExtra("roomUrl", videoCallUrl);
			toDetail.putExtra("friendName", fromName);
			startActivity(toDetail);
		} else if (type == TYPES_chatInviteGroup) {
			toDetail = new Intent(context, XWalkActivity.class);
			toDetail.putExtra("url", groupChatUrl);
			toDetail.putExtra("title", roomName);
			startActivity(toDetail);
		}

		// context.finish();
	}

	public void notiCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

		Log.e("myjson", jo.toString());

	}

	public class AdapterHelper {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void update(ArrayAdapter arrayAdapter,
				ArrayList<Object> listOfObject) {
			arrayAdapter.clear();
			for (Object object : listOfObject) {
				arrayAdapter.add(object);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
