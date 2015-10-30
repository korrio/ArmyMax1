package com.ame.armymax.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.VideoDetailActivity;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.post.PostVideoActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.PrefUtility;
import com.androidquery.util.AQUtility;

public class WorkingUploadVideoService extends IntentService {

	private static final int PROCESSING_TIMEOUT_SEC = 60 * 20; // 20 minutes

	private static final int PROCESSING_POLL_INTERVAL_SEC = 60;

	private static final int UPLOAD_REATTEMPT_DELAY_SEC = 60;

	private static long mStartTime;

	private int mUploadAttemptCount;

	private int MAX_RETRY = 1;
	AQuery aq;

	Uri fileUri;
	String token;
	String title;
	String desc;
	String type;

	Context context;

	public WorkingUploadVideoService() {
		super("UploadVideo");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		aq = new AQuery(this);

		fileUri = intent.getData();
		token = intent.getStringExtra("token");
		title = intent.getStringExtra("title");
		desc = intent.getStringExtra("desc");
		type = intent.getStringExtra("type");

		Toast.makeText(getApplicationContext(), (CharSequence) fileUri,
				Toast.LENGTH_SHORT).show();

		File vdo = new File(fileUri.getPath());
		videoPost(vdo);

	}

	private void videoPost(final File vdo) {

		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {

				try {
					Log.d("debugfail", "try");
					videoPost(getMessage(), "Description", vdo);
					Toast.makeText(getApplicationContext(), "try",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Log.d("debugfail", "catch");
					AQUtility.report(e);
					failed("catch");
					Toast.makeText(getApplicationContext(), "catch",
							Toast.LENGTH_SHORT).show();
				}
				return null;
			}
		};

		task.execute();

	}

	private void videoPost(String message, String desc, File file) {

		String url = "https://api.vdomax.com/service/postVideo/mobile";

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("token", DataUser.VM_USER_TOKEN);
		params.put("video", file);
		params.put("title", message);
		params.put("desc", desc);
		params.put("type", "video");

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Uploading video file...");
		aq.progress(dialog)
				.ajax(url, params, JSONObject.class, this, "videoCb");

	}

	public void videoCb(String url, JSONObject jo, AjaxStatus status) {

		AQUtility.debug(jo);

		if (jo != null && jo.optInt("status") == 4001) {

			uploaded(jo.optString("msg"), jo.optString("testStream"));

		} else {

			failed(jo.optString("msg"));
		}

	}

	private void uploaded(String tb, String ts) {

		
		 Intent notificationIntent = new Intent(this, VideoDetailActivity.class);
		 notificationIntent.putExtra("testStream", ts); 
		 String completed = "Finnnn";
		 notify(completed,"fin makkk", completed, notificationIntent);
		 
	}

	private String getMessage() {
		// TODO Auto-generated method stub
		return DataUser.VM_NAME + "'s video";
	}

	private void failed(String statusMsg) {
		Intent intent = new Intent(this, PostVideoActivity.class);

		String failed = getString(R.string.error);
		notify(failed, getString(R.string.action_post), statusMsg, intent);

	}

	private void notify(String ticker, String title, String message,
			Intent intent) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, ticker, when);

		Context context = getApplicationContext();
		CharSequence contentText = message;

		int id = getNotifyId();
		PendingIntent contentIntent = PendingIntent.getActivity(this, id,
				intent, 0);

		notification.setLatestEventInfo(context, title, contentText,
				contentIntent);

		mNotificationManager.cancelAll();

		AQUtility.debug("notify id", id);
		mNotificationManager.notify(id, notification);

	}

	private int getNotifyId() {

		long n = PrefUtility.getLong("1111", 1000L);

		n++;
		PrefUtility.put("1111", n);

		return (int) Math.abs(n % 10000);

	}

	private void tryUploadAndShowSelectableNotification(final Uri fileUri)
			throws InterruptedException {
		while (true) {
			Log.i("test",
					String.format("Uploading [%s] to YouTube",
							fileUri.toString()));
			String videoId = tryUpload(fileUri);
			if (videoId != null) {
				Log.i("test",
						String.format("Uploaded video with ID: %s", videoId));
				tryShowSelectableNotification(videoId);
				return;
			} else {
				Log.e("test", String.format("Failed to upload %s",
						fileUri.toString()));
				if (mUploadAttemptCount++ < MAX_RETRY) {
					Log.i("test",
							String.format(
									"Will retry to upload the video ([%d] out of [%d] reattempts)",
									mUploadAttemptCount, MAX_RETRY));
					zzz(UPLOAD_REATTEMPT_DELAY_SEC * 1000);
				} else {
					Log.e("test",
							String.format(
									"Giving up on trying to upload %s after %d attempts",
									fileUri.toString(), mUploadAttemptCount));
					return;
				}
			}
		}
	}

	private void tryShowSelectableNotification(final String videoId)
			throws InterruptedException {
		mStartTime = System.currentTimeMillis();
		boolean processed = false;
		while (!processed) {
			processed = true;
			if (!processed) {
				// wait a while
				Log.d("test",
						String.format(
								"Video [%s] is not processed yet, will retry after [%d] seconds",
								videoId, PROCESSING_POLL_INTERVAL_SEC));
				if (!timeoutExpired(mStartTime, PROCESSING_TIMEOUT_SEC)) {
					zzz(PROCESSING_POLL_INTERVAL_SEC * 1000);
				} else {
					Log.d("test",
							String.format(
									"Bailing out polling for processing status after [%d] seconds",
									PROCESSING_TIMEOUT_SEC));
					return;
				}
			} else {
				// show notification
				return;
			}
		}
	}

	private static void zzz(int duration) throws InterruptedException {
		Log.d("test", String.format("Sleeping for [%d] ms ...", duration));
		Thread.sleep(duration);
		Log.d("test", String.format("Sleeping for [%d] ms ... done", duration));
	}

	private String tryUpload(Uri mFileUri) throws InterruptedException {
		tryUploadAndShowSelectableNotification(mFileUri);
		long fileSize;
		InputStream fileInputStream = null;
		String videoId = null;
		try {
			fileSize = getContentResolver().openFileDescriptor(mFileUri, "r")
					.getStatSize();
			fileInputStream = getContentResolver().openInputStream(mFileUri);
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(mFileUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();

			videoId = "11";

		} catch (FileNotFoundException e) {
			Log.e(getApplicationContext().toString(), e.getMessage());
		} finally {
			try {
				fileInputStream.close();
			} catch (IOException e) {
				// ignore
			}
		}
		return videoId;
	}

	private static boolean timeoutExpired(long startTime, int timeoutSeconds) {
		long currTime = System.currentTimeMillis();
		long elapsed = currTime - startTime;
		if (elapsed >= timeoutSeconds * 1000) {
			return true;
		} else {
			return false;
		}
	}

}
