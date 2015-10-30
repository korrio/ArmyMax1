package com.ame.armymax;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.model.DataUser;
import com.ame.armymax.model.UserManager;
import com.ame.armymax.pref.UserHelper;
import com.ame.armymax.service.UpdateService;
import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.JsonUtility;
import com.parse.ParseInstallation;

public class LoginActivity extends Activity {

	static Context context;

	EditText txtUsername;
	EditText txtPassword;
	CheckBox rememberMe;
	Button loginButton;
	RelativeLayout connectFbButton;
	Button registerButton;
	TextView versionCode;

	UserHelper user;

	AQuery aq;
	AQuery aq2;

	boolean newUpdate = false;

	private FacebookHandle handle;
	private UserManager mManager;

	public String VERSION;

	public static String APP_ID = "391414774312517";
	public static String APP_PERMISSIONS = "read_stream,read_friendlists,manage_friendlists,manage_notifications,publish_stream,publish_checkins,offline_access,user_about_me,friends_about_me,user_activities,friends_activities,user_checkins,friends_checkins,user_events,friends_events,user_groups,friends_groups,user_interests,friends_interests,user_likes,friends_likes,user_notes,friends_notes,user_photos,friends_photos,user_status,friends_status,user_videos,friends_videos";

	public static Context getContext() {
		return context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			VERSION = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context = this;
		DataUser.context = context;

		boolean logout = getIntent().getBooleanExtra("logout", false);

		user = new UserHelper(this);
		if (user.isLogin() && !logout) {
			DataUser.VM_USER_NAME = user.getUserName();
			DataUser.VM_USER_TOKEN = user.getToken();
			DataUser.VM_USER_ID = user.getUserID();
			DataUser.VM_NAME = user.getName();
			DataUser.VM_USER_STREAM = DataUser.STREAMER + DataUser.VM_USER_NAME;
			

			// check token with getProfileInfo if session expire logout and say
			// session expire
			Intent main = new Intent(this, MainActivity.class);
			finish();
			startActivity(main);
		} else {
			if (user.isRemember()) {
				txtUsername.setText(user.getUserName());
				rememberMe.setChecked(true);
			}
		}

		aq = new AQuery(context);
		mManager = new UserManager(context);

		txtUsername = (EditText) findViewById(R.id.username_login);
		txtPassword = (EditText) findViewById(R.id.password_login);
		rememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
		loginButton = (Button) findViewById(R.id.login_button);
		connectFbButton = (RelativeLayout) findViewById(R.id.connect_facebook_button);
		versionCode = (TextView) findViewById(R.id.version_code);
		versionCode.setText("v" + VERSION);

		registerButton = (Button) findViewById(R.id.register_button);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String user = txtUsername.getText().toString().trim();
				String pass = txtPassword.getText().toString().trim();
				aqLogin(user, pass);

				if (rememberMe.isChecked()) {
					mManager.registerUser(user, pass);
				}
			}
		});

		connectFbButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				auth_facebook();

			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this,
						SignupArmyActivity.class);
				startActivity(i);

			}
		});

		checkUpdate();

	}

	public void checkUpdate() {

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle("ตรวจสอบเวอร์ชั่น...");

		aq.progress(dialog).ajax("https://www.armymax.com/apk/update.php",
				JSONObject.class, this, "updateCb");
	}

	UpdateService atualizaApp;

	public void updateCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {

			Log.e("updatetag", jo.optString("update"));

			if (jo.optString("update").equals("1")
					&& !jo.optString("version").equals(VERSION)) {
				Toast.makeText(context, "กำลังดาวน์โหลดอัพเดท",
						Toast.LENGTH_SHORT).show();

				// declare the dialog as a member field of your activity
				ProgressDialog mProgressDialog;

				// instantiate it within the onCreate method
				mProgressDialog = new ProgressDialog(LoginActivity.this);
				mProgressDialog.setMessage("Downloading update");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				mProgressDialog.setCancelable(false);

				// execute this when the downloader must be fired

				mProgressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								atualizaApp.cancel(true);
							}
						});

				atualizaApp = new UpdateService();
				atualizaApp
						.setContext(getApplicationContext(), mProgressDialog);
				atualizaApp.execute(jo.optString("downloadURL"));
			} else {
				Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static FacebookHandle makeHandle(Activity act) {
		FacebookHandle handle = new FacebookHandle(act, APP_ID, APP_PERMISSIONS);
		return handle;
	}

	public void auth_facebook() {
		handle = new FacebookHandle(this, APP_ID, APP_PERMISSIONS);
		String url = "https://graph.facebook.com/me";
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Authenicating...");

		aq.auth(handle).progress(dialog)
				.ajax(url, JSONObject.class, this, "facebookCb");
	}

	public void facebookCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {

			Log.e("myjson", jo.toString());

			DataUser.VM_USER_FB_ID = JsonUtility.getString(jo, "id");
			DataUser.VM_USER_FB_TOKEN = handle.getToken();
			DataUser.VM_USER_FB = JsonUtility.getString(jo, "username")
					.replace(".", "");
			DataUser.VM_USER_FB_EMAIL = DataUser.VM_USER_FB + "@facebook.com";
			DataUser.VM_USER_FB_FNAME = JsonUtility.getString(jo, "first_name");
			DataUser.VM_USER_FB_LNAME = JsonUtility.getString(jo, "last_name");
			DataUser.VM_IS_AUTHED = true;

			fbSignup();

		}
	}

	public void checkFbLogin() {
		String url = "https://www.armymax.com/api/?action=checkfacebooklogin";
		String fbId = DataUser.VM_USER_FB_ID;
		String fbToken = DataUser.VM_USER_FB_TOKEN;

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("fbUserID", fbId);
		params.put("fbToken", fbToken);

		aq.ajax(url, params, JSONObject.class, this, "fbCheckLogin");

	}

	public void fbCheckLogin(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG).show();
	}

	public void fbSignup() {
		checkFbLogin();

		String url = "http://www.armymax.com/api/?action=fbsignup";
		Map<String, Object> params = new HashMap<String, Object>();

		String fbId = DataUser.VM_USER_FB_ID;
		String username = DataUser.VM_USER_FB;
		String password = DataUser.VM_USER_FB_ID;
		String firstname = DataUser.VM_USER_FB_FNAME;
		String lastname = DataUser.VM_USER_FB_LNAME;
		String email = DataUser.VM_USER_FB_EMAIL;

		params.put("fbId", fbId);
		params.put("username", username);
		params.put("password", password);
		params.put("firstname", firstname);
		params.put("lastname", lastname);
		params.put("email", email);

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Logging in with " + DataUser.VM_USER_FB_FNAME + " "
				+ DataUser.VM_USER_FB_LNAME);
		aq.progress(dialog).ajax(url, params, JSONObject.class, this,
				"fbLoginCb");
	}

	public void fbLoginCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		// Log.e("fblogin", jo.toString());
		if (jo != null) {
			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_LONG)
					.show();
			if (jo.getInt("status") == 1001) {
				DataUser.VM_USER_TOKEN = jo.getString("token");
				DataUser.VM_USER_NAME = jo.getString("username");
				DataUser.VM_NAME = jo.getString("Name");
				DataUser.VM_USER_ID = jo.getString("userID");

				Intent finishLogin = new Intent(LoginActivity.this,
						MainActivity.class);
				finish();
				startActivity(finishLogin);
			}

		}
	}

	AsyncTask<Void, Void, Void> mRegisterTask;

	public void aqLogin(String username, String password) {

		String url = "http://www.armymax.com/api/?action=signin";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", password);

		DataUser.VM_USER_NAME = username;

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			int loginStatus;

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				loginStatus = json.optInt("status");
				if (loginStatus == 1001) {

					DataUser.VM_USER_TOKEN = json.optString("token");
					DataUser.VM_USER_ID = json.optString("userID");
					DataUser.VM_NAME = json.optString("Name");
					DataUser.VM_USER_STREAM = DataUser.STREAMER
							+ DataUser.VM_USER_NAME;

					user.createSession(DataUser.VM_USER_TOKEN,
							DataUser.VM_USER_ID, DataUser.VM_USER_NAME,
							DataUser.VM_NAME, rememberMe.isChecked());

					// ParseInstallation.getCurrentInstallation().saveInBackground();

					// Associate the device with a user
					ParseInstallation installation = ParseInstallation
							.getCurrentInstallation();

					// Toast.makeText(context, installation.toString(),
					// Toast.LENGTH_SHORT).show();

					installation.put("user_id", DataUser.VM_USER_ID);
					// installation.put("deviceToken",deviceToken);
					installation.saveInBackground();

					Intent main = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(main);
					finish();

					// Registering user on our server
					// Sending registraiton details to MainActivity
					/*
					 * i.putExtra("userId", DataUser.VM_USER_ID);
					 * i.putExtra("name", DataUser.VM_NAME); i.putExtra("email",
					 * DataUser.VM_NAME + "@armymax.com"); startActivity(i);
					 * finish();
					 */

				} else {
					Toast.makeText(context, json.optString("msg"),
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

}
