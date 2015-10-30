package com.ame.armymax.service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.ame.armymax.model.DataFeedFollower;
import com.ame.armymax.model.DataFeedFollowing;
import com.ame.armymax.model.DataFeedMe;
import com.ame.armymax.model.DataFeedSocial;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

public class CallFeedService extends AsyncTask<Context, Integer, String> {
	String s = "";

	AQuery aq;
	Integer type;

	Context context;

	ViewPager viewPager;

	public CallFeedService(Integer type) {
		this.type = type;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	protected String doInBackground(Context... params) {
		context = params[0];
		aq = new AQuery(params[0]);

		ajaxFeed(1);
		ajaxFeed(2);
		ajaxFeed(3);
		ajaxFeed(4);

		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		System.out.println("Downloading... : " + values[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("async", "finish loading");
		showToast("finished" + type);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public void showToast(Object o) {
		Toast.makeText(context, o.toString(), Toast.LENGTH_SHORT).show();
	}

	private void ajaxFeed(int mytype) {
		String url;
		String cbName = "";
		String token = DataUser.VM_USER_TOKEN;
		String surl = "https://api.vdomax.com/service/getTimeline/mobile";
		String app = "video";
		String start = "0";
		String size = "20";
		switch (mytype) {
		case 1:
			// my video
			url = surl + "?token=" + token + "&type=me&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedCb";
			break;
		case 2:
			// social video
			url = surl + "?token=" + token + "&type=public&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedSocialCb";
			break;
		case 3:
			// following
			url = surl + "?token=" + token + "&type=following&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedFollowingCb";
			break;
		case 4:
			// follower
			url = surl + "?token=" + token + "&type=follower&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedFollowerCb";
			break;
		case 5:
			app = "photo";
			url = surl + "?token=" + token + "&type=public&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedPhotoCb";
			break;
		case 6:
			app = "live";
			url = surl + "?token=" + token + "&type=public&app=" + app
					+ "&startPoint=" + start + "&sizePage=" + size;
			cbName = "feedLiveCb";
			break;
		default:
			url = surl + "?token=" + token
					+ "&type=public&app=video&startPoint=0&sizePage=20";
			cbName = "feedCb";
			break;
		}

		aq.ajax(url, JSONObject.class, this, cbName);
		Log.d("cbName", cbName);

	}

	public void feedCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			DataFeedMe.clearAll();
			JSONArray ja = jo.getJSONArray("data");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);

				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String avatar = post.getString("UserAvatarPath");
				String ago = timeToString((long) post.getInt("Time"));

				String contentName = post.getString("videoTitle");
				String contentDesc = post.getString("videoDesc");
				String contentMeta = post.getString("videoType");
				String statusText = post.getString("videoText");
				String commentCount = post.getString("Comments_n");
				String loveCount = post.getString("Loves");

				String contentTbUrl = "";

				if (contentMeta.equals("youtube")) {
					contentTbUrl = post.getString("videoThumbnail");
				} else {
					contentTbUrl = DataFeedMe.BASE
							+ post.getString("videoThumbnail");
				}

				String videoSource = post.getString("videoSource");

				DataFeedMe.setName(name);
				DataFeedMe.setStatus(statusText);
				DataFeedMe.setAgo(ago);
				DataFeedMe.setContentTbUrl(contentTbUrl);
				DataFeedMe.setContentName(contentName);
				DataFeedMe.setContentDesc(contentDesc);
				DataFeedMe.setContentMeta(contentMeta);
				DataFeedMe.setCommentCount(commentCount);
				DataFeedMe.setLoveCount(loveCount);

				// video
				if (contentMeta.equals("youtube")) {
					videoSource = "www.youtube.com/watch?v=" + videoSource;
				} else {
					videoSource = "rtmp://203.151.162.5/app2/mp4:"
							+ videoSource;
				}
				DataFeedMe.setVideoSource(videoSource);

				if (avatar.contains("facebook"))
					DataFeedMe.setTbUrl(avatar);
				else
					DataFeedMe.setTbUrl(DataFeedMe.BASE + avatar);

			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void feedSocialCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			DataFeedSocial.clearAll();
			JSONArray ja = jo.getJSONArray("data");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);
				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String avatar = post.getString("UserAvatarPath");
				String ago = timeToString((long) post.getInt("Time"));

				String contentName = post.getString("videoTitle");
				String contentDesc = post.getString("videoDesc");
				String contentMeta = post.getString("videoType");
				String statusText = post.getString("videoText");
				String commentCount = post.getString("Comments_n");
				String loveCount = post.getString("Loves");

				String contentTbUrl = "";

				if (contentMeta.equals("youtube")) {
					contentTbUrl = post.getString("videoThumbnail");
				} else {
					contentTbUrl = DataFeedSocial.BASE
							+ post.getString("videoThumbnail");
				}

				String videoSource = post.getString("videoSource");

				DataFeedSocial.setName(name);
				DataFeedSocial.setStatus(statusText);
				DataFeedSocial.setAgo(ago);
				DataFeedSocial.setContentTbUrl(contentTbUrl);
				DataFeedSocial.setContentName(contentName);
				DataFeedSocial.setContentDesc(contentDesc);
				DataFeedSocial.setContentMeta(contentMeta);
				DataFeedSocial.setCommentCount(commentCount);
				DataFeedSocial.setLoveCount(loveCount);

				// video
				if (contentMeta.equals("youtube")) {
					videoSource = "www.youtube.com/watch?v=" + videoSource;
				} else {
					videoSource = "rtmp://203.151.162.5/app2/mp4:"
							+ videoSource;
				}
				DataFeedSocial.setVideoSource(videoSource);

				if (avatar.contains("facebook"))
					DataFeedSocial.setTbUrl(avatar);
				else
					DataFeedSocial.setTbUrl(DataFeedMe.BASE + avatar);

			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void feedFollowingCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			DataFeedFollowing.clearAll();
			JSONArray ja = jo.getJSONArray("data");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);
				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String avatar = post.getString("UserAvatarPath");
				String ago = timeToString((long) post.getInt("Time"));

				String contentName = post.getString("videoTitle");
				String contentDesc = post.getString("videoDesc");
				String contentMeta = post.getString("videoType");
				String statusText = post.getString("videoText");
				String commentCount = post.getString("Comments_n");
				String loveCount = post.getString("Loves");

				String contentTbUrl = "";

				if (contentMeta.equals("youtube")) {
					contentTbUrl = post.getString("videoThumbnail");
				} else {
					contentTbUrl = DataFeedFollowing.BASE
							+ post.getString("videoThumbnail");
				}

				String videoSource = post.getString("videoSource");

				DataFeedFollowing.setName(name);
				DataFeedFollowing.setStatus(statusText);
				DataFeedFollowing.setAgo(ago);
				DataFeedFollowing.setContentTbUrl(contentTbUrl);
				DataFeedFollowing.setContentName(contentName);
				DataFeedFollowing.setContentDesc(contentDesc);
				DataFeedFollowing.setContentMeta(contentMeta);
				DataFeedFollowing.setCommentCount(commentCount);
				DataFeedFollowing.setLoveCount(loveCount);

				// video
				if (contentMeta.equals("youtube")) {
					videoSource = "www.youtube.com/watch?v=" + videoSource;
				} else {
					videoSource = "rtmp://203.151.162.5/app2/mp4:"
							+ videoSource;
				}
				DataFeedFollowing.setVideoSource(videoSource);

				if (avatar.contains("facebook"))
					DataFeedFollowing.setTbUrl(avatar);
				else
					DataFeedFollowing.setTbUrl(DataFeedFollowing.BASE + avatar);

			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public void feedFollowerCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			DataFeedFollower.clearAll();
			JSONArray ja = jo.getJSONArray("data");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject post = ja.getJSONObject(i);
				String name = post.getString("UserFirstName") + " "
						+ post.getString("UserLastName");
				String avatar = post.getString("UserAvatarPath");
				String ago = timeToString((long) post.getInt("Time"));

				String contentName = post.getString("videoTitle");
				String contentDesc = post.getString("videoDesc");
				String contentMeta = post.getString("videoType");
				String statusText = post.getString("videoText");
				String commentCount = post.getString("Comments_n");
				String loveCount = post.getString("Loves");

				String contentTbUrl = "";

				if (contentMeta.equals("youtube")) {
					contentTbUrl = post.getString("videoThumbnail");
				} else {
					contentTbUrl = DataFeedFollower.BASE
							+ post.getString("videoThumbnail");
				}

				String videoSource = post.getString("videoSource");

				DataFeedFollower.setName(name);
				DataFeedFollower.setStatus(statusText);
				DataFeedFollower.setAgo(ago);
				DataFeedFollower.setContentTbUrl(contentTbUrl);
				DataFeedFollower.setContentName(contentName);
				DataFeedFollower.setContentDesc(contentDesc);
				DataFeedFollower.setContentMeta(contentMeta);
				DataFeedFollower.setCommentCount(commentCount);
				DataFeedFollower.setLoveCount(loveCount);

				// video
				if (contentMeta.equals("youtube")) {
					videoSource = "www.youtube.com/watch?v=" + videoSource;
				} else {
					videoSource = "rtmp://203.151.162.5/app2/mp4:"
							+ videoSource;
				}
				DataFeedFollower.setVideoSource(videoSource);

				if (avatar.contains("facebook"))
					DataFeedFollower.setTbUrl(avatar);
				else
					DataFeedFollower.setTbUrl(DataFeedFollower.BASE + avatar);

			}

			AQUtility.debug("done");

		} else {
			AQUtility.debug("error!");
		}

	}

	public String timeToString(long unixSeconds) {
		Date date = new Date(unixSeconds * 1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
}
