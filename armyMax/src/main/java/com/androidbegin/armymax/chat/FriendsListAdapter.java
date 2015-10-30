package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.androidquery.AQuery;

public class FriendsListAdapter extends ArrayAdapter<FrienListModel> {

	private Context context;
	private View view;
	private ArrayList<FriendContentModel> arrFriends;
	private ArrayList<FriendContentModel> arrFriends_original;
	private FriendFilter filter;
	private AQuery aq;

	ArrayList<ChatGroupModel> arrChatGroup;

	private ArrayList<FrienListModel> arrData = new ArrayList<FrienListModel>();
	private ArrayList<FrienListModel> arrData_original = new ArrayList<FrienListModel>();

	// public FriendsListAdapter(Context context , ArrayList<FriendContentModel>
	// arrFriends) {
	// // TODO Auto-generated constructor stub
	// this.context = context;
	// this.arrFriends = new ArrayList<FriendContentModel>(arrFriends);
	// this.arrFriends_original = new ArrayList<FriendContentModel>(arrFriends);
	// }
	public FriendsListAdapter(Context context, int resource,
			ArrayList<FriendContentModel> arrFriends,
			ArrayList<ChatGroupModel> arrChatGroup) {
		super(context, resource);
		this.context = context;
		this.arrFriends = new ArrayList<FriendContentModel>(arrFriends);
		// this.arrGroup = new ArrayList<FriendContentModel>(arrFriends);
		this.arrFriends_original = new ArrayList<FriendContentModel>(arrFriends);
		this.arrChatGroup = arrChatGroup;
		filter = new FriendFilter();
		aq = new AQuery(context);

		manageArrayAdapter();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrData.size();
	}

	void manageArrayAdapter() {
		arrData.add(new SectionListFrienModel("Friend"));
		for (int i = 0; i < arrFriends.size(); i++) {
			arrData.add(new EnityListModel(arrFriends.get(i), null, false));
		}
		arrData.add(new SectionListFrienModel("Group"));
		for (int i = 0; i < arrChatGroup.size(); i++) {
			arrData.add(new EnityListModel(null, arrChatGroup.get(i), true));
		}
		arrData_original = arrData;
	}

	public FrienListModel getItem(int position) {
		// TODO Auto-generated method stub
		EnityListModel arr = (EnityListModel) arrData.get(position);

		// if (arr.isChatgroup()) {
		// return arr.getFriendContent();
		// }
		// else {
		// return arr.getFriendContent();
		// }
		return null;
	}

	public FriendContentModel getData(int position,
			OngetItemFriendListener callback) {
		// TODO Auto-generated method stub
		EnityListModel arr = (EnityListModel) arrData.get(position);
		// System.out.println("arrData.size() : "+arrData.size());
		if (arr.isChatgroup()) {
			callback.isChatGroup(arr.getChatGroup());
			return arr.getFriendContent();
		} else {
			callback.isChat(arr.getFriendContent());
			return arr.getFriendContent();
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FrienListModel modelData = arrData.get(position);
		if (modelData.isSection()) {
			SectionListFrienModel section = (SectionListFrienModel) modelData;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.chat_layout_list_friends_section,
					null);
			TextView textSection = (TextView) view
					.findViewById(R.id.textSection);
			view.setOnClickListener(null);
			view.setOnLongClickListener(null);
			view.setLongClickable(false);
			textSection.setText(section.getTxtSection());
		} else {
			EnityListModel enity = (EnityListModel) modelData;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.chat_layout_list_friends_item,
					null);
			TextView textUser = (TextView) view.findViewById(R.id.textUsername);
			ImageView thumeProfile = (ImageView) view
					.findViewById(R.id.imageProfile);

			if (!enity.isChatgroup()) {
				textUser.setText(enity.getFriendContent().getUserFirstName()
						+ " " + enity.getFriendContent().getUserLastName() );
				aq.id(thumeProfile).image(
						Link.LINK_PHOTO
								+ enity.getFriendContent().getUserAvatarPath(),
						true, true);
			} else {
				textUser.setText(enity.getChatGroup().getName());
				// aq.id(thumeProfile).image(Link.LINK_PHOTO+enity.getChatGroup().getName()
				// , true , true);
			}

		}
		// view = convertView;
		// Holder holder;
		// if (view == null) {
		// holder = new Holder();
		// LayoutInflater inflater = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// view = inflater.inflate(R.layout.layout_list_friends_item, null);
		// holder.textUser = (TextView)view.findViewById(R.id.textUsername);
		// holder.thumeProfile =
		// (ImageView)view.findViewById(R.id.imageProfile);
		// view.setTag(holder);
		// }
		// else {
		// holder = (Holder) convertView.getTag();
		// }
		//
		// FriendContentModel model = arrFriends.get(position);
		// holder.textUser.setText(model.getUserName());
		// aq.id(holder.thumeProfile).image(Link.LINK_PHOTO+model.getUserAvatarPath(),
		// true, true);

		return view;
	}

	class Holder {
		ImageView thumeProfile;
		TextView textUser;
	}

	@Override
	public Filter getFilter() {

		if (filter == null) {
			filter = new FriendFilter();
		}
		return filter;
	}

	private class FriendFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			String prefix = constraint.toString().toLowerCase();

			if (prefix == null || prefix.length() == 0) {
				ArrayList<FrienListModel> list = new ArrayList<FrienListModel>(
						arrData_original);
				results.values = list;
				results.count = list.size();
			} else {
				final ArrayList<FrienListModel> list = new ArrayList<FrienListModel>(
						arrData_original);
				final ArrayList<FrienListModel> nlist = new ArrayList<FrienListModel>();
				int count = list.size();

				for (int i = 0; i < count; i++) {
					if (!list.get(i).isSection()) {
						EnityListModel f = (EnityListModel) list.get(i);
						if (!f.isChatgroup()) {
							final FriendContentModel Friend = f
									.getFriendContent();
							final String value = Friend.getUserName()
									.toLowerCase();

							if (value.contains(prefix)) {
								nlist.add(f);
							}
							results.values = nlist;
							results.count = nlist.size();
						} else {
							final ChatGroupModel Friend = f.getChatGroup();
							final String value = Friend.getName().toLowerCase();

							if (value.contains(prefix)) {
								nlist.add(f);
							}
							results.values = nlist;
							results.count = nlist.size();
						}

					}

				}
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			arrData = (ArrayList<FrienListModel>) results.values;
			notifyDataSetChanged();
			clear();
			int count = arrData.size();
			for (int i = 0; i < count; i++) {
				add(arrData.get(i));
				notifyDataSetInvalidated();
			}
		}
	}

}
