package com.ame.armymax;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ame.armymax.adapter.ChatFriendAdapter;
import com.ame.armymax.app.Constants;
import com.ame.armymax.model.DataChatFriend;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkConferenceActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.dd.CircularProgressButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConferenceActivity extends Activity {

	List<DataChatFriend> recentList = new ArrayList<DataChatFriend>();
	ChatFriendAdapter aAdpt;
	AQuery aq;
	Context context;

	BootstrapEditText titleText;
    CircularProgressButton createConfBtn;
	Dialog createConfDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conference_recent);

		context = this;
		aq = new AQuery(context);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Loading...");
		dialog.setMessage(getResources().getString(R.string.list_conference));

		String roomName = getIntent().getStringExtra("roomName");

		if (roomName != null) {
			joinRoom(roomName);
		} else {
			String recentConference = "https://armymax.com/api/conference.php?action=conferenceroom";
			aq.progress(dialog).ajax(recentConference, JSONArray.class, this,
					"recentConfCb");
		}

		createConfDialog = new Dialog(context, R.style.Dialog);

		createConfDialog.setContentView(R.layout.activity_create_conf);

		createConfDialog.setTitle(getResources().getString(R.string.create_conference));

		titleText = (BootstrapEditText) createConfDialog.findViewById(R.id.room_name);

        createConfBtn = (CircularProgressButton) createConfDialog.findViewById(R.id.button_create_conf);

        createConfBtn.setIndeterminateProgressMode(true); // turn on indeterminate progress

        createConfBtn.setProgress(0); // set progress to 0 to switch back to normal state

        createConfBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				createConf(titleText.getEditableText().toString());
                createConfBtn.setProgress(30);

			}
		});

		createConfDialog.getWindow().setBackgroundDrawableResource(
				R.drawable.bg_gray_texture);

	}

	public void createConf(String roomName) {
        String url = "";
        if(DataUser.VM_USER_ID !="" || DataUser.VM_USER_ID != null)
            url = Constants.CREATE_CONF
				+ roomName + "&user_id=" + DataUser.VM_USER_ID;
        else
            url = Constants.CREATE_CONF
                    + roomName + "&user_id=41";
		aq.ajax(url, JSONObject.class, this, "createConfCb");
	}

	public void createConfCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

        createConfBtn.setProgress(50); // set progress > 0 & < 100 to display indeterminate progress
        createConfBtn.setProgress(100); // set progress to 100 or -1 to indicate complete or error state
        if(jo != null) {
            String roomName = jo.optString("room_name");
            String userId = jo.optString("user_id");
            String token = jo.optString("token");
            String myurl = Constants.GOTO_CONF
                    + userId + "&token=" + token;
            Intent xwalk = new Intent(ConferenceActivity.this,
                    XWalkConferenceActivity.class);
            xwalk.putExtra("url", myurl);
            startActivity(xwalk);
        } else {
            createConfBtn.setProgress(-1);
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
		case R.id.action_create_conference:
			createConfDialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu aMenu) {

		return super.onPrepareOptionsMenu(aMenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.conference, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void recentConfCb(String url, JSONArray ja, AjaxStatus status)
			throws JSONException {
		if (ja.length() != 0) {
			Log.e("conference", ja.toString());

			for (int i = 0; i < ja.length(); i++) {
				JSONObject list = ja.optJSONObject(i);
				String userName = list.optString("name");
				String type = list.optString("type");
				String create_by = list.optString("create_by");
				int id = Integer.parseInt(list.optString("id"));

				DataChatFriend ppl = new DataChatFriend(userName, "Create by "
						+ create_by, type, id);
				recentList.add(ppl);
			}
		} else {
			createConfDialog.show();
		}
		ListView lv = (ListView) findViewById(R.id.listView);
		aAdpt = new ChatFriendAdapter(recentList, this);
		lv.setAdapter(aAdpt);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {

				DataChatFriend p = (DataChatFriend) parentAdapter
						.getItemAtPosition(position);

				String roomName = p.getName();

				joinRoom(roomName);

			}
		});

		// we register for the contextmneu
		registerForContextMenu(lv);

		// TextFilter
		lv.setTextFilterEnabled(true);

		EditText editTxt = (EditText) findViewById(R.id.editTxt);

	}

	public void joinRoom(String roomName) {
		String requestConference = "https://armymax.com/api/conference.php?action=requestconference&action_type=JOIN&room_type=GROUP&room_name="
				+ roomName + "&user_id=" + DataUser.VM_USER_ID;

		Log.e("conferenceroom", requestConference);

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(getResources().getString(R.string.req_conference));

		aq.progress(dialog).ajax(requestConference, JSONObject.class, context,
				"requestConferenceCb");
	}
	
	public void joinRoomToken(String cuser_id,String ctoken) {
		Intent xwalk = new Intent(ConferenceActivity.this,
				XWalkConferenceActivity.class);
		xwalk.putExtra("url",
				"https://armymax.com/app/gotoconfpost.php?user_id="
						+ cuser_id + "&token=" + ctoken);
		startActivity(xwalk);
	}

	public void requestConferenceCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		
		if(jo.optInt("error_code") != 440) {
			String ctoken = jo.optString("token");
			String cuser_id = jo.optString("user_id");

			Intent xwalk = new Intent(ConferenceActivity.this,
					XWalkConferenceActivity.class);
			xwalk.putExtra("url",
					"https://armymax.com/app/gotoconfpost.php?user_id="
							+ cuser_id + "&token=" + ctoken);
			startActivity(xwalk);
		} else {
			Toast.makeText(context,getResources().getString(R.string.req_conference_error), Toast.LENGTH_LONG).show();
		}

		

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	// We want to create a context Menu when the user long click on an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		DataChatFriend planet = aAdpt.getItem(aInfo.position);

		menu.setHeaderTitle("Options for " + planet.getName());
		menu.add(1, 1, 1, "Details");
		menu.add(1, 2, 2, "Delete");

	}

	// This method is called when user selects an Item in the Context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		recentList.remove(aInfo.position);
		aAdpt.notifyDataSetChanged();
		return true;
	}

}
