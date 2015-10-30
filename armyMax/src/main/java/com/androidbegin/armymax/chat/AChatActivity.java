package com.androidbegin.armymax.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.MainActivity;
import com.ame.armymax.model.DataUser;

public class AChatActivity extends FragmentActivity implements OnClickListener {

	private LinearLayout layoutFriends, layoutChats, layoutTimeline,
			layoutMore;
	private TextView textTitleBar;
	private ImageView iconTitlebarRight;
	private ImageView iconBackHome;
	private View includeTabbar;
	private TextView chatBadge;
	private int curentPage;
	Context context;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_activity_main);
		initUI();
		context = getApplicationContext();
		DataUser.context = context;
		
	}
	
	void initUI() {
		iconTitlebarRight = (ImageView) findViewById(R.id.iconTitleBarRight);
		iconBackHome = (ImageView) findViewById(R.id.iconBackHome);
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		layoutFriends = (LinearLayout) findViewById(R.id.layoutFriends);
		layoutChats = (LinearLayout) findViewById(R.id.layoutChats);
		layoutTimeline = (LinearLayout) findViewById(R.id.layoutTimeline);
		layoutMore = (LinearLayout) findViewById(R.id.layoutMore);
		includeTabbar = findViewById(R.id.tabbar);
		chatBadge = (TextView) includeTabbar.findViewById(R.id.chatBadge);

		if (DataUser.VM_CHAT_N > 0)
			chatBadge.setText(DataUser.VM_CHAT_N + "+");
		else
			chatBadge.setVisibility(View.GONE);

		layoutFriends.setOnClickListener(this);
		layoutChats.setOnClickListener(this);
		layoutTimeline.setOnClickListener(this);
		layoutMore.setOnClickListener(this);
		iconTitlebarRight.setOnClickListener(this);
		iconBackHome.setOnClickListener(this);

		if(DataUser.chatLastTab == 0 || DataUser.chatLastTab == FragmentConstant.KEY_FRIENDS) {
			manageFragment(FragmentConstant.KEY_FRIENDS); // Add Fragment First
		} else if(DataUser.chatLastTab == FragmentConstant.KEY_CHATS) {
			manageFragment(FragmentConstant.KEY_CHATS);
			//chatBadge.setText(DataUser.VM_CHAT_N + "");
		} else if(DataUser.chatLastTab == FragmentConstant.KEY_TIMELINE) {
			manageFragment(FragmentConstant.KEY_TIMELINE);
		} else if(DataUser.chatLastTab == FragmentConstant.KEY_MORE) {
			manageFragment(FragmentConstant.KEY_MORE);
		} else {
			manageFragment(FragmentConstant.KEY_FRIENDS); // Add Fragment First
		}
		
	}
	
	boolean doubleBackToExitPressedOnce = false;

	@Override
	public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        Intent i = new Intent(AChatActivity.this, MainActivity.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
	        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			finish();
			startActivity(i);
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "แตะ BACK อีกครั้งเพื่อออกจาก Chat", Toast.LENGTH_SHORT).show();

	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	


	void manageFragment(int constantFragment) {
		curentPage = constantFragment;
		Fragment fragmentSet = null;

		if (constantFragment == FragmentConstant.KEY_FRIENDS) {
			
			textTitleBar.setText("เพื่อน");
			fragmentSet = new BFriendsFragment();
		} else if (constantFragment == FragmentConstant.KEY_CHATS) {
			textTitleBar.setText("แชท");
			fragmentSet = new BHistoryFragment();
		} else if (constantFragment == FragmentConstant.KEY_TIMELINE) {
			textTitleBar.setText("ไทม์ไลน์");
			fragmentSet = new BTimelineFragment();
		} else if (constantFragment == FragmentConstant.KEY_MORE) {
			textTitleBar.setText("อื่นๆ");
			fragmentSet = new BMoreFragment();
		}

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		transaction.replace(R.id.My_Container_2_ID, fragmentSet,
				"Frag_Bottom_tag");
		transaction.commit();

		manageIconTitleBar();// chang Icon Titlebar
	}

	void manageIconTitleBar() {
		int image = 0;
		if (curentPage == FragmentConstant.KEY_FRIENDS) {
			image = R.drawable.ic_qr;
			iconTitlebarRight.setVisibility(View.VISIBLE);
		} else if (curentPage == FragmentConstant.KEY_CHATS) {
			image = R.drawable.ic_group_chat;
			iconTitlebarRight.setVisibility(View.VISIBLE);
		} else if (curentPage == FragmentConstant.KEY_TIMELINE) {
			image = R.drawable.ic_timeline;
			iconTitlebarRight.setVisibility(View.GONE);
		} else if (curentPage == FragmentConstant.KEY_MORE) {
			image = R.drawable.ic_more;
			iconTitlebarRight.setVisibility(View.GONE);
		}
		iconTitlebarRight.setImageResource(image);
	}
	
	

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == layoutFriends) {
			manageFragment(FragmentConstant.KEY_FRIENDS);
			DataUser.chatLastTab = FragmentConstant.KEY_FRIENDS;
		} else if (view == layoutChats) {
			manageFragment(FragmentConstant.KEY_CHATS);
			DataUser.chatLastTab = FragmentConstant.KEY_CHATS;
		} else if (view == layoutTimeline) {
			manageFragment(FragmentConstant.KEY_TIMELINE);
			DataUser.chatLastTab = FragmentConstant.KEY_TIMELINE;
		} else if (view == layoutMore) {
			manageFragment(FragmentConstant.KEY_MORE);
			DataUser.chatLastTab = FragmentConstant.KEY_MORE;
		} else if (view == iconTitlebarRight) {
			// Click Icon On title bar
			if (curentPage == FragmentConstant.KEY_FRIENDS) {
				Intent toAddFriend = new Intent(this, AddFriendActivity.class);
				startActivity(toAddFriend);
			} else if (curentPage == FragmentConstant.KEY_CHATS) {
				DialogCreateGroupChat dialogNameGroup = new DialogCreateGroupChat(
						AChatActivity.this);
				dialogNameGroup.show();
			} else if (curentPage == FragmentConstant.KEY_TIMELINE) {

			} else if (curentPage == FragmentConstant.KEY_MORE) {

			}
		} else if (view == iconBackHome) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Intent i = new Intent(AChatActivity.this,
								MainActivity.class);
						finish();
						startActivity(i);
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						dialog.dismiss();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("ต้องการออกจากห้องห้องสนทนา?")
					.setPositiveButton("ใช่", dialogClickListener)
					.setNegativeButton("ไม่", dialogClickListener).show();
		}
	}
}
