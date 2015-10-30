package com.ame.armymax.xwalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceResponse;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.androidbegin.armymax.chat.AChatActivity;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

public class XWalkActivity extends Activity {

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

	private XWalkView mXwalkView;
	String url;
	String title;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conference);

		statusCheck();
		
		url = getIntent().getExtras().getString("url");
		title = getIntent().getExtras().getString("title");
		
		if(title != null || title != "") {
			getActionBar().setTitle(title);
		} else {
			getActionBar().setTitle(url);
		}
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.e("XwalkURL",url);
		//Toast.makeText(this, url, Toast.LENGTH_LONG).show();

		mXwalkView = (XWalkView) findViewById(R.id.conference);
		mXwalkView.setResourceClient(new MyResourceClient(mXwalkView));
		mXwalkView.setUIClient(new MyUIClient(mXwalkView));
		//mXwalkView.setGeolocationEnabled(true);
		mXwalkView.load(url, null);
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
		case android.R.id.home:
			Intent upIntent = new Intent(this, AChatActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;

		case R.id.action_refresh:
			mXwalkView.load(url, null);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		Intent upIntent = new Intent(this, AChatActivity.class);
		startActivity(upIntent);
		

	}

}
