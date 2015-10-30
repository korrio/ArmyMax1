package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

@SuppressLint("NewApi")
public class BHistoryFragment extends Fragment implements OnItemClickListener {
	private View view;
	private ListView listViewChats;
	private ChatsListAdapter adapterListChats;
	ArrayList<ChatsModel> historyList;
	AQuery aq;
	TextView unreaded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.chat_fragment_history, container,
				false);

		aq = new AQuery(getActivity());

		initUI();
		callService();
		return view;
	}

	void initUI() {
		listViewChats = (ListView) view.findViewById(R.id.listViewChats);
		listViewChats.setOnItemClickListener(this);
	}

	void callService() {
		TaskHistory taskChats = new TaskHistory(getActivity(),
				new OnTaskChatsListener() {

					@Override
					public void loadDataChatsSuccess(
							ArrayList<ChatsModel> arrChats) {
						// TODO Auto-generated method stub
						adapterListChats = new ChatsListAdapter(getActivity(),
								2, arrChats);

						historyList = adapterListChats.getAll();
						listViewChats.setAdapter(adapterListChats);
						// System.out.println("LOAD CHATS SUCCESS");
					}
				});
		taskChats.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		unreaded = (TextView) view.findViewById(R.id.unreaded);

		String readNotiUrl = "http://www.armymax.com/api/noti/noti.php?a=readed&noti_id="
				+ "";
		String readMessageUrl = "http://www.armymax.com/api/chat.php?action=read&rid="
				+ historyList.get(position).getRecentModel().getLASTMESSAGE()
						.getRID();
		aq.ajax(readMessageUrl, JSONObject.class, this, "readCb");
		Intent toChats = new Intent(getActivity(), XWalkActivity.class);
		toChats.putExtra("friendName", historyList.get(position)
				.getRecentModel().getUSERFRIENDPROFILE().getUSERFIRSTNAME()
				+ " "
				+ historyList.get(position).getRecentModel()
						.getUSERFRIENDPROFILE().getUSERLASTNAME());
		toChats.putExtra("userId", historyList.get(position).getRecentModel()
				.getUSERFRIENDPROFILE().getFRIENDID());
		toChats.putExtra(
				"url",
				Link.getLinkChat(historyList.get(position).getRecentModel()
						.getUSERFRIENDPROFILE().getFRIENDID()));
		toChats.putExtra("title", historyList.get(position).getRecentModel()
				.getUSERFRIENDPROFILE().getUSERFIRSTNAME()
				+ " "
				+ historyList.get(position).getRecentModel()
						.getUSERFRIENDPROFILE().getUSERLASTNAME());
		startActivity(toChats);
		unreaded.setVisibility(View.GONE);
		int n = DataUser.VM_CHAT_N;
		n = n--;
		if(n >= 0)
			DataUser.VM_CHAT_N = n;
		else {
			DataUser.VM_CHAT_N = 0;
			
		}
			
	}

	public void readCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

	}
}
