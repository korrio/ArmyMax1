package com.ame.armymax.adapter;

import java.util.ArrayList;
import java.util.Collections;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataFeedLive;
import com.squareup.picasso.Picasso;

public class FeedLiveListViewAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<String> names;
	int type;

	public FeedLiveListViewAdapter(Context context, int type) {
		this.context = context;
		this.type = type;
		names = DataFeedLive.getNames();
		Collections.addAll(names);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.feed_item, parent, false);
			holder = new ViewHolder();

			holder.avatar = (ImageView) view.findViewById(R.id.tb);
			holder.name = (TextView) view.findViewById(R.id.statusName);
			holder.ago = (TextView) view.findViewById(R.id.ago);
			holder.status = (TextView) view.findViewById(R.id.desc);
			holder.contentTb = (ImageView) view.findViewById(R.id.content_tb);
			holder.contentName = (TextView) view
					.findViewById(R.id.content_name);
			holder.contentDesc = (TextView) view
					.findViewById(R.id.content_desc);
			holder.contentMeta = (TextView) view
					.findViewById(R.id.content_meta);
			holder.loveCount = (TextView) view.findViewById(R.id.love_count);
			holder.commentCount = (TextView) view
					.findViewById(R.id.comment_count);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String tbUrl = "";
		String name = "";
		String ago = "";
		String status = "";
		String contentTbUrl = "";
		String contentName = "";
		String contentDesc = "";
		String contentMeta = "";
		String loveCount = "";
		String commentCount = "";

		String postType = DataFeedLive.getPostType(position);
		
		PrettyTime p = new PrettyTime();

		tbUrl = DataFeedLive.getTbUrl(position);
		name = DataFeedLive.getName(position);
		ago = DataFeedLive.getAgo(position);
		status = DataFeedLive.getStatus(position);
		loveCount = DataFeedLive.getLoveCount(position);
		commentCount = DataFeedLive.getCommentCount(position);
		
		holder.name.setText(name);
		holder.ago.setText(ago);
		holder.status.setText(status);


		int type = Integer.parseInt(postType);

		switch (type) {
		case 1:
			holder.contentTb.setVisibility(View.GONE);
			holder.contentName.setVisibility(View.GONE);
			holder.contentDesc.setVisibility(View.GONE);
			holder.contentMeta.setVisibility(View.GONE);
			break;
		case 2:
			contentTbUrl = DataFeedLive.getContentTbUrl(position);
			contentName = DataFeedLive.getContentName(position);
			contentDesc = DataFeedLive.getContentDesc(position);
			contentMeta = DataFeedLive.getContentMeta(position);
			break;
		case 3:
			contentTbUrl = DataFeedLive.getContentTbUrl(position);
			contentName = DataFeedLive.getContentName(position);
			contentDesc = DataFeedLive.getContentDesc(position);
			contentMeta = DataFeedLive.getContentMeta(position);
			break;
		case 4:
			contentTbUrl = DataFeedLive.getContentTbUrl(position);
			contentName = DataFeedLive.getContentName(position);
			contentDesc = DataFeedLive.getContentDesc(position);
			contentMeta = DataFeedLive.getContentMeta(position);
			break;
		default:
			break;

		}

		if(contentDesc.equals(""))
			holder.contentDesc.setVisibility(View.GONE);
		if(contentMeta.equals(""))
			holder.contentMeta.setVisibility(View.GONE);
		
		if (!tbUrl.equals("") && tbUrl != null) {
			Picasso.with(context).load(tbUrl)
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
					.into(holder.avatar);
		} 

		
		if (!contentTbUrl.equals("") && contentTbUrl != null) {
			Picasso.with(context)
					.load(contentTbUrl)
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.resizeDimen(R.dimen.content_tb,
							R.dimen.content_tb_height)
							.centerInside()
					.into(holder.contentTb);
		} else {
			holder.contentTb.setVisibility(View.GONE);
		}

		holder.contentName.setText(contentName);
		if (contentDesc.length() >= 100)
			holder.contentDesc.setText(contentDesc.substring(0, 100) + " ...");
		else
			holder.contentDesc.setText(contentDesc);
		holder.contentMeta.setText(contentMeta);
		holder.loveCount.setText(loveCount);
		holder.commentCount.setText(commentCount);

		return view;
	}

	@Override
	public int getCount() {
		return names.size();
	}

	@Override
	public String[] getItem(int position) {
		String[] foo = new String[3];
		
		//foo[0] = DataFeedLive.getVideoSource(position);
		//foo[1] = DataFeedLive.getContentName(position);
		//foo[2] = DataFeedLive.getContentTbUrl(position);
		return foo;
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
	}

}
