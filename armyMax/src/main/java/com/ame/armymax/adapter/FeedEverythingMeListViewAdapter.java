package com.ame.armymax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.squareup.picasso.Picasso;

public class FeedEverythingMeListViewAdapter extends BaseAdapter {
	private final Context context;
	int type;

	public FeedEverythingMeListViewAdapter(Context context, int type) {
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



		holder.name.setText(name);
		holder.ago.setText(ago);
		holder.status.setText(status);

		//int type = Integer.parseInt(postType);



		if (status.equals("") || status == null) {
			holder.status.setVisibility(View.GONE);
		} 
		
		if (contentMeta.equals("") || contentMeta == null)
			holder.contentMeta.setVisibility(View.GONE);

		if (!tbUrl.equals("") && tbUrl != null) {
			Picasso.with(context).load(tbUrl)
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
					.into(holder.avatar);
		}

		if (contentTbUrl.equals("") || contentTbUrl.equals("-")
				|| contentTbUrl.equals("null") || contentTbUrl == null) {
			holder.contentTb.setVisibility(View.GONE);
		} else {
			Picasso.with(context)
					.load(contentTbUrl)
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.resizeDimen(R.dimen.content_tb,
							R.dimen.content_tb_height).centerInside()
					.into(holder.contentTb);
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
		return 0;
	}

	@Override
	public String[] getItem(int position) {
		String[] foo = new String[3];
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
