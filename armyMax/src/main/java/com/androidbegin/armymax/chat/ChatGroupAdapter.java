package com.androidbegin.armymax.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.androidquery.AQuery;

public class ChatGroupAdapter extends BaseAdapter{
	private Context context;
	private View view;
	private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();
	private ArrayList<FriendContentModel> arrFriends;
	
	public ChatGroupAdapter(Context context , ArrayList<FriendContentModel> arrFriends) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arrFriends = arrFriends;
		for(int i = 0; i < arrFriends.size(); i++){
			myChecked.put(i, false);
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrFriends.size();
	}
	public void setSelectToggle(int position){
		if(myChecked.get(position)){
			myChecked.put(position, false);
		}else{
			myChecked.put(position, true);
		}
		notifyDataSetChanged();
	}
	public List<Integer> getCheckedItemPositions(){
		List<Integer> checkedItemPositions = new ArrayList<Integer>();
		
		for(int i = 0; i < myChecked.size(); i++){
			if (myChecked.get(i)){
				(checkedItemPositions).add(i);
			}
		}
		return checkedItemPositions;
	}
	public List<String> getCheckedItems(){
		List<String> checkedItems = new ArrayList<String>();
		
		for(int i = 0; i < myChecked.size(); i++){
			if (myChecked.get(i)){
				(checkedItems).add(arrFriends.get(i).getUserID());  //Add FriendId To List Select
			}
		}
		
		return checkedItems;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			view = inflater.inflate(R.layout.chat_layout_selectmulti_item, null);
			holder.aq = new AQuery(context);
			holder.textName = (TextView)view.findViewById(R.id.textName);
			holder.thumeProfile = (ImageView)view.findViewById(R.id.imageThume);
			holder.checkBokSelect = (CheckBox)view.findViewById(R.id.checkBokSelect);
			
			view.setTag(holder);
		}
		else {
			holder =  (Holder) convertView.getTag();
		}
		
		FriendContentModel model = arrFriends.get(position);
		holder.textName.setText(model.getUserName());
		holder.aq.id(holder.thumeProfile).image(Link.LINK_PHOTO+model.getUserAvatarPath(),true , true);
		
		//--------- Check And Set CheckBox
		Boolean checked = myChecked.get(position);
		if (checked != null) {
			holder.checkBokSelect.setChecked(checked);
        }
		
		
		
		return view;
	}
	class Holder {
		AQuery aq;
		ImageView thumeProfile;
		TextView textName;
		CheckBox checkBokSelect;
	}

}
