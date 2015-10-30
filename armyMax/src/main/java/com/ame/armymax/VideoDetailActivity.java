package com.ame.armymax;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.ame.armymax.R.id;
import com.ame.armymax.adapter.CommentAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataComment;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.player.PlayerViewControllerRTMP;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.CircleTransform;
import com.androidquery.simplefeed.util.ListUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VideoDetailActivity extends Activity {

    @InjectView(R.id.comment_listview)
    public ListView listView;

    @InjectView(R.id.videoThumbnail)
    public ImageView thumb;
    @InjectView(R.id.comment_count)
    public TextView loveCount;
    @InjectView(id.love_count)
    public TextView commentCount;
    @InjectView(R.id.surfaceview)
	public RelativeLayout overlay;

    @InjectView(R.id.video_player)
    public VideoView videoView;
    @InjectView(R.id.love_button)
    public ToggleButton loveButton;

    @InjectView(R.id.profile_name)
    public TextView titleView;

    @InjectView(R.id.progress_video)
    public RelativeLayout progressbar;

    @InjectView(R.id.tb)
    public ImageView tb;
    @InjectView(R.id.statusName)
    public TextView yourName;
    @InjectView(R.id.ago)
    public TextView ago;

    @InjectView(R.id.statusDetail)
    public TextView statusDetail;
    @InjectView(R.id.button_post_comment)
    public Button post;

    @InjectView(id.post_comment_text)
    public EditText commentBox;

	public VideoDetailActivity() {

	}

    public String VDO_URL = "";
    public String VDO_NAME = "";
    public String VDO_THUMBNAIL = "";
    public String VDO_TYPE = "";
    public String VDO_LOVE_URL = "";
    public String VDO_LOVE_STATUS = "";
    public String POST_ID = "";
    public String POST_TYPE = "";
    public String COMMENT_TEXT = "";
    public PlayerViewControllerRTMP playerViewController;

    public int stopPosition;
    public static String from;

    public AQuery aq;
    public String url;
    public Context context;

    public List<DataComment> commentList = new ArrayList<DataComment>();

    public CommentAdapter adapter;

    public ArrayList<DataFeedEverything> list;
    public int index;

    public String postUsername;
    public String postIds;
    public String postTypes;
    public String videoSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_feed_detail);
        ButterKnife.inject(this);
		aq = new AQuery(this);
		context = this;
		DataUser.context = context;

		if (getIntent().getStringExtra("position") != null
				|| getIntent().getStringExtra("position") != "") {

			index = Integer.parseInt(getIntent().getStringExtra("position"));
			list = getIntent().getParcelableArrayListExtra("data");
			Picasso.with(this).load(list.get(index).getTbUrl())
					.transform(new CircleTransform())
					.resizeDimen(R.dimen.tb_size, R.dimen.tb_size).centerCrop()
					.into(tb);
			VDO_NAME = list.get(index).getContentName();
			VDO_URL = list.get(index).getVideoSource();
			VDO_THUMBNAIL = list.get(index).getContentTbUrl();
			VDO_LOVE_URL = list.get(index).getLoveUrl();
			// VDO_LOVE_URL = VDO_LOVE_URL.replace("?action=doLove&",
			// "doLove/mobile/");
			VDO_LOVE_STATUS = list.get(index).getLoveStatus();
			postIds = list.get(index).getPostId();
			postTypes = list.get(index).getPostType();

			postUsername = list.get(index).getVideoSource();
			yourName.setText(list.get(index).getName());
			ago.setText(list.get(index).getAgo());
			commentCount.setText(list.get(index).getCommentCount());
			loveCount.setText(list.get(index).getLoveCount());

			POST_ID = list.get(index).getPostId();
			POST_TYPE = list.get(index).getPostType();
			VDO_TYPE = list.get(index).getContentMeta();
			videoSource = list.get(index).getVideoSource();
			getComment(list.get(index).getPostId(), list.get(index)
					.getPostType());
		}

		videoView.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				mp.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
					@Override
					public void onVideoSizeChanged(MediaPlayer mp, int arg1,
							int arg2) {
						// TODO Auto-generated method stub
						progressbar.setVisibility(View.GONE);
						mp.start();
					}
				});
			}
		});

		loveButton.setChecked(false);
		if (VDO_LOVE_STATUS.equals("1"))
			loveButton.setChecked(true);

		statusDetail.setVisibility(View.GONE);
		titleView.setText(VDO_NAME);
		Picasso.with(this).load(VDO_THUMBNAIL).centerCrop().resize(720,480).into(thumb);


		loveButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (!isChecked) {
					ProgressDialog dialog = new ProgressDialog(context);
					VDO_LOVE_URL = VDO_LOVE_URL.replace("flag=1", "flag=0");

					dialog.setIndeterminate(true);
					dialog.setCancelable(true);
					dialog.setInverseBackgroundForced(false);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setTitle("Break...");
					Log.e("love_url", VDO_LOVE_URL);
					aq.progress(dialog).ajax(VDO_LOVE_URL, JSONObject.class,
							context, "lovePostCb");

				} else {
					ProgressDialog dialog = new ProgressDialog(context);
					VDO_LOVE_URL = VDO_LOVE_URL.replace("flag=0", "flag=1");

					dialog.setIndeterminate(true);
					dialog.setCancelable(true);
					dialog.setInverseBackgroundForced(false);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setTitle("Love...");
					Log.e("love_url", VDO_LOVE_URL);
					aq.progress(dialog).ajax(VDO_LOVE_URL, JSONObject.class,
							context, "lovePostCb");
				}

			}
		});

		post.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String postId = postIds;
                COMMENT_TEXT = commentBox.getText().toString();
				String postText = commentBox.getText().toString()
						.replace("\n", "%0A");
				String postComment = "https://armymax.com/api/?action=postComment&token="
						+ DataUser.VM_USER_TOKEN
						+ "&text="
						+ postText
						+ "&postID=" + postId + "&postType=" + postTypes;
				ProgressDialog dialog = new ProgressDialog(context);
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setInverseBackgroundForced(false);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setTitle("Posting");
				dialog.setMessage("กำลังโหลดความคิดเห็น...");
				aq.progress(dialog).ajax(postComment, JSONObject.class,
						context, "postCommentCb");

			}
		});

		titleView.setText(VDO_NAME);
		statusDetail.setVisibility(View.GONE);
        thumb.setVisibility(View.GONE);
		overlay.setVisibility(View.GONE);

        POST_TYPE = postTypes;

		if (postTypes.equals("1")) {
			progressbar.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);
            thumb.setVisibility(View.GONE);
			overlay.setVisibility(View.GONE);
			titleView.setText(list.get(index).getStatus());
		} else if (postTypes.equals("2")) {

			VDO_LOVE_URL = "https://www.armymax.com/api/?action=doLove&token="
					+ DataUser.VM_USER_TOKEN + "&type=post&commentID=0&postID="
					+ POST_ID + "&postType=" + postTypes + "&flag=1";

            thumb.setVisibility(View.GONE);
			overlay.setVisibility(View.VISIBLE);

			progressbar.setVisibility(View.GONE);

            initRTMPPlayer();

		} else if (postTypes.equals("3")) {
			progressbar.setVisibility(View.GONE);

			VDO_LOVE_URL = "https://www.armymax.com/api/?action=doLove&token="
					+ DataUser.VM_USER_TOKEN + "&type=post&commentID=0&postID="
					+ POST_ID + "&postType=" + postTypes + "&flag=1";
			overlay.setVisibility(View.GONE);
			Picasso.with(this).load(VDO_THUMBNAIL).into(thumb);
            thumb.setVisibility(View.VISIBLE);


            thumb.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					VDO_THUMBNAIL = VDO_THUMBNAIL.replace("thumbnail_500", "");
					showPhotoDetails(postIds, VDO_THUMBNAIL, VDO_NAME);

				}
			});
			titleView.setText(list.get(index).getStatus());
		} else if (postTypes.equals("4") && VDO_TYPE.equals("fileupload")) {
			progressbar.setVisibility(View.GONE);

			videoView.setVisibility(View.VISIBLE);
			VDO_URL = VDO_URL.replace("_800", "");
			// VDO_URL = DataUser.BASE + VDO_URL;
			videoView.setVideoURI(Uri.parse(VDO_URL));
			Log.e("yesyes", VDO_URL);
			// Toast.makeText(context, VDO_URL, Toast.LENGTH_LONG).show();
			videoView.setMediaController(new MediaController(this));
			videoView.requestFocus();
			videoView.start();

			videoView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);

				}
			});
		} else {
            thumb.setVisibility(View.VISIBLE);
			overlay.setVisibility(View.VISIBLE);

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	void showPhotoDetails(String postId, String imgUrl, String caption) {

		Intent photoDetailIntent = new Intent(context,
				PhotoDetailActivity.class);
		photoDetailIntent.putExtra("post_id", postId);
		photoDetailIntent.putExtra("url", imgUrl);
		photoDetailIntent.putExtra("caption", caption);
		startActivity(photoDetailIntent);
	}

	public void getComment(String id, String type) {
		String postId = id;
		String postType = type;
		String startPoint = "0";
		String sizePage = "20";
		url = "https://armymax.com/api/?action=getComment_rev";
		url += "&token=" + DataUser.VM_USER_TOKEN + "&postID=" + postId
				+ "&postType=" + postType + "&startPoint=" + startPoint
				+ "&sizePage=" + sizePage;

		aq.ajax(url, JSONObject.class, context, "getCommentCb");
		Log.e("mymyurl", url);
	}

	public void refresh() {
		getComment(POST_ID, POST_TYPE);
	}

	public void lovePostCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			int jsonStatus = jo.getInt("status");
			if (jsonStatus == 8001) {
				Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_SHORT)
						.show();
				int n = Integer.parseInt(loveCount.getText().toString());

				if (jo.getInt("Love_status") == 0) {
					n++;
					loveCount.setText(n + "");
					list.get(index).setLoveStatus("1");
				} else {
					n--;
					loveCount.setText(n + "");
					list.get(index).setLoveStatus("0");
				}

			}

		}
	}

	public void postCommentCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			int ajaxStatus = jo.getInt("status");
			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
					.show();
			if (ajaxStatus == 5101) {
				commentBox.setText("");
				String commentText = COMMENT_TEXT;
				String ago = "moments ago";
				String name = jo.getString("UserFirstName") + " "
						+ jo.getString("UserLastName");
				String avatar = jo.getString("UserAvatarPath");

				if (!avatar.contains("facebook"))
					avatar = Data.BASE + avatar;
				DataComment c = new DataComment(avatar, name, ago, commentText);
				commentList.add(c);

				int n = Integer.parseInt(commentCount.getText().toString());
				n++;
				commentCount.setText(n + "");

				new AdapterHelper().update((ArrayAdapter<DataComment>) adapter,
						new ArrayList<Object>(commentList));
				adapter.notifyDataSetChanged();
				// adapter.updateCommentList(commentList);
				ListUtil.setListViewHeightBasedOnChildren(listView);
			} else {

			}
		}

	}

	public void getCommentCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null && jo.optJSONArray("Comment") != null) {
			JSONArray ja = jo.optJSONArray("Comment");

			for (int i = 0; i < ja.length(); i++) {
				JSONObject comment = ja.getJSONObject(i);
				String id = comment.getString("ID");
				String postType = comment.getString("PostType");
				String postId = comment.getString("PostID");

				String commentText = comment.getString("Text");
				String ago = comment.getString("Time");
				String name = comment.getString("UserFirstName") + " "
						+ comment.getString("UserLastName");
				String avatar = comment.getString("UserAvatarPath");

				PrettyTime p = new PrettyTime();
				long agoLong = Integer.parseInt(ago);
				Date timeAgo = new java.util.Date((long) agoLong * 1000);
				ago = p.format(timeAgo);

				if (!avatar.contains("facebook"))
					avatar = Data.BASE + avatar;

				DataComment c = new DataComment(avatar, name, ago, commentText);
				commentList.add(c);

			}
		}

		adapter = new CommentAdapter(commentList, this);
		listView.setAdapter(adapter);

		// new AdapterHelper().update((ArrayAdapter<DataComment>) adapter,
		// new ArrayList<Object>(commentList));
		// adapter.notifyDataSetChanged();
		ListUtil.setListViewHeightBasedOnChildren(listView);

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed in the action
			// bar.
			// Create a simple intent that starts the hierarchical parent
			// activity and
			// use NavUtils in the Support Package to ensure proper handling of
			// Up.
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so
				// create a new task
				// with a synthesized back stack.
				TaskStackBuilder.from(this)
				// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
				finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(playerViewController != null && POST_TYPE.equals("2")) {
            playerViewController.engineStop();
            playerViewController = null;
        }
    }

	@Override
	public void onPause() {
		super.onPause();
		stopPosition = videoView.getCurrentPosition(); // stopPosition is an int
        if(playerViewController != null && POST_TYPE.equals("2")) {
            //playerViewController.engineStop();
            playerViewController = null;
        }

		// videoView.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		videoView.seekTo(stopPosition);
		videoView.start();
        if(playerViewController != null || POST_TYPE.equals("2"))
            playerViewController.engineResume();
        else
            initRTMPPlayer();
		// sure
	}

    public void initRTMPPlayer() {
        playerViewController = new PlayerViewControllerRTMP(
                this, overlay);
        playerViewController.initPlayerView();
        playerViewController.startPlay(postUsername, postUsername);
    }

	@Override
	public void recreate() {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			super.recreate();
		} else {
			startActivity(getIntent());
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
