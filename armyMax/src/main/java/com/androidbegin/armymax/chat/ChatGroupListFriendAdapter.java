package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.androidquery.AQuery;

public class ChatGroupListFriendAdapter extends BaseAdapter{
	private Context context;
	private View view;
	private ArrayList<ChatGroupListModel> arrData;
	public ChatGroupListFriendAdapter(Context context , ArrayList<ChatGroupListModel> arrData) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arrData = arrData;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			view = inflater.inflate(R.layout.chat_layout_list_friends_item, null);
			holder.aq = new AQuery(context);
			holder.textUser = (TextView)view.findViewById(R.id.textUsername);
			holder.thumeProfile = (ImageView)view.findViewById(R.id.imageProfile);
			view.setTag(holder);
		}
		else {
			holder =  (Holder) convertView.getTag();
		}
		holder.textUser.setText(arrData.get(position).getName());
		holder.aq.id(holder.thumeProfile).image(Link.LINK_PHOTO+arrData.get(position).getUser_avatar(),true , true);
		return view;
	}
	class Holder{
		ImageView thumeProfile;
		TextView textUser;
		AQuery aq;
	}

}
