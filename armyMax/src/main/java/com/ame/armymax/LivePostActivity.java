package com.ame.armymax;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.R.id;
import com.ame.armymax.R.layout;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class LivePostActivity extends Activity {

	Context context;
	AQuery aq;

	BootstrapEditText titleText;
	BootstrapEditText descText;

	BootstrapButton button_live;
	ProgressBar progress;

	String myTitle;
	String myDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live);

		context = this;

		aq = new AQuery(context);

		getActionBar().setTitle("ถ่ายทอดสด");

		titleText = (BootstrapEditText) findViewById(R.id.room_name);
		descText = (BootstrapEditText) findViewById(R.id.desc_txt);

		button_live = (BootstrapButton) findViewById(R.id.button_live);

		progress = (ProgressBar) findViewById(R.id.progress);

		button_live.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				postLive(titleText.getEditableText().toString(), descText
						.getEditableText().toString());

			}
		});
	}

	public void postLive(String title, String desc) {
		String url = "http://www.armymax.com/api/?action=postLive";

		Map<String, Object> params = new HashMap<String, Object>();

		myTitle = title;
		myDesc = desc;

		String myTitle = title.toString().replace("\n", "%0A");
		String myDesc = desc.toString().replace("\n", "%0A");

		if (!myTitle.equals("") && !myDesc.equals("")) {
			params.put("token", DataUser.VM_USER_TOKEN);
			params.put("title", title);
			params.put("desc", desc);
			params.put("source", "rtmp://" + Data.LIVE + ":1935/live/"
					+ DataUser.VM_USER_NAME);

			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setInverseBackgroundForced(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setTitle("Initializing Livestreaming...");
			dialog.setMessage("กำลังสร้างห้องถ่ายทอดสด");
			aq.progress(dialog).ajax(url, params, JSONObject.class, this,
					"liveCb");

			button_live.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);

		} else {
			Toast.makeText(context, "กรุณากรอกให้ครบ",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void liveCb(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			Toast.makeText(context, json.optString("msg"), Toast.LENGTH_LONG)
					.show();
			if (json.optInt("status") == 5101) {

				Intent startLive = new Intent(LivePostActivity.this,
						LiveStableActivity.class);
				startLive.putExtra("title", myTitle);
				startLive.putExtra("desc", myDesc);
				finish();
				startActivity(startLive);

				Toast.makeText(context, "You are ready to Live",
						Toast.LENGTH_LONG).show();
			} else {

			}
		}
	}
}
