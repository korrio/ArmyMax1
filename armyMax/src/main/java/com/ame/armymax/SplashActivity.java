package com.ame.armymax;

import com.ame.armymax.R;
import com.ame.armymax.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		int SPLASH_DISPLAY_TIME = 3000;
		/*
		 * new CountDownTimer(SPLASH_DISPLAY_TIME, SPLASH_DISPLAY_TIME) {
		 * 
		 * @Override public void onFinish() {
		 * 
		 * Intent viewActivity = new Intent(SplashActivity.this,
		 * LoginActivity.class); viewActivity.putExtra("state", "init");
		 * startActivity(viewActivity);
		 * 
		 * }
		 * 
		 * @Override public void onTick(long millisUntilFinished) {
		 * 
		 * }
		 * 
		 * }.start();
		 */

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent mainIntent = new Intent(SplashActivity.this,
						LoginActivity.class);
				mainIntent.putExtra("state", "init");

				startActivity(mainIntent);
				SplashActivity.this.finish();

			}
		}, SPLASH_DISPLAY_TIME);

	}
}
