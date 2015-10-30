package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.androidquery.AQuery;

public class SeachFriendListAdapter extends ArrayAdapter<FriendSearchModel> {

	


	private Context context;
	private View view;
	private ArrayList<FriendSearchModel> arrFriends;
//	private ArrayList<FriendSearchModel> arrFriends_original;
//	private FriendFilter filter ;
	private AQuery aq  ;
	
//	public FriendsListAdapter(Context context , ArrayList<FriendContentModel> arrFriends) {
//		// TODO Auto-generated constructor stub
//		this.context 			 = context;
//		this.arrFriends 		 = new ArrayList<FriendContentModel>(arrFriends);
//		this.arrFriends_original = new ArrayList<FriendContentModel>(arrFriends);
//	}
	public SeachFriendListAdapter(Context context, int resource,
			ArrayList<FriendSearchModel> arrFriends) {
		super(context, resource, arrFriends);
		this.context 			 = context;
		this.arrFriends 		 = new ArrayList<FriendSearchModel>(arrFriends);
//		this.arrFriends_original = new ArrayList<FriendSearchModel>(arrFriends);
//		filter = new FriendFilter();
		aq = new AQuery(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrFriends.size();
	}

	@Override
	public FriendSearchModel getItem(int position) {
		// TODO Auto-generated method stub
		return arrFriends.get(position);
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
		final Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			view = inflater.inflate(R.layout.chat_layout_list_friends_search_item, null);
			holder.textUser = (TextView)view.findViewById(R.id.textUsername);
			holder.thumeProfile = (ImageView)view.findViewById(R.id.imageProfile);
			holder.btAddFriend = (Button)view.findViewById(R.id.btAddFriend);
			view.setTag(holder);
		}
		else {
			holder =  (Holder) convertView.getTag();
		}
		
		FriendSearchModel model = arrFriends.get(position);
		holder.textUser.setText(model.getUserName());
		aq.id(holder.thumeProfile).image(Link.LINK_PHOTO+model.getUserAvatarPath(), true, true);
		
		holder.btAddFriend.setTag(position);
		holder.btAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//System.out.println(.toString());
				final int positionClick = Integer.parseInt(holder.btAddFriend.getTag().toString());
				TaskAddFriend taskAddFriend = new TaskAddFriend(context,arrFriends.get(positionClick).getUserID(), new OnAddfriendListener() {
					
					@Override
					public void addFriendSuccess() {
						// TODO Auto-generated method stub
						arrFriends.remove(positionClick);
						notifyDataSetChanged();
						Toast.makeText(context, "เพิ่มเพื่อนสำเร็จ ...", Toast.LENGTH_SHORT).show();
						//System.out.println("SUCCESS+++++++");
					}
					
					@Override
					public void addFriendFailed() {
						// TODO Auto-generated method stub
						Toast.makeText(context, "เป็นเพื่อนกันแล้ว ...", Toast.LENGTH_SHORT).show();
					}
				});
				taskAddFriend.execute();
			}
		});
		
		return view;
	}
	class Holder{
		ImageView thumeProfile;
		TextView textUser;
		Button btAddFriend;
	}
	
//	@Override
//	public Filter getFilter(){
//
//	    if(filter == null){
//	        filter = new FriendFilter();
//	    }
//	    return filter;
//	}
//	
//	private class FriendFilter extends Filter{
//	    @Override
//	    protected FilterResults performFiltering(CharSequence constraint){
//	        FilterResults results = new FilterResults();
//	        String prefix = constraint.toString().toLowerCase();
//
//	        if (prefix == null || prefix.length() == 0){
//	        	ArrayList<FriendContentModel> list = new ArrayList<FriendContentModel>(arrFriends_original);
//	        	results.values = list;
//	            results.count = list.size();
//	        }else{
//	            final ArrayList<FriendContentModel> list = new ArrayList<FriendContentModel>(arrFriends_original);
//	            final ArrayList<FriendContentModel> nlist = new ArrayList<FriendContentModel>();
//	            int count = list.size();
//
//	            for (int i = 0; i<count; i++){
//	                final FriendContentModel Friend = list.get(i);
//	                final String value = Friend.getUserName().toLowerCase();
//
//	                if(value.contains(prefix)){
//	                    nlist.add(Friend);
//	                }
//	                results.values = nlist;
//	                results.count = nlist.size();
//	            }
//	        }
//	        return results;
//	    }
//
//	    @SuppressWarnings("unchecked")
//	    @Override
//	    protected void publishResults(CharSequence constraint, FilterResults results) {
//	        arrFriends = (ArrayList<FriendContentModel>)results.values;
//	        notifyDataSetChanged();
//	        clear();
//	        int count = arrFriends.size();
//	        for(int i = 0; i<count; i++){
//	            add(arrFriends.get(i));
//	            notifyDataSetInvalidated();
//	        }
//	    }
//	}
	
	

}
