package com.ame.armymax.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataNoti;
import com.androidquery.simplefeed.ui.BadgeView;

public class NotiAdapter extends ArrayAdapter<DataNoti> {
	Context context;

	public static int TYPES_likeFeed = 100;
	public static int TYPES_commentFeed = 101;
	public static int TYPES_liveNow = 200;
	public static int TYPES_followedYou = 300;
	public static int TYPES_chatMessage = 500;
	public static int TYPES_chatSticker = 501;
	public static int TYPES_chatFile = 502;

	public static int TYPES_chatLocation = 503;
	public static int TYPES_chatFreeCall = 504;
	public static int TYPES_chatVideoCall = 505;
	public static int TYPES_chatInviteGroup = 506;
	public static int TYPES_confInvite = 600;
	public static int TYPES_confCreate = 601;
	public static int TYPES_confJoin = 602;

	public ArrayList<DataNoti> notiList = new ArrayList<DataNoti>();

	public NotiAdapter(ArrayList<DataNoti> notiList, Context context) {
		super(context, R.layout.sample_noti_item);
		this.notiList = notiList;
		this.context = context;
	}

	@Override
	public View getView(int i, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.sample_noti_item, parent, false);
			holder = new ViewHolder();

			// holder.name = (TextView) view.findViewById(R.id.statusName);
			holder.ago = (TextView) view.findViewById(R.id.ago);
			holder.status = (TextView) view.findViewById(R.id.statusTextDetail);
			holder.unreaded = (TextView) view.findViewById(R.id.unreaded);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		DataNoti noti = notiList.get(i);
		String notiType = noti.getType();
		
		if(noti.getReaded()) {
			holder.unreaded.setVisibility(View.GONE);
		} else {
			holder.unreaded.setText("ใหม่!");
		}
		
		int type = Integer.parseInt(notiType);

		Spanned span = null;
		if (type == TYPES_likeFeed)
			span = Html.fromHtml("<p><b>" + noti.getMyName()
					+ "</b> love your post</p>");
		if (type == TYPES_commentFeed)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> comment on <b>" + noti.getMyName()
					+ "</b>'s post</p>");
		if (type == TYPES_liveNow)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> live now</p>");
		if (type == TYPES_followedYou)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> follow you</p>");
		if (type == TYPES_chatMessage)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> sent you a message</p>");
		if (type == TYPES_chatSticker)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> sent you a Tattoo</p>");
		if (type == TYPES_chatFile)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> sent you a photo</p>");
		if (type == TYPES_chatLocation)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> sent you a location</p>");

		if (type == TYPES_chatFreeCall)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> request an audio call</p>");

		if (type == TYPES_chatVideoCall)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> request a video call</p>");

		if (type == TYPES_chatInviteGroup)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> invite to a groupchat</p>");

		if (type == TYPES_confInvite)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> invite to a conference room</p>");

		if (type == TYPES_confCreate)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> create a conference room");

		if (type == TYPES_confJoin)
			span = Html.fromHtml("<p><b>" + noti.getFromName()
					+ "</b> join a conference room</p>");

		if (type == 0) {
			span = Html.fromHtml("<p><center><b>" + noti.getMessage()
					+ "</b></center></p>");
		}

		holder.status.setText(span);
		holder.ago.setText(noti.getAgo());
		
		
		
		/*

		holder.text = (TextView) view.findViewById(android.R.id.text1);
		holder.badge = new BadgeView(context, holder.text);
		
		//if(!noti.getReaded()) {
			
			holder.badge.setBadgeBackgroundColor(Color.RED);
			holder.badge.setTextColor(Color.WHITE);
			holder.badge.setText("!");
			holder.badge.show();
		//} else {
			//holder.badge.hide();
		//}

		 */
		
		
		

		return view;
	}

	@Override
	public int getCount() {
		return notiList.size();
	}

	public DataNoti getItem(int position) {
		return notiList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView avatar;
		TextView name;
		TextView ago;
		TextView status;
		ImageView contentTb;
		TextView contentName;
		TextView contentDesc;
		TextView contentMeta;
		TextView loveCount;
		TextView commentCount;
		TextView unreaded;
	}

}
