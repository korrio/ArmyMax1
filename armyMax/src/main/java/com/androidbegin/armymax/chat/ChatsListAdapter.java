package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;

public class ChatsListAdapter extends ArrayAdapter<ChatsModel> {

	private Context context;
	private View view;
	private ArrayList<ChatsModel> arrChats;
	private AQuery aq;

	public ChatsListAdapter(Context context, int resource,
			ArrayList<ChatsModel> arrChats) {
		super(context, resource, arrChats);
		this.context = context;
		this.arrChats = arrChats;
		this.aq = new AQuery(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrChats.size();
	}

	@Override
	public ChatsModel getItem(int position) {
		// TODO Auto-generated method stub
		return arrChats.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<ChatsModel> getAll() {
		return arrChats;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		view = convertView;
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.chat_layout_list_history_item,
					null);
			holder.textUser = (TextView) view.findViewById(R.id.textUsername);
			holder.thumeProfile = (ImageView) view
					.findViewById(R.id.imageProfile);
			holder.textChat = (TextView) view.findViewById(R.id.textChat);
			holder.unreaded = (TextView) view.findViewById(R.id.unreaded);
			view.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Chats_FriendProfileModel model = arrChats.get(position)
				.getRecentModel().getUSERFRIENDPROFILE();
		Chats_LastMassageModel lastMsgObj = arrChats.get(position)
				.getRecentModel().getLASTMESSAGE();
		String lastMsg = lastMsgObj.getMESSAGECHAT();
		String type = lastMsgObj.getMESSAGETYPE();

		if (type.equals("0")) {
			lastMsg = lastMsg;
		} else if (type.equals("1")) {
			lastMsg = "ส่งแทตทูหาคุณ";
		} else if (type.equals("2")) {
			lastMsg = "ส่งไฟล์หาคุณ";
		} else if (type.equals("3")) {
			lastMsg = "ส่งวิดิโอหาคุณ";
		} else if (type.equals("6")) {
			lastMsg = "ส่งโลเกชั่นหาคุณ";
		} else if (type.equals("7")) {
			lastMsg = "ส่งคอนแทคเพื่อนหาคุณ";
		}

		if (lastMsgObj.getSTATUS().equals("0")) {
			holder.unreaded.setVisibility(View.GONE);
			
		} else {
			holder.unreaded.setVisibility(View.VISIBLE);
			int count = DataUser.countN;
			count++;
			DataUser.VM_CHAT_N = count;
		}
		holder.textUser.setText(model.getUSERFIRSTNAME() + " "
				+ model.getUSERLASTNAME());
		holder.textChat.setText(lastMsg);
		aq.id(holder.thumeProfile).image(
				Link.LINK_PHOTO + model.getFRIENDIMAGE(), true, true);

		return view;
	}

	class Holder {
		ImageView thumeProfile;
		TextView textUser;
		TextView textChat;
		TextView unreaded;
	}

}
