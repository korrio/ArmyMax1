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
import com.ame.armymax.model.DataPeople;
import com.squareup.picasso.Picasso;

public final class PeopleListViewAdapter extends ArrayAdapter<DataPeople> {
	private final Context context;
	private List<DataPeople> peopleList;

	public PeopleListViewAdapter(List<DataPeople> peopleList,Context context) {
		super(context,R.layout.sample_comment_item);
		this.context = context;
		this.peopleList = peopleList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// SquaredImageView view = (SquaredImageView) convertView;

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.sample_comment_item, parent, false);
			holder = new ViewHolder();
			// holder.image.setScaleType(CENTER_CROP);
			holder.image = (ImageView) convertView.findViewById(R.id.tb);
			holder.text = (TextView) convertView.findViewById(R.id.statusName);
			holder.ago = (TextView) convertView.findViewById(R.id.ago);
			holder.desc = (TextView) convertView.findViewById(R.id.desc);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DataPeople ppl = peopleList.get(position);
		// Get the image URL for the current position.
		String url = ppl.getAvatar();
		String name = ppl.getName();
		String username = ppl.getUsername();

		holder.text.setText(name);
		holder.ago.setVisibility(View.GONE);
		holder.desc.setText(username);

		Picasso.with(context).load(url).placeholder(R.drawable.avatar_placeholder)
				.error(R.drawable.avatar_placeholder)
				.resizeDimen(R.dimen.grid_image_size, R.dimen.grid_image_size)
				.into(holder.image);

		return convertView;
	}

	@Override
	public int getCount() {
		return peopleList.size();
	}

	@Override
	public DataPeople getItem(int position) {
		return peopleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView image;
		TextView text;
		TextView ago;
		TextView desc;
	}
}
