package com.ame.armymax.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataComment;
import com.androidquery.simplefeed.util.CircleTransform;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends ArrayAdapter<DataComment> {
	private final Context context;
	private List<DataComment> commentList;

	public CommentAdapter(List<DataComment> commentList, Context ctx) {
		super(ctx, R.layout.sample_comment_item);
		this.commentList = commentList;
		this.context = ctx;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {

			view = LayoutInflater.from(context).inflate(
					R.layout.sample_comment_item, parent, false);
			holder = new ViewHolder();

			holder.avatar = (ImageView) view.findViewById(R.id.tb);
			holder.name = (TextView) view.findViewById(R.id.statusName);
			holder.ago = (TextView) view.findViewById(R.id.ago);
			holder.commentText = (TextView) view.findViewById(R.id.desc);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		DataComment p = commentList.get(position);

		String tbUrl = p.getTbUrl();
		String name = p.getName();
		String ago = p.getAgo();
		String commentText = p.getCommentText();

		holder.name.setText(name);
		holder.ago.setText(ago);
		holder.commentText.setText(commentText);

		Picasso.with(context).load(tbUrl).placeholder(R.drawable.placeholder)
				.error(R.drawable.error)
				.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
				.transform(new CircleTransform())
				.into(holder.avatar);

		return view;
	}

	public void resetData() {
		commentList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	public DataComment getItem(int position) {
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return commentList.get(position).hashCode();
	}
	
	public void updateCommentList(List<DataComment> newlist) {
		commentList.clear();
		commentList.addAll(newlist);
	    this.notifyDataSetChanged();
	}

	static class ViewHolder {
		ImageView avatar;
		TextView name;
		TextView ago;
		TextView commentText;
	}

}
