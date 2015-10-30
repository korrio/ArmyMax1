package com.androidbegin.armymax.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.pref.UserHelper;
import com.ame.armymax.xwalk.XWalkActivity;
import com.ame.armymax.xwalk.XWalkChatRoomActivity;
import com.androidquery.AQuery;

public class DialogClickFriends extends Dialog implements
		android.view.View.OnClickListener {

	private Button btChats, btFreeCall, btVideoCall;
	private TextView textUserName;
	private String friendName;
	private String friendId;
	private String avatar;
	private ImageView imgProfile;
	private AQuery aq;

	Context context;
    Activity activity;

	public DialogClickFriends(Activity activity, Context context, String frienduserName,
			String friendID, String avata) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.friendName = frienduserName;
		this.friendId = friendID;
		this.avatar = avata;
        this.context = context;
        this.activity = activity;
		//this.context = activity.getApplicationContext();
        //this.activity = activity;

		setContentView(R.layout.chat_dialog_friend);
		initDialog();

		// show();

	}

	void initDialog() {

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = 700;
		lp.height = 950;
		this.getWindow().setAttributes(lp);

		setTitle(friendName);
		// getWindow().setLayout(400, 600);
		aq = new AQuery(getContext());
		imgProfile = (ImageView) findViewById(R.id.imageProfile);
		aq.id(imgProfile).image(Link.LINK_PHOTO + avatar, true, true);
		textUserName = (TextView) findViewById(R.id.textUserName);
		textUserName.setText(friendName);
		btChats = (Button) findViewById(R.id.buttonChats);
		btFreeCall = (Button) findViewById(R.id.buttonFreeCall);
		btVideoCall = (Button) findViewById(R.id.buttonVideoCall);

		btChats.setOnClickListener(this);
		btFreeCall.setOnClickListener(this);
		btVideoCall.setOnClickListener(this);
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		dismiss();
		super.cancel();
	}
	
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		//Toast.makeText(getContext(), friendId + " : " + friendName,
			//	Toast.LENGTH_LONG).show();
		UserHelper user = new UserHelper(context);
		String username = user.getUserName();
		String token = user.getToken();
		String userId = user.getUserID();

		Intent toWeb;
		
		long session = System.currentTimeMillis() / 1000L;
		
		if (view == btChats) {
			/*
			 * toWeb = new Intent(getContext(), ChatRoomActivity.class);
			 * toWeb.putExtra("userId", friendId); toWeb.putExtra("friendName",
			 * friendName); getContext().startActivity(toWeb);
			 */

			Intent webView = new Intent(getContext(), XWalkActivity.class);
			webView.putExtra("url", "https://armymax.com/mobile.php?id="
					+ userId + "&token=" + token + "&type=user&fid=" + friendId);
			webView.putExtra("title", friendName);
			getContext().startActivity(webView);
		} else if (view == btFreeCall) {



//			String url = "http://armymax.com/RTCMultiConnection/demos/phone/audio.html?session="+session+"&userid="
//					+ friendId + "&r=" + userId;

            long unixTime = System.currentTimeMillis() / 1000L;

            String roomName = userId + "to" + friendId + unixTime;

            //armymax voice

            //String roomName = "voice";

            String url = "https://armymax.com:9393/" + roomName;

            //http://150.107.31.11:3000/r/


			String app = "ArmyMax%20AudioCall";
			String m = username + "%20โทรด้วยเสียงหาคุณ";
			String notiUrl = "http://armymax.com/api/noti/?title=" + app
					+ "&m=" + m + "&f=" + userId + "&n=" + username + "&t="
					+ friendId + "&type=504&session=" + session;


//
//            long unixTime = System.currentTimeMillis() / 1000L;
//
//            String roomName = "AM_"+ userId + "_" + friendId + "_" + unixTime ;
//
//            MainActivity.connectToRoom(activity, roomName, false);


			String status = "";
			notiUrl = notiUrl.replace(" ", "%20");

			Log.e("urlurl", notiUrl);

			Intent chatRoom = new Intent(getContext(),
					XWalkChatRoomActivity.class);
			chatRoom.putExtra("userId", friendId);
			chatRoom.putExtra("friendName", friendName);
			chatRoom.putExtra("roomUrl", url);
			chatRoom.putExtra("notiUrl", notiUrl);
			//Toast.makeText(getContext(), "url:" + url, Toast.LENGTH_SHORT)
				//	.show();
			getContext().startActivity(chatRoom);
		} else if (view == btVideoCall) {

            String app = "ArmyMax%20VideoCall";
            String m = username + "%20วิดิโอคอลหาคุณ";


            long unixTime = System.currentTimeMillis() / 1000L;

            //String roomName = "AM_"+ userId + "_" + friendId + "_" + unixTime ;

            String roomName = userId + "to" + friendId;



            String url = "http://armymax.com:9393/" + roomName;

//            String url = "http://150.107.31.11:3000/r/"
//					+ roomName;

            String notiUrl = "http://armymax.com/api/noti/?title=" + app
                    + "&m=" + m + "&f=" + userId + "&n=" + username + "&t="
                    + friendId + "&type=505&session=" + roomName;



            //MainActivity.connectToRoom(activity, roomName, true);


			Log.e("vUrl",url);


			notiUrl = notiUrl.replace(" ", "%20");
			Log.e("urlurl", notiUrl);

			Intent chatRoom = new Intent(getContext(),
					XWalkChatRoomActivity.class);
			chatRoom.putExtra("userId", friendId);
			chatRoom.putExtra("friendName", friendName);
			chatRoom.putExtra("roomUrl", url);
			chatRoom.putExtra("notiUrl", notiUrl);
			//Toast.makeText(getContext(), "url:" + url, Toast.LENGTH_SHORT)
				//	.show();
			getContext().startActivity(chatRoom);

		}
	}

}
