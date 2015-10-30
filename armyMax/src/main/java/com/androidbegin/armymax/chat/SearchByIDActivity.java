package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.pref.UserHelper;

public class SearchByIDActivity extends Activity implements OnClickListener{
	
	private EditText edtSearchFriend;
	private ListView listViewSearchFrien;
	private SeachFriendListAdapter adapterSearchFriend;
	private Button btSearch;
	private TextView textMyUserID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_search_activity);
		getActionBar().setTitle("เพิ่มเพื่อน");
		DataUser.context = this;
		initUI();
	}
	void initUI(){
		textMyUserID = (TextView)findViewById(R.id.textMyUserName);
		textMyUserID.setText(new UserHelper(this).getUserID());
		btSearch = (Button)findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		edtSearchFriend = (EditText)findViewById(R.id.edtSearchFriend);
	//	edtSearchFriend.setOnTouchListener(new rig)
		listViewSearchFrien = (ListView)findViewById(R.id.listViewSearchFrien);
				
		
		
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(SearchByIDActivity.this,
				AChatActivity.class);
		finish();
		startActivity(i);

	}
	
	void callService(){
		TaskSearchFriend task = new TaskSearchFriend(this, edtSearchFriend.getText().toString().trim(), new OnSearchFriendListener() {
			
			@Override
			public void SearchFriendSuccess(ArrayList<FriendSearchModel> arrSearchFriend) {
				// TODO Auto-generated method stub
				adapterSearchFriend = new SeachFriendListAdapter(SearchByIDActivity.this, 3, arrSearchFriend);
				listViewSearchFrien.setAdapter(adapterSearchFriend);
			}
		});
		task.execute();
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btSearch) {
			if (edtSearchFriend.getText().toString().length()>0) {
				callService();
			}
		}
	}
}
