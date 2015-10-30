package com.ame.armymax.post;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ame.armymax.LoginActivity;
import com.ame.armymax.R;
import com.ame.armymax.MainActivity;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

public class PostStatusAcitivity extends Activity implements OnClickListener {

	Context context;
	AQuery aq;

	EditText status;
	Button button_post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_post_status);

		context = this;

		aq = new AQuery(context);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		status = (EditText) findViewById(R.id.comment_box);
		button_post = (Button) findViewById(R.id.button_recent);

		button_post.setOnClickListener(this);


		getActionBar().setTitle("โพสข้อความ");

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

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.button_recent) {
			String statusText = status.getText().toString()
					.replace("\n", "%0A");
			//statusText.toString().trim().replaceAll("\\s+", " ");
			
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(statusText);
			boolean found = matcher.find();
			boolean isWhitespace = statusText.matches("^\\s*$");
			
			if (statusText.length() == 0 || statusText.trim() == "" || found || isWhitespace) {
				status.setError("กรุณาพิมพ์ข้อความก่อนส่ง");
				Log.e("YEAH", statusText.length() + " " + statusText.trim() + " " + found + " " + isWhitespace);
			} else {
				
				
				String url = "http://www.armymax.com/api/?action=postText&"
						+ "token=" + DataUser.VM_USER_TOKEN + "&text="
						+ statusText;

				ProgressDialog dialog = new ProgressDialog(context);
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setInverseBackgroundForced(false);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setTitle("Loading...");
				dialog.setMessage("กำลังโพสข้อความ");
				aq.progress(dialog).ajax(url, JSONObject.class, this, "textCb");

			}
		}

	}

	public void textCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Log.e("check", jo.toString());

			String ajaxStatus = jo.getString("status");
			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
					.show();
			if (ajaxStatus.equals("5001")) {
				Intent intent = new Intent(PostStatusAcitivity.this,
						MainActivity.class);
				intent.putExtra("refresh", 1);
				startActivity(intent);
			} else {
				DataUser.clearAll();
				Intent logout = new Intent(
						PostStatusAcitivity.this.getApplicationContext(),
						LoginActivity.class);
				startActivity(logout);
			}
		}
	}
}
