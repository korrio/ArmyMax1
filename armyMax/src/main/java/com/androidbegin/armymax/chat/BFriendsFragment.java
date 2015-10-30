package com.androidbegin.armymax.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ame.armymax.R;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class BFriendsFragment extends Fragment implements OnClickListener,
		OnItemClickListener {
	private View view;
	private ListView listViewFriend;
	private EditText edtSearch;
	private Button btCancel;
	private FriendsListAdapter adapterListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.chat_fragment_friends, container,
				false);
		initUI();
		return view;
	}

	void initUI() {
		edtSearch = (EditText) view.findViewById(R.id.editSearch);
		btCancel = (Button) view.findViewById(R.id.btCancel);
		btCancel.setOnClickListener(this);
		btCancel.setVisibility(View.GONE);
		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				adapterListView.getFilter().filter(s);
				if (s.length() > 0) {
					btCancel.setVisibility(View.VISIBLE);
				} else {
					btCancel.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		listViewFriend = (ListView) view.findViewById(R.id.listViewFriends);
		listViewFriend.setOnItemClickListener(this);

		callService();
		// setUI();
	}

	void callService() {
		TaskFriends taskFriends = new TaskFriends(getActivity(),
				new OnTaskFriendListener() {

					@Override
					public void loadTaskFriendSuccess(
							ArrayList<FriendContentModel> arrFriends,
							ArrayList<ChatGroupModel> arrChatGroup) {
						// TODO Auto-generated method stub
						// System.out.println("arrChatGroup : "+arrChatGroup.size());
						setUI(arrFriends, arrChatGroup);
					}
				});
		taskFriends.execute();
	}

	void setUI(ArrayList<FriendContentModel> arrFriends,
			ArrayList<ChatGroupModel> arrChatGroup) {
		System.out.println("arrFriends : " + arrFriends.size());
		// adapterListView = new FriendsListAdapter(getActivity() , arrFriends);
		adapterListView = new FriendsListAdapter(getActivity(), 1, arrFriends,
				arrChatGroup);
		listViewFriend.setAdapter(adapterListView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btCancel) {
			btCancel.setVisibility(View.GONE);
			edtSearch.setText("");
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// FriendContentModel modelFriend = (FriendContentModel)
		// parent.getAdapter().getItem(position);
		// FriendContentModel modelFriend = adapterListView.getData(position);
		adapterListView.getData(position, new OngetItemFriendListener() {

			@Override
			public void isChatGroup(ChatGroupModel modelFriend) {
				// TODO Auto-generated method stub
				Intent toChatGroup = new Intent(getActivity(),
						ChatGroupActivity.class);
				Bundle bundle = new Bundle();
				
				bundle.putSerializable("value", modelFriend);
				toChatGroup.putExtras(bundle);
				startActivity(toChatGroup);
			}

			@Override
			public void isChat(FriendContentModel modelFriend) {
				// TODO Auto-generated method stub
				Utils.showLog("FRIEND ++ ", modelFriend.getUserName());
				DialogClickFriends dialogFriends = new DialogClickFriends(getActivity(),getActivity(),
						modelFriend.getUserName(), modelFriend.getUserID(),
						modelFriend.getUserAvatarPath());
				dialogFriends.show();
			}
		});

	}

}
