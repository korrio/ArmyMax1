package com.ame.armymax;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.R.id;
import com.ame.armymax.R.layout;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class RouteActivity extends Activity {

	Context context;
	AQuery aq;
	public ArrayList<DataFeedEverything> feedList = new ArrayList<DataFeedEverything>();
	private static String POST_ID;
	private static String USER_ID;
	private static String TYPE;

	TextView noPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route);

		noPost = (TextView) findViewById(R.id.edit_profile);

		context = this;
		DataUser.context = context;
		aq = new AQuery(context);

		TYPE = getIntent().getStringExtra("type");
		POST_ID = getIntent().getStringExtra("post_id");
		USER_ID = getIntent().getStringExtra("user_id");

		if (TYPE.equals("post")) {
			String urlPost = "http://www.armymax.com/api/?action=getPostInfo&token="
					+ DataUser.VM_USER_TOKEN + "&ID=" + POST_ID;
			Log.e("mynoti", urlPost);
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setInverseBackgroundForced(false);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setTitle("Loading Post : " + POST_ID);
			aq.progress(dialog).ajax(urlPost, JSONObject.class, context,
					"postInfoCb");
		} else if (TYPE.equals("profile")) {
			String urlProfile = "http://www.armymax.com/api/?action=getprofileinfo&token="
					+ DataUser.VM_USER_TOKEN + "&userid=" + USER_ID;
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setInverseBackgroundForced(false);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setTitle("Loading Profile : " + USER_ID);
			aq.progress(dialog).ajax(urlProfile, JSONObject.class, context,
					"profileInfoCb");
		}

	}

	public void profileInfoCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Log.e("checkprofile", jo.toString());
			JSONObject post = jo.optJSONObject("data");
			if (post != null) {
				String username = post.getString("UserName");
				Intent profileIntent = new Intent(context,
						ProfileActivity.class);
				profileIntent.putExtra("id", USER_ID);
				profileIntent.putExtra("username", username);
				startActivity(profileIntent);
				finish();
			} else {
				Log.e("errorurl", url);
				Toast.makeText(context, "error loading profile : " + USER_ID,
						Toast.LENGTH_LONG).show();
			}

		}
	}

	public void postInfoCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			if (jo.optInt("status") != 6004) {
				Log.e("check", jo.toString());
				JSONObject post = jo.optJSONObject("data");
				if (post != null) {
					Log.e("yeayea", post.toString());
					String id = post.getString("ID");
					String userId = post.getString("UserID");

					String postId = post.getString("PostID");
					String postType = post.getString("PostType");
					String loveCount = post.getString("Loves");
					String commentCount = post.getString("Comments_n");
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

					if (!avatar.contains("facebook"))
						avatar = Data.BASE + avatar;

					int type = Integer.parseInt(postType);

					String contentName = post.getString("videoTitle");
					String contentDesc = post.getString("videoDesc");
					String contentMeta = post.getString("videoDesc");
					String contentTbUrl = post.getString("videoThumbnail");
					String videoSource = post.getString("videoSource");
					String loveUrl = "";
					String loveStatus = "";

					switch (type) {
					case 1:
						statusText = post.getString("newsText");

						break;
					case 2:
						statusText = post.getString("liveTitle");
						contentName = post.getString("liveTitle");
						contentTbUrl = Data.BASE + post.getString("livePhoto");

						videoSource = userName;

						post.getString("livePermission");

						break;
					case 3:
						statusText = post.getString("photoText");
						contentTbUrl = Data.BASE
								+ post.getString("photoSource")
								+ "/thumbnail_500";

						break;
					case 4:
						statusText = post.getString("videoText");
						contentMeta = post.getString("videoType");

						if (contentMeta.equals("youtube")
								|| post.getString("videoThumbnail").contains(
										"ytimg")) {
							contentTbUrl = post.getString("videoThumbnail");
						} else {
							contentTbUrl = Data.BASE
									+ post.getString("videoThumbnail");
						}

						if (contentMeta.equals("youtube")) {
							videoSource = "http://www.youtube.com/watch?v="
									+ videoSource;
						} else {
							videoSource = "http://"+Data.VOD+":1935/app2/mp4:"
									+ videoSource + "/playlist.m3u8";
						}

						break;
					default:
						System.out.print("default");
						break;
					}

					DataFeedEverything feedElement = new DataFeedEverything(
							postId, postType, avatar, name, ago, statusText,
							contentTbUrl, contentName, contentDesc,
							contentMeta, loveCount, commentCount, videoSource,
							loveUrl, loveStatus,userId);

					String[] yes = { postId, postType, avatar, name, ago,
							statusText, contentTbUrl, contentName, contentDesc,
							contentMeta, loveCount, commentCount, videoSource,
							loveUrl, loveStatus,userId };

					for (int i = 0; i < yes.length; i++)
						Log.e("checkEverything", yes[i]);

					feedList.add(feedElement);
					showVideoDetails(0);
					finish();
				} else {
					Log.e("errorurl", url);
					Toast.makeText(context, "error loading post : " + POST_ID,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(context, jo.optString("msg"), Toast.LENGTH_LONG)
						.show();
				noPost.setVisibility(View.VISIBLE);
			}

		}
	}

	void showVideoDetails(int i) {
		Intent videoDetailIntent = new Intent(context,
				VideoDetailActivity.class);
		videoDetailIntent.putParcelableArrayListExtra("data", feedList);
		videoDetailIntent.putExtra("position", "" + i);
		startActivity(videoDetailIntent);
	}

}
