package com.ame.armymax.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

import com.ame.armymax.R;
import com.ame.armymax.MainActivity;

public class PlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surfaceview);
		
		PlayerViewController playerViewController = new PlayerViewController(this,null);
		playerViewController.initPlayerView();
		
		Intent intent = getIntent();
		String isPlay = intent.getStringExtra("isPlay");
		String roomId = intent.getStringExtra("roomId");
		String roomTag = intent.getStringExtra("roomTag");
		
		if("1".equals(isPlay)){
			playerViewController.startPlay(roomTag, roomId);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

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
	}

}
