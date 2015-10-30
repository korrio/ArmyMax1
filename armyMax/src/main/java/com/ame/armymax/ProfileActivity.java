package com.ame.armymax;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ame.armymax.adapter.EverythingAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkActivity;
import com.androidbegin.armymax.chat.Link;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.ListUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

@SuppressLint({ "ValidFragment", "NewApi" })
public class ProfileActivity extends FragmentActivity {

	AQuery aq;
	Context context;
	TextView profileName;
	ImageView profileTb;
	TextView countFollower;
	TextView countFollowing;
	ToggleButton followButton;
	ToggleButton addFriend;
	
	Button chatButton;

	public ArrayList<DataFeedEverything> feedList = new ArrayList<DataFeedEverything>();
	public ListView listView;
	public EverythingAdapter adapter;

	String USER_ID;
	String USER_NAME;

	int RELATION = 0;

	ImageView imgQRCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		context = this;
		DataUser.context = context;
		aq = new AQuery(context);

		String a = "http://www.armymax.com/api/?action=getUserInfo&token="
				+ DataUser.VM_USER_TOKEN + "&userName=" + USER_NAME;
		String b = "http://www.armymax.com/api/?action=getUserTimelineByUserName&token="
				+ DataUser.VM_USER_TOKEN
				+ "&userName="
				+ USER_NAME
				+ "&type=public&startPoint=0&sizePage=80&app=all";

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent myIntent = getIntent(); // gets the previously created intent
		USER_ID = myIntent.getStringExtra("id");
		USER_NAME = myIntent.getStringExtra("username");
		
		

		imgQRCode = (ImageView) findViewById(R.id.qr_code);
		imgQRCode.setImageBitmap(genarateQR(USER_NAME));

		followButton = (ToggleButton) findViewById(R.id.follow_button);
		followButton.setChecked(false);

		chatButton = (Button) findViewById(R.id.chat_button);
		addFriend = (ToggleButton) findViewById(R.id.add_friend);
		addFriend.setChecked(false);
		
		if(USER_ID.equals(DataUser.VM_USER_ID)) {
			followButton.setVisibility(View.GONE);
			chatButton.setVisibility(View.GONE);
			addFriend.setVisibility(View.GONE);
		}

		addFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int flag;
				String txt;
				// Log.e("checked:", followButton.isChecked() + "");
				if (RELATION == 0) {
					flag = 1;
					txt = "become friend with";
				} else {
					flag = 0;
					txt = "unfriend with";
				}

				String followUrl = "http://www.armymax.com/?action=doFollow&?callback=doFollow&token="
						+ DataUser.VM_USER_TOKEN
						+ "&followID="
						+ USER_ID
						+ "&flag=" + flag;
				Log.e("followurl", followUrl);
				ProgressDialog dialog = new ProgressDialog(context);
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setInverseBackgroundForced(false);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setTitle("You " + txt + " " + USER_NAME);
				aq.progress(dialog).ajax(followUrl, JSONObject.class, this,
						"doFollowCb");

			}
		});

		profileName = (TextView) findViewById(R.id.profile_name);

		profileTb = (ImageView) findViewById(R.id.profile_avatar);

		countFollower = (TextView) findViewById(R.id.countFollower);
		countFollowing = (TextView) findViewById(R.id.countFollowing);

		listView = (ListView) findViewById(R.id.profile_feed_listview);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int index, long id) {
				// index = index - 1;
				String postId = feedList.get(index).getPostId();
				if (feedList.get(index).getPostType().equals("1")) {
					showVideoDetails(index);
				} else if (feedList.get(index).getPostType().equals("2")) {
					showVideoDetails(index);
				} else if (feedList.get(index).getPostType().equals("3")) {
					String status = feedList.get(index).getStatus();
					String contentTbUrl = feedList.get(index).getContentTbUrl();
					//showPhotoDetails(postId, contentTbUrl, status);
					showVideoDetails(index);
				} else {
					String contentMeta = feedList.get(index).getContentMeta();
					String videoSource = feedList.get(index).getVideoSource();


					showVideoDetails(index);


				}

			}
		});
		// feedList.clear();

		adapter = new EverythingAdapter(feedList, context);
		adapter.resetData();
		listView.setAdapter(adapter);

		getUserInfo();
		getUserFeed();

	}

	Bitmap genarateQR(String username) {
		Bitmap bm = null;
		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(username, BarcodeFormat.QR_CODE,
					400, 400);
			bm = toBitmap(matrix);
			// Now what??
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap toBitmap(BitMatrix matrix) {
		int height = matrix.getHeight();
		int width = matrix.getWidth();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

	private void getUserInfo() {
		String url_userinfo = "http://www.armymax.com/api/?action=getUserInfo&token="
				+ DataUser.VM_USER_TOKEN + "&userName=" + USER_NAME;
		String cbName = "profileCb";
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Loading...");
		dialog.setMessage("กำลังโหลดเพื่อน");
		aq.progress(dialog).ajax(url_userinfo, JSONObject.class, context,
				cbName);
	}

	private void getUserFeed() {
		String url;
		String cbName = "feedProfileCb";
		String token = DataUser.VM_USER_TOKEN;
		String username = USER_NAME;
		String surl = "http://www.armymax.com/api/?action=getUserTimelineByUserName";
		String app = "all";
		String start = "0";
		String size = "20";

		url = surl + "&token=" + token + "&userName=" + username
				+ "&type=me&app=" + app + "&startPoint=" + start + "&sizePage="
				+ size;

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Loading user timeline");
		aq.progress(dialog).ajax(url, JSONObject.class, context, cbName);
	}

	public void profileCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		JSONObject profile = jo.getJSONObject("data");
		Log.e("myjsonprofile", jo.toString());
		if (jo != null) {

			String avatar = profile.getString("UserAvatarPath");
			String nFollowing = profile.getString("UserFollowings");
			String nFollower = profile.getString("UserFollowers");
			final String profileStr = profile.getString("UserFirstName") + " "
					+ profile.getString("UserLastName");
			final String userId = profile.getString("UserID");
			int relation = profile.getJSONObject("follow").getInt("Relation");

			if (relation == 1) {
				RELATION = 1;
				Toast.makeText(context, "You are now friend with this user",
						Toast.LENGTH_SHORT).show();
				addFriend.setChecked(true);
			}

			if (!avatar.contains("facebook"))
				avatar = Data.BASE + avatar;

			Picasso.with(context).load(avatar).fit().into(profileTb);
			profileName.setText(profileStr);
			countFollower.setText(nFollower);
			countFollowing.setText(nFollowing);

			chatButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent chatRoom = new Intent(ProfileActivity.this,
							XWalkActivity.class);
					//chatRoom.putExtra("userId", userId);
					//chatRoom.putExtra("friendName", profileStr);
					chatRoom.putExtra("url", Link.getLinkChat(userId));
					chatRoom.putExtra("title", profileStr);

					startActivity(chatRoom);
				}
			});

		}
	}

	public void feedProfileCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		JSONArray ja = jo.getJSONArray("data");
		if (jo != null && ja != null) {
			// feedList.clear();
			Log.e("myjson", jo.toString());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);
				String id = post.getString("ID");
				String userId = post.getString("UserID");

				String postId = post.getString("PostID");
				String postType = post.getString("PostType");
				String loveCount = post.getString("Loves");
				String commentCount = post.getString("Comments_n");
				// String viewCount = post.getString("Views");
				String userName = post.getString("UserName");
				String avatar = post.getString("UserAvatarPath");
				String userFirstName = post.getString("UserFirstName");
				String userLastName = post.getString("UserLastName");
				String statusText = "";

				String name = userFirstName + " " + userLastName;

				String ago = post.getString("Time");
				PrettyTime p = new PrettyTime();
				long agoLong = Integer.parseInt(ago);
				Date timeAgo = new java.util.Date((long) agoLong * 1000);
				ago = p.format(timeAgo);

				loveCount = " " + loveCount + "";
				commentCount = " " + commentCount + "";

				if (!avatar.contains("facebook"))
					avatar = Data.BASE + avatar;

				int type = Integer.parseInt(postType);

				String contentName = post.getString("videoTitle");
				String contentDesc = post.getString("videoDesc");
				String contentMeta = post.getString("videoDesc");
				String contentTbUrl = post.getString("videoThumbnail");
				String videoSource = post.getString("videoSource");
				String loveUrl = post.getJSONObject("Love").getString(
						"Love_command");
				String loveStatus = post.getJSONObject("Love").getInt(
						"Love_status")
						+ "";

				switch (type) {
				case 1:
					statusText = post.optString("newsText");
					contentName = post.optString("newsText");
					break;
				case 2:
					statusText = post.optString("liveTitle");
					contentName = post.optString("liveDesc");

					/*
					if (post.optString("livePhoto").equals("vdomaxscreen")) {
						contentTbUrl = Data.BASE
								+ post.optString("livePhoto");
					} else {
					*/
						contentTbUrl = "http://www.armymax.com/live-photo/"+post.optString("UserName")+".png";
					//}

					JSONObject streamUrls = post
							.getJSONObject("stream_url");
					String rtmp = streamUrls.optString("rtmp");

					videoSource = userName;

					post.optString("livePermission");

					break;
				case 3:
					statusText = post.optString("photoText");

					contentName = "รูปภาพ โดย " + userName;

					contentMeta = Data.BASE + post.optString("photoSource");
					contentTbUrl = "http://www.armymax.com/photo/thumbnail_500/"
							+ post.optString("photoSource") + ".png";

					break;
				case 4:
					statusText = post.optString("videoText");
					contentMeta = post.optString("videoType");

					if (contentMeta.equals("youtube")
							|| post.optString("videoThumbnail").contains(
									"ytimg")) {
						contentTbUrl = post.optString("videoThumbnail");
					} else {
						contentTbUrl = Data.BASE
								+ post.optString("videoThumbnail");
					}

					if (contentMeta.equals("youtube")) {
						videoSource = "http://www.youtube.com/watch?v="
								+ videoSource;
					} else {
						videoSource = "http://www.armymax.com/vdofile/"
								+ videoSource;

					}

					contentTbUrl = Data.BASE
							+ post.optString("videoThumbnail");

					break;
				default:
					System.out.print("default");
					break;
				}

				DataFeedEverything feedElement = new DataFeedEverything(postId,
						postType, avatar, name, ago, statusText, contentTbUrl,
						contentName, contentDesc, contentMeta, loveCount,
						commentCount, videoSource, loveUrl, loveStatus, userId);

				feedList.add(feedElement);

			}
			adapter.append(feedList);
			adapter.notifyDataSetChanged();

			listView.setDividerHeight(5);
			ListUtil.setListViewHeightBasedOnChildren(listView);
		} else {
			Intent finishLogin = new Intent(context, LoginActivity.class);
			startActivity(finishLogin);
			finish();
		}

	}

	public void doFollowCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
					.show();
			if (jo.getInt("status") == 9001) {
				Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
						.show();
			} else {
				DataUser.clearAll();
				Intent logout = new Intent(context, LoginActivity.class);
				startActivity(logout);
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, PeopleGridViewActivity.class);
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

	void showPhotoDetails(String postId, String imgUrl, String caption) {
		Intent photoDetailIntent = new Intent(context,
				PhotoDetailActivity.class);
		photoDetailIntent.putExtra("post_id", postId);
		photoDetailIntent.putExtra("url", imgUrl);
		photoDetailIntent.putExtra("caption", caption);
		startActivity(photoDetailIntent);
	}

	void showVideoDetails(int i) {
		Intent videoDetailIntent = new Intent(context,
				VideoDetailActivity.class);
		videoDetailIntent.putParcelableArrayListExtra("data", feedList);
		videoDetailIntent.putExtra("position", "" + i);
		startActivity(videoDetailIntent);
	}

}
