package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class ChatGroupActivity extends Activity implements OnClickListener {
	private TextView textTitleBar, textCountUser;
	private Button btInvite, btChat, btLeave;
	private ListView listChatGroup;
	private String rid;
	private String roomCreatorId;

	String roomName;
	Context context;
	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_chatgroup);
		context = this;
		DataUser.context = this;
		aq = new AQuery(context);

		initUI();
	}

	void initUI() {
		// value
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();

		// AQuery aq = new AQuery(ChatGroupActivity.this);
		ChatGroupModel model = (ChatGroupModel) bundle.getSerializable("value");
		rid = model.getId();
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textCountUser = (TextView) findViewById(R.id.textMemberCount);
		listChatGroup = (ListView) findViewById(R.id.listChatGroup);
		// listChatGroup.setOnItemClickListener(this);
		btInvite = (Button) findViewById(R.id.btInvite);
		btChat = (Button) findViewById(R.id.btChat);
		btLeave = (Button) findViewById(R.id.btLeave);

		btInvite.setOnClickListener(this);
		btChat.setOnClickListener(this);
		btLeave.setOnClickListener(this);

		roomName = model.getName();
		roomCreatorId = model.getCreate_by();

		textTitleBar.setText(roomName);

		// aq.id(thumeGroup).image(Link.LINK_PHOTO+model.)


		
		callService(model.getId());

		// callService("1");
	}

	void callService(String conversationId) {
		TaskGroupChat task = new TaskGroupChat(ChatGroupActivity.this,
				conversationId, new OnLoadFriendGroupSuccess() {

					@Override
					public void loadSucces(ArrayList<ChatGroupListModel> arrData) {
						// TODO Auto-generated method stub
						ChatGroupListFriendAdapter adapter = new ChatGroupListFriendAdapter(
								ChatGroupActivity.this, arrData);
						listChatGroup.setAdapter(adapter);
						int memberCount = arrData.size();

						textCountUser.setText(memberCount + " คน");

					}

					@Override
					public void loadFaild() {
						// TODO Auto-generated method stub

					}
				});
		task.execute();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btInvite) {
			DialogSelectFriendsForChatGroup inviteFriendDialog = new DialogSelectFriendsForChatGroup(
					context, roomName);
			inviteFriendDialog.show();
		} else if (view == btChat) {
			Intent toChat = new Intent(this, XWalkActivity.class);
			String url = "https://www.armymax.com/mobile.php?token="
					+ DataUser.VM_USER_TOKEN + "&id=" + DataUser.VM_USER_ID
					+ "&type=group&rid=" + rid;
			Toast.makeText(context, url, Toast.LENGTH_LONG).show();
			toChat.putExtra("url", url);
			toChat.putExtra("title", roomName);
			startActivity(toChat);
		} else if (view == btLeave) {
			String url = "http://www.armymax.com/api/chat.php?action=leavegroup&rid="
					+ rid
					+ "&uid="
					+ DataUser.VM_USER_ID
					+ "&creator_uid="
					+ roomCreatorId;
			Log.e("leaveGroup", url);
			aq.ajax(url, JSONObject.class, context, "leaveCb");
		}
	}

	public void leaveCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo.optInt("status") == 50001) {
			Toast.makeText(context, jo.optString("msg"), Toast.LENGTH_LONG)
					.show();
			Intent toAChat = new Intent(ChatGroupActivity.this,
					AChatActivity.class);
			startActivity(toAChat);
		}

	}

}
