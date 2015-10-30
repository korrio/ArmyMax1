package com.ame.armymax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataFeedFollower;
import com.ame.armymax.model.DataFeedFollowing;
import com.ame.armymax.model.DataFeedMe;
import com.ame.armymax.model.DataFeedSocial;
import com.squareup.picasso.Picasso;

public class FeedVideoListViewAdapter extends BaseAdapter {
	private final Context context;
	int type;

	public FeedVideoListViewAdapter(Context context, int type) {
		this.context = context;
		this.type = type;
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
	
		switch (type) {
		case 1:
			tbUrl = DataFeedMe.getTbUrl(position);
			name = DataFeedMe.getName(position);
			ago = DataFeedMe.getAgo(position);
			status = DataFeedMe.getStatus(position);
			contentTbUrl = DataFeedMe.getContentTbUrl(position);
			contentName = DataFeedMe.getContentName(position);
			contentDesc = DataFeedMe.getContentDesc(position);
			contentMeta = DataFeedMe.getContentMeta(position);
			loveCount = DataFeedMe.getLoveCount(position);
			commentCount = DataFeedMe.getCommentCount(position);
			break;
		case 2:
			tbUrl = DataFeedSocial.getTbUrl(position);
			name = DataFeedSocial.getName(position);
			ago = DataFeedSocial.getAgo(position);
			status = DataFeedSocial.getStatus(position);
			contentTbUrl = DataFeedSocial.getContentTbUrl(position);
			contentName = DataFeedSocial.getContentName(position);
			contentDesc = DataFeedSocial.getContentDesc(position);
			contentMeta = DataFeedSocial.getContentMeta(position);
			loveCount = DataFeedSocial.getLoveCount(position);
			commentCount = DataFeedSocial.getCommentCount(position);
			break;
		case 3:
			tbUrl = DataFeedFollowing.getTbUrl(position);
			name = DataFeedFollowing.getName(position);
			ago = DataFeedFollowing.getAgo(position);
			status = DataFeedFollowing.getStatus(position);
			contentTbUrl = DataFeedFollowing.getContentTbUrl(position);
			contentName = DataFeedFollowing.getContentName(position);
			contentDesc = DataFeedFollowing.getContentDesc(position);
			contentMeta = DataFeedFollowing.getContentMeta(position);
			loveCount = DataFeedFollowing.getLoveCount(position);
			commentCount = DataFeedFollowing.getCommentCount(position);
			break;
		case 4:
			tbUrl = DataFeedFollower.getTbUrl(position);
			name = DataFeedFollower.getName(position);
			ago = DataFeedFollower.getAgo(position);
			status = DataFeedFollower.getStatus(position);
			contentTbUrl = DataFeedFollower.getContentTbUrl(position);
			contentName = DataFeedFollower.getContentName(position);
			contentDesc = DataFeedFollower.getContentDesc(position);
			contentMeta = DataFeedFollower.getContentMeta(position);
			loveCount = DataFeedFollower.getLoveCount(position);
			commentCount = DataFeedFollower.getCommentCount(position);
			break;
		default:
			break;

		}

		// Get the image URL for the current position.

		Picasso.with(context).load(tbUrl).placeholder(R.drawable.placeholder)
				.error(R.drawable.error)
				.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
				.into(holder.avatar);

		holder.name.setText(name);
		holder.ago.setText(ago);
		
		if(contentMeta.equals("fileupload"))
			holder.status.setText(contentName);
		else
			holder.status.setVisibility(View.GONE);

		if(contentDesc.equals("") || contentDesc.equals("-") || contentDesc.equals("null") || contentDesc == null)
			holder.contentDesc.setVisibility(View.GONE);

		if (contentTbUrl.equals("") || contentTbUrl.equals("-") || contentTbUrl.equals("null") || contentTbUrl == null) {
			holder.contentTb.setVisibility(View.GONE);
		} else {
		Picasso.with(context)
				.load(contentTbUrl)
				.placeholder(R.drawable.placeholder)
				.error(R.drawable.error)
				.resizeDimen(R.dimen.content_tb,
						R.dimen.content_tb_height).centerCrop()
				.into(holder.contentTb);
		}

		holder.contentName.setText(contentName);
		if (contentDesc.length() >= 100)
			holder.contentDesc.setText(contentDesc.substring(0, 100) + " ...");
		else
			holder.contentDesc.setText(contentDesc);
		holder.contentMeta.setVisibility(View.GONE);
		//holder.contentMeta.setText(contentMeta);
		holder.loveCount.setText(loveCount);
		holder.commentCount.setText(commentCount);

		return view;
	}

	@Override
	public int getCount() {
		return DataFeedMe.getNames().size();
	}

	@Override
	public String[] getItem(int position) {
		String[] foo = new String[3];
		foo[0] = DataFeedMe.getVideoSource(position);
		foo[1] = DataFeedMe.getContentName(position);
		foo[2] = DataFeedMe.getContentTbUrl(position);
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
