package com.ame.armymax.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataFeedEverything;
import com.androidquery.AQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EverythingAdapter extends ArrayAdapter<DataFeedEverything>
		implements OnClickListener {
	private final Context context;
	private List<DataFeedEverything> feedList;
	private List<DataFeedEverything> origfeedList;
	int type;
	AQuery aq;

	public EverythingAdapter(List<DataFeedEverything> feedList, Context ctx) {
		super(ctx, R.layout.feed_item, feedList);
		this.feedList = feedList;
		this.context = ctx;
		this.origfeedList = feedList;
		aq = new AQuery(ctx);
	}

	@Override
	public View getView(int i, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.feed_item,
					parent, false);
			holder = new ViewHolder();

			holder.avatar = (ImageView) view.findViewById(R.id.ImageView01);
			holder.name = (TextView) view.findViewById(R.id.statusName);
			holder.ago = (TextView) view.findViewById(R.id.ago);
			holder.status = (TextView) view.findViewById(R.id.desc);
			holder.contentTb = (ImageView) view
					.findViewById(R.id.content_tb);
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
		DataFeedEverything p = feedList.get(i);

		String postId = p.getPostId();
		String postType = p.getPostType();
		String tbUrl = p.getTbUrl();
		String name = p.getName();
		String ago = p.getAgo();
		String status = p.getStatus();
		String loveCount = p.getLoveCount();
		String commentCount = p.getCommentCount();

		holder.name.setText(name);
		holder.ago.setText(ago);
		holder.status.setText(status);
		if (!tbUrl.equals("") && tbUrl != null) {
			Picasso.with(context).load(tbUrl)
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.into(holder.avatar);
					//.transform(new CircleTransform()).into(holder.avatar);
		}

		holder.name.setTag("name");
		holder.ago.setTag("ago");
		holder.avatar.setTag("avatar");

		int type = Integer.parseInt(postType);

		final String contentTbUrl = p.getContentTbUrl();
		String contentName = p.getContentName();
		String contentDesc = p.getContentDesc();
		String contentMeta = p.getContentMeta();

		switch (type) {
		case 1:
			holder.contentTb.setVisibility(View.GONE);
			holder.contentName.setVisibility(View.GONE);
			holder.contentDesc.setVisibility(View.GONE);
			holder.contentMeta.setVisibility(View.GONE);
			break;
		case 2:
			Picasso.with(context).load(contentTbUrl)
			.error(R.drawable.error).into(holder.contentTb);
			holder.contentTb.setVisibility(View.VISIBLE);
			
			break;
		case 3:
			if (contentDesc.equals(""))
				holder.contentDesc.setVisibility(View.GONE);
			if (contentMeta.equals(""))
				holder.contentMeta.setVisibility(View.GONE);

            holder.contentDesc.setVisibility(View.GONE);
            holder.contentMeta.setVisibility(View.GONE);

			if (!contentTbUrl.equals("") && contentTbUrl != null) {
				Picasso.with(context).load(contentTbUrl)
				.error(R.drawable.error).into(holder.contentTb);
				holder.contentTb.setVisibility(View.VISIBLE);
				

			} else {
				holder.contentTb.setVisibility(View.GONE);
			}

			holder.contentName.setText(contentName);
			if (contentDesc.length() >= 100)
				holder.contentDesc.setText(contentDesc.substring(0, 100)
						+ " ...");
			else
				holder.contentDesc.setText(contentDesc);
			break;
		case 4:
			if (p.getContentDesc().equals("") || p.getContentDesc().equals("-")
					|| p.getContentDesc().equals("null")
					|| p.getContentDesc() == null)
				holder.contentDesc.setVisibility(View.GONE);

			if (p.getContentTbUrl().equals("")) {
				holder.contentTb.setVisibility(View.GONE);
			} else {
				Picasso.with(context).load(contentTbUrl)
				.error(R.drawable.error).into(holder.contentTb);
				
				holder.contentTb.setVisibility(View.VISIBLE);
				
			}
			break;
		default:
			break;

		}

		if (status.equals("") || status == null) {
			holder.status.setVisibility(View.GONE);
		}

		if (contentMeta.equals("") || contentMeta == null
				|| contentMeta.equals("null"))
			holder.contentMeta.setVisibility(View.GONE);

		if (contentDesc.equals("") || contentDesc == null
				|| contentDesc.equals("null"))
			holder.contentDesc.setVisibility(View.GONE);

		if (contentName.equals("") || contentName == null
				|| contentName.equals("null"))
			holder.contentName.setVisibility(View.GONE);

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

	public void resetData() {
		feedList = origfeedList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return feedList.size();
	}

	public DataFeedEverything getItem(int position) {
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return feedList.get(position).hashCode();
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

	public void append(ArrayList<DataFeedEverything> feedList2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		Log.e("Sample", "Clicked on tag: " + v.getTag());

	}

}
