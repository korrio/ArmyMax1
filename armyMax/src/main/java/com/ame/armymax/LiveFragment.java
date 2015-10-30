package com.ame.armymax;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.R.id;
import com.ame.armymax.R.layout;
import com.ame.armymax.adapter.FeedLiveListViewAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataFeedLive;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.player.PlayActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

@SuppressLint({ "ValidFragment", "NewApi" })
public class LiveFragment extends Fragment {

	AQuery aq;
	View rootView;

	Context context;
	public ArrayList<DataFeedEverything> feedList = new ArrayList<DataFeedEverything>();
	LinearLayout footer;

	public LiveFragment(Context context) {
		this.context = context;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_feed, container, false);

		aq = new AQuery(rootView);
		
		footer = (LinearLayout) rootView.findViewById(R.id.footer_layout);
		footer.setVisibility(View.GONE);

		String url;
		String cbName;

		String token = DataUser.VM_USER_TOKEN;
		String surl = "http://www.armymax.com/api/?action=getTimeline";
		String app = "live";
		String start = "0";
		String size = "50";

		url = surl + "&token=" + token + "&type=public&app=" + app
				+ "&startPoint=" + start + "&sizePage=" + size;
		cbName = "feedLiveCb";

		aq.progress(R.id.progress).ajax(url, JSONObject.class, this, cbName);

		return rootView;
	}

	public void feedLiveCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			if(jo.optInt("status") != 2003) {
				DataFeedLive.clearAll();
				Log.e("check", jo.toString());
				JSONArray ja = jo.optJSONArray("data");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject post = ja.getJSONObject(i);
					String id = post.getString("ID");
					String userId = post.getString("UserID");
					String postType = post.getString("PostType");

					String postId = post.getString("PostID");
					String time = post.getString("Time");
					String favoriteCount = post.getString("Favorits");
					String loveCount = post.getString("Loves");
					String commentCount = post.getString("Comments_n");
					// post.getString("Comments");
					// String viewCount = post.getString("Views");
					String userName = post.getString("UserName");
					String avatar = post.getString("UserAvatarPath");

					String statusText = post.getString("liveTitle");

					String ago = post.getString("Time");

					String name = post.getString("UserFirstName") + " "
							+ post.getString("UserLastName");

					String tbUrl;

					PrettyTime p = new PrettyTime();

					long agoLong = Integer.parseInt(ago);
					Date timeAgo = new Date((long) agoLong * 1000);

					ago = p.format(timeAgo);

					// general
					DataFeedLive.setName(name);
					DataFeedLive.setStatus(statusText);
					DataFeedLive.setAgo(ago);
					DataFeedLive.setLoveCount(" " + loveCount + " Loves");
					DataFeedLive.setCommentCount(" " + commentCount + " Comments");
					DataFeedLive.setPostType(postType);
					DataFeedLive.setPostId(postId);

					if (avatar.contains("facebook")) {
						DataFeedLive.setTbUrl(avatar);
						tbUrl = avatar;
					} else {
						DataFeedLive.setTbUrl(Data.BASE + avatar);
						tbUrl = Data.BASE + avatar;
					}
					String contentTbUrl = "https://www.armymax.com/photo-live/"
							+ userName;
					
					if(post.getString("livePhoto").equals("vdomaxscreen")) {
						contentTbUrl = "https://www.armymax.com/photo/" + post.getString("livePhoto") + ".png";
					} else {
						contentTbUrl = Data.BASE + post.getString("livePhoto")
							+ "/thumbnail_500";
					}

					String contentName = post.getString("liveTitle");
					String contentDesc = post.getString("liveDesc");
					String contentMeta = post.getString("liveStatus");
					String videoSource = userName;
					String loveUrl = post.getJSONObject("Love").getString(
							"Love_command");
					String loveStatus = post.getJSONObject("Love").getInt(
							"Love_status")
							+ "";

					DataFeedLive.setContentTbUrl(contentTbUrl);
					DataFeedLive.setContentName(contentName);
					DataFeedLive.setContentDesc(contentDesc);
					DataFeedLive.setContentMeta("");
					DataFeedLive.setVideoSource(videoSource);

					DataFeedEverything liveFeed = new DataFeedEverything(postId,
							postType, tbUrl, userName, ago, statusText, contentTbUrl,
							contentName, contentDesc, contentMeta, loveCount,
							commentCount, videoSource, loveUrl, loveStatus,userId);
					
					feedList.add(liveFeed);

				}
			} else {
				Toast.makeText(context, jo.optString("msg"), Toast.LENGTH_LONG).show();
				Intent i = new Intent(getActivity(),LoginActivity.class);
				startActivity(i);
			}
			
		}

		ListView listView = (ListView) rootView
				.findViewById(R.id.feed_listview);
		final FeedLiveListViewAdapter adapter = new FeedLiveListViewAdapter(
				getActivity(), 3);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long id) {
				 //String[] vals = adapter.getItem(i);
				
				/*
				int type;

				type = 1;
				String contentUrl = DataFeedLive.getVideoSource(i - 1);
				String contentName = DataFeedLive.getContentName(i - 1);
				String contentTbUrl = DataFeedLive.getContentTbUrl(i - 1);

				String vdoOwner = DataFeedLive.getName(i - 1);
				String vdoOwnerTb = DataFeedLive.getTbUrl(i - 1);
				String vdoAgo = DataFeedLive.getAgo(i - 1);

				//Toast.makeText(context, contentUrl, Toast.LENGTH_LONG).show();
				//showVideoDetails(i - 1, contentUrl, contentName, contentTbUrl,
					//	vdoOwner, vdoOwnerTb, vdoAgo, type, "live");
					 
					 */
				i = i -1;
				showVideoDetails(i);
				
				/*
				Intent intent = new Intent(getActivity(), PlayActivity.class);
				intent.putExtra("isPlay", "1");
				intent.putExtra("roomId", feedList.get(i).getVideoSource());
				intent.putExtra("roomTag", "0");
				startActivity(intent);
				*/
				
				
				

			}
		});

	}

	
	void showVideoDetails(int i) {
		Intent videoDetailIntent = new Intent(getActivity(),
				VideoDetailActivity.class);
		videoDetailIntent.putParcelableArrayListExtra("data", feedList);
		videoDetailIntent.putExtra("position", "" + i);
		startActivity(videoDetailIntent);
	}

}
