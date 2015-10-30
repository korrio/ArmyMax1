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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataChatFriend;
import com.androidquery.AQuery;
import com.squareup.picasso.Picasso;

public class ChatFriendAdapter extends ArrayAdapter<DataChatFriend> implements Filterable {

	private List<DataChatFriend> planetList;
	private Context context;
	private Filter planetFilter;
	private List<DataChatFriend> origPlanetList;
	AQuery aq;
	
	public ChatFriendAdapter(List<DataChatFriend> planetList, Context ctx) {
		super(ctx, R.layout.activity_chat_item, planetList);
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
		aq = new AQuery(ctx);
	}
	
	public int getCount() {
		return planetList.size();
	}

	public DataChatFriend getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.activity_chat_item, null);
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.name);
			TextView distView = (TextView) v.findViewById(R.id.dist);
			ImageView avatar = (ImageView) v.findViewById(R.id.img);
			
			holder.planetNameView = tv;
			holder.distView = distView;
			holder.avatar = avatar;
			
			v.setTag(holder);
		}
		else 
			holder = (PlanetHolder) v.getTag();
		
		DataChatFriend p = planetList.get(position);
		holder.planetNameView.setText(p.getName());
		holder.distView.setText(p.getDesc());
		//holder.avatar
		
		//aq.id(holder.avatar).image(p.getImgUrl(),true,true,60,0);
		
		Picasso.with(context).load(p.getImgUrl())
		.placeholder(R.drawable.chat_avatar_placeholder)
		.error(R.drawable.chat_avatar_placeholder)
		.resizeDimen(R.dimen.chat_tb_size, R.dimen.chat_tb_size).centerCrop()
		.into(holder.avatar);
		
		return v;
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		public TextView planetNameView;
		public TextView distView;
		public ImageView avatar;
	}
	

	
	/*
	 * We create our filter	
	 */
	
	@Override
	public Filter getFilter() {
		if (planetFilter == null)
			planetFilter = new PlanetFilter();
		
		return planetFilter;
	}



	private class PlanetFilter extends Filter {

		
		
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = origPlanetList;
				results.count = origPlanetList.size();
			}
			else {
				// We perform filtering operation
				List<DataChatFriend> nPlanetList = new ArrayList<DataChatFriend>();
				
				for (DataChatFriend p : planetList) {
					if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
						nPlanetList.add(p);
				}
				
				results.values = nPlanetList;
				results.count = nPlanetList.size();

			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				planetList = (List<DataChatFriend>) results.values;
				notifyDataSetChanged();
			}
			
		}
		
	}
}
