package com.ame.armymax.xwalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.WebResourceResponse;
import android.widget.Button;

import com.ame.armymax.LivePostActivity;
import com.ame.armymax.LoginActivity;
import com.ame.armymax.R;
import com.ame.armymax.SettingsActivity;
import com.ame.armymax.model.DataUser;
import com.androidbegin.armymax.chat.AChatActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class XWalkChatRoomActivity extends Activity implements GeolocationPermissions.Callback {

	@Override
	public void invoke(String s, boolean b, boolean b1) {

	}

	class MyResourceClient extends XWalkResourceClient {
		MyResourceClient(XWalkView view) {
			super(view);
		}

		@Override
		public WebResourceResponse shouldInterceptLoadRequest(XWalkView view,
				String url) {
			// TODO Auto-generated method stub
			return super.shouldInterceptLoadRequest(view, url);

		}


	}

	class MyUIClient extends XWalkUIClient {
		MyUIClient(XWalkView view) {
			super(view);
		}

		@Override
		public void onFullscreenToggled(XWalkView view, boolean enterFullscreen) {
			// TODO Auto-generated method stub
			super.onFullscreenToggled(view, enterFullscreen);
		}

	}

	public void statusCheck()
	{
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			buildAlertMessageNoGps();

		}


	}
	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,  final int id) {
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();

	}
	
	AQuery aq;

	private XWalkView mXwalkView;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

        //mXwalkView.isInEditMode();

		statusCheck();

		XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
		
		Intent myIntent = getIntent(); // gets the previously created intent
		String id = myIntent.getStringExtra("userId");
		String name = myIntent.getStringExtra("friendName");
		String url = myIntent.getStringExtra("roomUrl");
		String notiUrl = myIntent.getStringExtra("notiUrl");
		getActionBar().setTitle(name);
		
		Button endCall = (Button) findViewById(R.id.button_end_call);
		
		context = this;
		aq = new AQuery(context);
		
		if(notiUrl != "" || notiUrl != null) {
			aq.ajax(notiUrl, JSONObject.class, this, "notiCb");
		}
		
		endCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							Intent i = new Intent(XWalkChatRoomActivity.this,
									AChatActivity.class);
							finish();
							startActivity(i);
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							dialog.dismiss();
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("ต้องการออกจากการสนทนา")
						.setPositiveButton("ใช่", dialogClickListener)
						.setNegativeButton("ไม่", dialogClickListener).show();
				
			}
		});
		
		
		mXwalkView = (XWalkView) findViewById(R.id.conference);
		mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
		mXwalkView.setUIClient(new MyUIClient(mXwalkView));
		//mXwalkView.getSettings().setJavaScriptEnabled(true);
        //mXwalkView.getSettings().setGeolocationEnabled(true);
		mXwalkView.load(url, null);

		//mXwalkView.setXWalkWebChromeClient(new XWalkWebChromeClient());

		//Toast.makeText(context, url, Toast.LENGTH_LONG).show();
		Log.e("chatRoomUrl", url);
		
	}
	
	public void notiCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		
		//Log.e("myjson",jo.toString());

	}

	@Override
	protected void onPause() {
		super.onPause();
		DataUser.setContext(null);
		if (mXwalkView != null) {
			mXwalkView.pauseTimers();
			mXwalkView.onHide();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		DataUser.setContext(this);
		if (mXwalkView != null) {
			mXwalkView.resumeTimers();
			mXwalkView.onShow();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mXwalkView != null) {
			mXwalkView.onDestroy();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mXwalkView != null) {
			mXwalkView.onActivityResult(requestCode, resultCode, data);
		}
		/*
		 * mXwalkView.load("http://150.107.31.5/dev/app/conference.php?room=test"
		 * , null); mXwalkView.load("http://aquariospace.com:3000/room/main/",
		 * null); mXwalkView.load("https://tawk.com/armymax", null);
		 * mXwalkView.load("https://talky.io/armymax", null);
		 */
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (mXwalkView != null) {
			mXwalkView.onNewIntent(intent);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {

		case R.id.action_live:

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Intent i = new Intent(XWalkChatRoomActivity.this,
								LivePostActivity.class);
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

			return true;
		case R.id.action_setting:
			Intent i2 = new Intent(XWalkChatRoomActivity.this, SettingsActivity.class);
			startActivity(i2);
			finish();
			return true;
		case R.id.action_logout:
			Intent i3 = new Intent(XWalkChatRoomActivity.this, LoginActivity.class);
			startActivity(i3);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.xwalk, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		/*
		Intent i = new Intent(ChatRoomXWalkActivity.this,
				AChatActivity.class);
		finish();
		startActivity(i);
		*/

	}

}
