package com.ame.armymax.post;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.LoginActivity;
import com.ame.armymax.R;
import com.ame.armymax.MainActivity;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.pref.UserHelper;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostPhotoActivity extends Activity implements OnClickListener {

	Context context;
	Button select_photo;
	Button take_photo;
	Button post_photo;
	ImageView imageView;
	LinearLayout imageInfoLayout;
	TextView imageHeightTextView;
	TextView imageWidthTextView;
	EditText photoText;

	static final int REQUEST_TAKE_PHOTO = 1;
	static final int REQUEST_CHOOSE_PHOTO = 2;

	File tempFile;

	AQuery aq;

	private int imageWidth = 640;
	private int imageHeight = 480;

	Bitmap bmpByteArray;

	private int progress = 0;
	private RemoteViews view = null;
	private Notification notification = new Notification();
	private NotificationManager manager = null;
	private Intent intent = null;
	private PendingIntent pIntent = null;// 更新显示


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_post_photo);

		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		view = new RemoteViews(getPackageName(), R.layout.custom_dialog);
		intent = new Intent(PostPhotoActivity.this, MainActivity.class);
		pIntent = PendingIntent
				.getService(PostPhotoActivity.this, 0, intent, 0);

		Bundle extras = getIntent().getExtras();
		String path = extras.getString("photo");
		String rotate = extras.getString("rotate");
		byte[] byteArray = getIntent().getByteArrayExtra("byteArray");
		if (byteArray != null) {
			bmpByteArray = BitmapFactory.decodeByteArray(byteArray, 0,
					byteArray.length);

		} else {
			bmpByteArray = BitmapFactory.decodeFile(path);
		}
		bmpByteArray = RotateBitmap(bmpByteArray, Integer.parseInt(rotate));

		tempFile = new File(path);

		context = this;
		aq = new AQuery(context);

		select_photo = (Button) findViewById(R.id.button_photo);
		take_photo = (Button) findViewById(R.id.button_video);
		post_photo = (Button) findViewById(R.id.button_recent);

		imageView = (ImageView) findViewById(R.id.image);

		// Bitmap b = BitmapFactory.decodeFile(path);

		imageView.setImageBitmap(bmpByteArray);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			bmpByteArray.compress(Bitmap.CompressFormat.PNG, 85, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		photoText = (EditText) findViewById(R.id.comment_box);

		select_photo.setOnClickListener(this);
		take_photo.setOnClickListener(this);
		post_photo.setOnClickListener(this);

		getActionBar().setTitle("แบ่งปันรูปภาพ");
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public Bitmap decodeFile(String path) {// you can provide file path here
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 0;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeFile(path, o2);
			Bitmap bitmap = bm;

			ExifInterface exif = new ExifInterface(path);

			orientation = exif
					.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

			Log.e("ExifInteface .........", "rotation =" + orientation);

			// exif.setAttribute(ExifInterface.ORIENTATION_ROTATE_90, 90);

			Log.e("orientation", "" + orientation);
			Matrix m = new Matrix();

			if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
				m.postRotate(180);
				// m.postScale((float) bm.getWidth(), (float) bm.getHeight());
				// if(m.preRotate(90)){
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				m.postRotate(90);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				m.postRotate(270);
				Log.e("in orientation", "" + orientation);
				bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
						bm.getHeight(), m, true);
				return bitmap;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return true;
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

	long totalSize;

	@Override
	public void onClick(View v) {


		if (v.getId() == R.id.button_recent) {


            String statusText =  photoText.getText().toString()
                    .replace("\n", "%0A");
            //statusText.toString().trim().replaceAll("\\s+", " ");

            Pattern pattern = Pattern.compile("\\s");
            Matcher matcher = pattern.matcher(statusText);
            boolean found = matcher.find();
            boolean isWhitespace = statusText.matches("^\\s*$");

            if (statusText.length() == 0 || statusText.trim() == "" || found || isWhitespace) {
                photoText.setError("กรุณาพิมพ์ข้อความก่อนส่ง");
                Log.e("YEAH", statusText.length() + " " + statusText.trim() + " " + found + " " + isWhitespace);
            }

            String url = "http://www.armymax.com/api/?action=postPhoto";
            statusText = photoText.getText().toString();

			// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("text", statusText);
			params.put("token", DataUser.VM_USER_TOKEN);
			params.put("photo", tempFile);

			final ProgressDialog dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setInverseBackgroundForced(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setTitle("Uploading");
			dialog.setMessage("กำลังอัพโหลดรูปภาพ..");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setIndeterminate(false);
			dialog.setMax(100);

			final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			final Builder mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setContentTitle("Photo upload")
					.setContentText("Upload in progress")
					.setSmallIcon(R.drawable.ic_launcher);

			new Thread(new Runnable() {
				@Override
				public void run() {
					int incr;
					// Do the "lengthy" operation 20 times
					for (incr = 0; incr <= 100; incr += 19) {
						// Sets the progress indicator to a max value, the
						// current completion percentage, and "determinate"
						// state
						dialog.setIndeterminate(false);
						dialog.setMax(100);
						dialog.setProgress((int) incr);
						mBuilder.setProgress(100, incr, false);
						// Displays the progress bar for the first time.
						mNotifyManager.notify(1001, mBuilder.build());
						// Sleeps the thread, simulating an operation
						// that takes time
						try {
							// Sleep for 5 seconds
							Thread.sleep(5 * 1000);
						} catch (InterruptedException e) {
							Log.d("LOGME", "sleep failure");
						}
					}
					// When the loop is finished, updates the notification
					mBuilder.setContentText("Upload complete")
					// Removes the progress bar
							.setProgress(0, 0, false);
					mNotifyManager.notify(1001, mBuilder.build());
				}
			}
			// Starts the thread by calling the run() method in its Runnable
			).start();

			aq.progress(dialog).ajax(url, params, JSONObject.class, this,
					"postPhotoCb");

			/*
			 * notification.icon = R.drawable.ic_launcher;
			 * view.setImageViewResource(R.id.image, R.drawable.ic_launcher);
			 * new Thread(new Runnable() {
			 * 
			 * @Override public void run() { for (int i = 0; i < 20; i++) {
			 * progress = (i + 1) * 5; try { if (i < 19) { Thread.sleep(1000); }
			 * else { Thread.currentThread().interrupt(); } } catch
			 * (InterruptedException e) { e.printStackTrace(); } Message msg =
			 * new Message(); handler.sendMessage(msg); } } }).start();
			 */

		}

	}



	public void postPhotoCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Log.e("check", jo.toString());
			int ajaxStatus = jo.getInt("status");
			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
					.show();

			if (ajaxStatus == 4001) {
				Intent intent = new Intent(PostPhotoActivity.this,
						MainActivity.class);
				startActivity(intent);

			} else {
				DataUser.clearAll();
				Intent logout = new Intent(PostPhotoActivity.this,
						LoginActivity.class);
				UserHelper user = new UserHelper(this);
				user.deleteSession();
				startActivity(logout);
			}
		}
	}

}
