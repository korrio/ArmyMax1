package com.androidbegin.armymax.chat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.pref.UserHelper;
import com.ame.armymax.xwalk.XWalkActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class DialogSelectFriendsForChatGroup extends Dialog implements
		OnItemClickListener, android.view.View.OnClickListener {

	private ListView listSelectChatGroup;
	private Button btSelect;
	private ChatGroupAdapter adapterChatGroup;
	private String nameGroup;
	String roomName;
	Context context;

	public DialogSelectFriendsForChatGroup(Context context, String nameGroup) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub

		this.nameGroup = nameGroup;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_dialog_selectchatgroup);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);

		this.context = context;

		initUI();
	}

	void initUI() {
		btSelect = (Button) findViewById(R.id.btSelect);
		btSelect.setOnClickListener(this);
		listSelectChatGroup = (ListView) findViewById(R.id.listViewSelectChatGroup);
		listSelectChatGroup.setOnItemClickListener(this);

		TaskFriends taskFriend = new TaskFriends(getContext(),
				new OnTaskFriendListener() {

					@Override
					public void loadTaskFriendSuccess(
							ArrayList<FriendContentModel> arrFriends,
							ArrayList<ChatGroupModel> arrChatGroup) {
						// TODO Auto-generated method stub
						if (arrFriends.size() > 0) {
							adapterChatGroup = new ChatGroupAdapter(
									getContext(), arrFriends);
							listSelectChatGroup.setAdapter(adapterChatGroup);
						} else {
							Toast.makeText(
									getContext(),
									"กรุณาเลือกเพื่อนก่อน",
									Toast.LENGTH_LONG).show();
						}

					}
				});
		taskFriend.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		adapterChatGroup.setSelectToggle(position);

	}
	
	AQuery aq;
	
	public void notiCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		
		Log.e("myjson",jo.toString());

	}

	ArrayList<String> inviteList = new ArrayList<String>();
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btSelect) {
			String result = "";
			// getCheckedItems
			List<String> resultListSelect = adapterChatGroup.getCheckedItems();
			
			if (resultListSelect.size() > 0) {
				
				for (int i = 0; i < resultListSelect.size(); i++) {
					result += String.valueOf(resultListSelect.get(i)) + ",";
					inviteList.add(String.valueOf(resultListSelect.get(i)));

				}
				adapterChatGroup.getCheckedItemPositions().toString();
				Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
				this.dismiss(); // Close Dialog

				roomName = nameGroup;
				Toast.makeText(context, nameGroup, Toast.LENGTH_SHORT).show();

				aq = new AQuery(context);
				TaskCreateGroup task = new TaskCreateGroup(nameGroup,
						new UserHelper(getContext()).getUserID(), getContext(),
						new OnCreateGroup() {

							@Override
							public void CreateGroupSuccess(
									String conversation_id) {
								// TODO Auto-generated method stub
								String app = "ArmyMax%20Group%20Chat";
								String m = "ได้ชวนคุณเข้าร่วมแชทกลุ่ม: " + nameGroup;
								
								for (int i = 0; i < inviteList.size(); i++) {
									String notiUrl = "http://www.armymax.com/api/noti/?title=" + app
											+ "&m=" + m + "&f=" + DataUser.VM_USER_ID + "&n="
											+ DataUser.VM_NAME + "&t=" + inviteList.get(i) + "&type=506&cid="+conversation_id + "&extra=" + nameGroup ;
									aq.ajax(notiUrl, JSONObject.class, this, "notiCb");
								}
								
								intentToChat(conversation_id,nameGroup);
							}

							@Override
							public void CreateGroupFailed() {
								// TODO Auto-generated method stub
								Toast.makeText(getContext(),
										"เกิดข้อผิดพลาดในการชวนเพื่อนเข้ากลุ่ม",
										Toast.LENGTH_LONG).show();
							}
						});
				task.execute();
			} else {
				Toast.makeText(getContext(),
						"กรุณาเลือกชื่อเพื่อนก่อน",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	void intentToChat(String conversation_id,String room_name) {
		Intent toChat = new Intent(getContext(), XWalkActivity.class);
		//toChat.putExtra("userId", "0");
		//toChat.putExtra("friendName", roomName);
		toChat.putExtra("url", "https://www.armymax.com/mobile.php?token="
				+ DataUser.VM_USER_TOKEN + "&id=" + DataUser.VM_USER_ID
				+ "&type=group&rid=" + conversation_id);
		toChat.putExtra("title",room_name);
		// startActivity(toChat);
		getContext().startActivity(toChat);
	}

}
