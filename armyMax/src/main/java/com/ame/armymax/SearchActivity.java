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

package com.ame.armymax;

/**
 * @author Surviving with android (jfrankie)
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ame.armymax.adapter.SearchResultAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataSearchResult;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.ListUtil;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class SearchActivity extends Activity {

	List<DataSearchResult> resultList = new ArrayList<DataSearchResult>();
	SearchResultAdapter adapter;
	ListView listView;
	AQuery aq;
	BootstrapEditText searchBox;
	Button searchButton;
	String query = "a";
	Context context;
	boolean newSearch = false;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		context = this;
		DataUser.context = context;
		aq = new AQuery(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("ค้นหาเพื่อน");
		
		query = getIntent().getStringExtra("query");

		searchBox = (BootstrapEditText) findViewById(R.id.room_name);
		searchBox.setText(query);
		
		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setVisibility(View.GONE);
		
		intent = new Intent(this, ProfileActivity.class);

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				query = searchBox.getEditableText().toString();

				if (!query.equals("")) {
					String resultUrl = "http://www.armymax.com/api/?action=search&txt="
							+ query + "&startPoint=0&sizePage=50";
					ProgressDialog dialog = new ProgressDialog(context);
					dialog.setIndeterminate(true);
					dialog.setCancelable(true);
					dialog.setInverseBackgroundForced(false);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setTitle("Finding ...");
					Log.e("searchurl",resultUrl);
					newSearch = true;
					aq.progress(dialog).ajax(resultUrl, JSONObject.class, context, "newSearchResultCb");
					
				}

			}
		});

		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				query = searchBox.getEditableText().toString();

				if (!query.equals("")) {
					String resultUrl = "http://www.armymax.com/api/?action=search&txt="
							+ query + "&startPoint=0&sizePage=20";
					
					ProgressDialog dialog = new ProgressDialog(context);
					dialog.setIndeterminate(true);
					dialog.setCancelable(true);
					dialog.setInverseBackgroundForced(false);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setTitle("Finding ...");
					newSearch = true;
					Log.e("q",query);
					aq.ajax(resultUrl, JSONObject.class, context, "newSearchResultCb");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		String resultUrl = "http://www.armymax.com/api/?action=search&txt="
				+ query + "&startPoint=0&sizePage=50";
		aq.ajax(resultUrl, JSONObject.class, this, "searchResultCb");

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent i = new Intent(SearchActivity.this,
							MainActivity.class);
					finish();
					startActivity(i);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.dismiss();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("ต้องการออกจากห้องประชุม?")
				.setPositiveButton("ใช่", dialogClickListener)
				.setNegativeButton("ไม่", dialogClickListener).show();

	}

	public void recentChatCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {

		}
	}
	
	public void newSearchResultCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			resultList.clear();
			JSONObject people = jo.getJSONObject("people");

			JSONArray ja = people.getJSONArray("data");

			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);

				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String userName = post.getString("UserName");

				String avatar = post.getString("UserAvatarPath");
				Integer userId = Integer.parseInt(post.getString("UserID"));

				if (!avatar.contains("facebook"))
					avatar = Data.BASE + avatar + "/thumbnail_500";
				//avatar = "http://www.armymax.com/photo/" + avatar;

				DataSearchResult result = new DataSearchResult(userId, name,
						userName, avatar);

				resultList.add(result);
			}
			
			new AdapterHelper().update((ArrayAdapter<DataSearchResult>) adapter,
					new ArrayList<Object>(resultList));
			ListUtil.setListViewHeightBasedOnChildren(listView);
			
			listView
			.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parentAdapter,
						View view, int position, long id) {

					DataSearchResult p = (DataSearchResult) parentAdapter
							.getItemAtPosition(position);

					intent.putExtra("id", p.getUserId().toString());
					intent.putExtra("username", p.getUserName());
					startActivity(intent);

				}
			});

	// we register for the contextmneu
	registerForContextMenu(listView);

		}
	}

	public void searchResultCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			resultList.clear();
			JSONObject people = jo.getJSONObject("people");

			JSONArray ja = people.getJSONArray("data");

			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);

				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String userName = post.getString("UserName");

				String avatar = post.getString("UserAvatarPath");
				Integer userId = Integer.parseInt(post.getString("UserID"));

				if (!avatar.contains("facebook"))
					avatar = Data.BASE + avatar + "/thumbnail_500";

				DataSearchResult result = new DataSearchResult(userId, name,
						userName, avatar);

				resultList.add(result);
			}
			
			

		}
		
		listView = (ListView) findViewById(R.id.listView);
		adapter = new SearchResultAdapter(resultList, this);
		listView.setAdapter(adapter);
		
		
		listView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parentAdapter,
							View view, int position, long id) {

						DataSearchResult p = (DataSearchResult) parentAdapter
								.getItemAtPosition(position);

						intent.putExtra("id", p.getUserId().toString());
						intent.putExtra("username", p.getUserName());
						startActivity(intent);

					}
				});

		// we register for the contextmneu
		registerForContextMenu(listView);

		// TextFilter
		//listView.setTextFilterEnabled(true);
		

		
	}
	
	public class AdapterHelper {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void update(ArrayAdapter arrayAdapter,
				ArrayList<Object> listOfObject) {
			arrayAdapter.clear();
			for (Object object : listOfObject) {
				arrayAdapter.add(object);
			}
		}
	}

	// We want to create a context Menu when the user long click on an item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		DataSearchResult planet = adapter.getItem(aInfo.position);

		menu.setHeaderTitle("Options for " + planet.getName());
		menu.add(1, 1, 1, "Details");
		menu.add(1, 2, 2, "Delete");

	}

	// This method is called when user selects an Item in the Context menu
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		resultList.remove(aInfo.position);
		adapter.notifyDataSetChanged();
		return true;
	}

	private void initList() {
		// We populate the friends

		resultList.add(new DataSearchResult("Mercury", 10));
		resultList.add(new DataSearchResult("Venus", 20));
		resultList.add(new DataSearchResult("Mars", 30));
		resultList.add(new DataSearchResult("Jupiter", 40));
		resultList.add(new DataSearchResult("Saturn", 50));
		resultList.add(new DataSearchResult("Uranus", 60));
		resultList.add(new DataSearchResult("Neptune", 70));

	}

	// Handle user click
	public void addPlanet(View view) {

	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
