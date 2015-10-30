/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.ame.armymax.model.DataFeedEverything;
import com.androidquery.AQuery;
import com.squareup.picasso.Picasso;

public class VideoAdapter extends ArrayAdapter<DataFeedEverything> {

	private Context context;
	private List<DataFeedEverything> videoList;
	private List<DataFeedEverything> origvideoList;
	AQuery aq;

	public VideoAdapter(List<DataFeedEverything> videoList, Context ctx) {
		super(ctx, R.layout.activity_chat_item, videoList);
		this.videoList = videoList;
		this.context = ctx;
		this.origvideoList = videoList;
		aq = new AQuery(ctx);
	}

	public int getCount() {
		return videoList.size();
	}

	public DataFeedEverything getItem(int position) {
		return videoList.get(position);
	}

	public long getItemId(int position) {
		return videoList.get(position).hashCode();
	}

	public View getView(int i, View convertView, ViewGroup parent) {
		View view = convertView;

		VideoHolder holder = new VideoHolder();

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.feed_item, null);

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
		} else
			holder = (VideoHolder) view.getTag();

		DataFeedEverything p = videoList.get(i);

		Picasso.with(context).load(p.getTbUrl()).placeholder(R.drawable.placeholder)
				.error(R.drawable.error)
				.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
				.into(holder.avatar);

		holder.status.setText(p.getStatus());
		holder.name.setText(p.getName());
		holder.ago.setText(p.getAgo());

		if (p.getContentDesc().equals("") || p.getContentDesc().equals("-")
				|| p.getContentDesc().equals("null") || p.getContentDesc() == null)
			holder.contentDesc.setVisibility(View.GONE);

		if (p.getContentTbUrl().equals("")) {
			holder.contentTb.setVisibility(View.GONE);
		} else {
			Picasso.with(context)
					.load(p.getContentTbUrl())
					.placeholder(R.drawable.placeholder)
					.error(R.drawable.error)
					.resizeDimen(R.dimen.content_tb,
							R.dimen.content_tb_height).centerCrop()
					.into(holder.contentTb);
		}

		holder.contentName.setText(p.getContentName());
		if (p.getContentDesc().length() >= 100)
			holder.contentDesc.setText(p.getContentDesc().substring(0, 100) + " ...");
		else
			holder.contentDesc.setText(p.getContentDesc());
		holder.contentMeta.setVisibility(View.GONE);
		// holder.contentMeta.setText(contentMeta);
		holder.loveCount.setText(p.getLoveCount());
		holder.commentCount.setText(p.getCommentCount());

		return view;
	}

	public void resetData() {
		videoList = origvideoList;
	}

	/* *********************************
	 * We use the holder pattern It makes the view faster and avoid finding the
	 * component *********************************
	 */

	private static class VideoHolder {
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
