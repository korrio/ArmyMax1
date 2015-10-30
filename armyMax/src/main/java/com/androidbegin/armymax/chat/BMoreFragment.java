package com.androidbegin.armymax.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.ame.armymax.ProfileActivity;
import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;

@SuppressLint("NewApi")
public class BMoreFragment extends Fragment implements OnClickListener {
	private View view;
	private Button btAddFriend, btSetting, btProfile,btCreateGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.chat_fragment_more, container, false);
		initUI();
		return view;
	}

	void initUI() {
		btAddFriend = (Button) view.findViewById(R.id.btAddFriend);
		// btSetting = (Button)view.findViewById(R.id.btSetting);
		btProfile = (Button) view.findViewById(R.id.btProfile);
		btCreateGroup = (Button) view.findViewById(R.id.btGroupChat);
		btCreateGroup.setOnClickListener(this);
		btAddFriend.setOnClickListener(this);
		// btSetting.setOnClickListener(this);
		btProfile.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btAddFriend) {
			Intent toAddFriend = new Intent(getActivity() , AddFriendActivity.class);
			startActivity(toAddFriend);
		}
		else if (view == btSetting) {
			//Click Setting
		}
		else if (view == btProfile) {
			//Click Profile
			Intent toProfile = new Intent(getActivity() , ProfileActivity.class);
			toProfile.putExtra("id", DataUser.VM_USER_ID);
			toProfile.putExtra("username", DataUser.VM_USER_NAME);
			startActivity(toProfile);
		} else if(view == btCreateGroup) {
			DialogCreateGroupChat dialogNameGroup  = new DialogCreateGroupChat(getActivity());
			dialogNameGroup.show();
		}
	}
}
