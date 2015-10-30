package com.ame.armymax.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataFriend;
import com.squareup.picasso.Picasso;

public final class FriendGridViewAdapter extends BaseAdapter {
	private final Context context;
	private final List<String> urls;
	private final List<String> names;

	public FriendGridViewAdapter(Context context) {
		this.context = context;

		// Ensure we get a different ordering of images on each run.
		urls = DataFriend.getURLS();
		names = DataFriend.getNAMES();
		Collections.addAll(urls);
		// Collections.addAll(urls, DataFriend.URLS);
		// Collections.shuffle(urls);

		// Triple up the list.
		// ArrayList<String> copy = new ArrayList<String>(urls);
		// urls.addAll(copy);
		// urls.addAll(copy);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// SquaredImageView view = (SquaredImageView) convertView;

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_people_item, parent, false);
			holder = new ViewHolder();
			// holder.image.setScaleType(CENTER_CROP);
			holder.image = (ImageView) convertView.findViewById(R.id.profile_avatar);
			holder.text = (TextView) convertView.findViewById(R.id.url);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Get the image URL for the current position.
		String url = DataFriend.getUrl(position);
		String name = DataFriend.getName(position);

		holder.text.setText(name);

		// Trigger the download of the URL asynchronously into the image view.
		Picasso.with(context).load(url).placeholder(R.drawable.avatar_placeholder)
				.error(R.drawable.error)
				.resizeDimen(R.dimen.grid_image_size, R.dimen.grid_image_size)
				.into(holder.image);

		/*
		 * if (view == null) { view = new SquaredImageView(context);
		 * view.setScaleType(CENTER_CROP); }
		 * 
		 * // Get the image URL for the current position. String url =
		 * getItem(position);
		 * 
		 * // Trigger the download of the URL asynchronously into the image
		 * view. Picasso.with(context) // .load(url) //
		 * .placeholder(R.drawable.placeholder) // .error(R.drawable.error) //
		 * .fit() // .into(view);
		 */

		return convertView;
	}

	@Override
	public int getCount() {
		return urls.size();
	}

	@Override
	public String getItem(int position) {
		return urls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView image;
		TextView text;
	}
}
