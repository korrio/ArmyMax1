/*
 *  Copyright (c) 2011 by Twilio, Inc., all rights reserved.
 *
 *  Use of this software is subject to the terms and conditions of 
 *  the Twilio Terms of Service located at http://www.twilio.com/legal/tos
 */

package com.ame.armymax.basicphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ame.armymax.R;
import com.ame.armymax.basicphone.BasicPhone.BasicConnectionListener;
import com.ame.armymax.basicphone.BasicPhone.BasicDeviceListener;
import com.ame.armymax.basicphone.BasicPhone.LoginListener;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PhoneCallActivity extends Activity implements LoginListener,
		BasicConnectionListener, BasicDeviceListener, View.OnClickListener,
		CompoundButton.OnCheckedChangeListener {
	private static final Handler handler = new Handler();

	private BasicPhone phone;

	private ImageButton mainButton;
	private ToggleButton speakerButton;
	private EditText logTextBox;
	private AlertDialog incomingAlert;

	String MC_TOKEN;
	String CAP_TOKEN;
	private static final String AUTH_PHP_SCRIPT = "http://maxcalling.com/api/call-begin";
	String CALL_TO;
	String CODE;

	AQuery aq;
	Context mContext;
	User user;

	boolean connected = false;
	boolean pressed = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maxcalling);

		mContext = this;

		CALL_TO = getIntent().getExtras().getString("number");
		CODE = getIntent().getExtras().getString("code");

		mainButton = (ImageButton) findViewById(R.id.main_button);
		mainButton.setOnClickListener(this);
		speakerButton = (ToggleButton) findViewById(R.id.speaker_toggle);
		speakerButton.setOnCheckedChangeListener(this);
		logTextBox = (EditText) findViewById(R.id.log_text_box);

		phone = BasicPhone.getInstance(getApplicationContext());
		phone.setListeners(this, this, this);
		// phone.login(mContext);

		aq = new AQuery(mContext);
		user = new User();

		Map<String, String> params = new HashMap<String, String>();
		params.put("prefix", "+66");
		params.put("password", DataUser.VM_USER_FLER_COUNT);
		params.put("phone",
				(DataUser.VM_USER_FLING_COUNT).replaceFirst("^0+(?!$)", ""));

		aq.ajax("http://maxcalling.com/api/login", params, JSONObject.class,
				mContext, "loginCb");

	}

	public void capTokenCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			Toast.makeText(mContext, jo.getString("result"), Toast.LENGTH_SHORT)
					.show();
			if (jo.getString("result").equals("OK")) {
				JSONObject val = jo.getJSONObject("value");
				CAP_TOKEN = val.optString("capabilityToken");
				phone.login(MC_TOKEN, CAP_TOKEN);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (!phone.isConnected()) {
							// BEGIN CALL
							String callTo = CALL_TO
									.replaceFirst("^0+(?!$)", "");
							addStatusMessage("UserId: " + user.getId()
									+ " Call from +66" + user.getPhone());
							addStatusMessage("Call to +66" + callTo);
							phone.connect(user, callTo);
						}
					}

				}, 2000);

			}
		}
	}

	public void loginCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		Log.e("jo", jo.toString());
		if (jo != null) {
			Toast.makeText(mContext, jo.getString("result"), Toast.LENGTH_SHORT)
					.show();
			if (jo.getString("result").equals("OK")) {

				JSONObject val = jo.getJSONObject("value");

				user.setToken(val.optString("token"));
				user.setId(val.optString("id"));
				user.setEmail(val.optString("email"));
				user.setPrefix(val.optString("prefix"));
				user.setPhone(val.optString("phone"));
				user.setCountry(val.optString("country"));
				user.setBalance(val.optString("balance"));

				MC_TOKEN = val.optString("token");
				aq.ajax(AUTH_PHP_SCRIPT + "?token=" + MC_TOKEN,
						JSONObject.class, mContext, "capTokenCb");

			} else {
				Toast.makeText(mContext,
						jo.getString("result") + ":" + jo.getString("value"),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (phone.handleIncomingIntent(getIntent())) {
			showIncomingAlert();
			addStatusMessage(R.string.got_incoming);
			syncMainButton();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (phone != null) {
			phone.setListeners(null, null, null);
			phone = null;
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.main_button) {
			pressed = true;
			if (!phone.isConnected()) {
				// BEGIN CALL
				String callTo = CODE + CALL_TO.replaceFirst("^0+(?!$)", "");
				;
				addStatusMessage("UserId: " + user.getId() + " Call from +66"
						+ user.getPhone());
				addStatusMessage("Call to " + callTo);
				phone.connect(user, callTo);
			} else {
				phone.disconnect();

			}

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean isChecked) {
		if (button.getId() == R.id.speaker_toggle)
			phone.setSpeakerEnabled(isChecked);
		Toast.makeText(getApplicationContext(),"Speaker: " + isChecked,Toast.LENGTH_LONG).show();
	}

	private void addStatusMessage(final String message) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				logTextBox.append('-' + message + '\n');
			}
		});
	}

	private void addStatusMessage(int stringId) {
		addStatusMessage(getString(stringId));
	}

	private void syncMainButton() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				if (phone.isConnected()) {
					
					switch (phone.getConnectionState()) {
					case DISCONNECTED:
						mainButton.setImageResource(R.drawable.idle);
						finish();
						break;
					case CONNECTED:
						mainButton.setImageResource(R.drawable.inprogress);
						break;
					default:
						mainButton.setImageResource(R.drawable.dialing);
						break;
					}
				} else if (phone.hasPendingConnection())
					mainButton.setImageResource(R.drawable.dialing);
				else
					mainButton.setImageResource(R.drawable.idle);
			}
		});
	}

	private void showIncomingAlert() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (incomingAlert == null) {
					incomingAlert = new AlertDialog.Builder(
							PhoneCallActivity.this)
							.setTitle(R.string.incoming_call)
							.setMessage(R.string.incoming_call_message)
							.setPositiveButton(R.string.answer,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											phone.acceptConnection();
										}
									})
							.setNegativeButton(R.string.ignore,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											phone.ignoreIncomingConnection();
										}
									})
							.setOnCancelListener(
									new DialogInterface.OnCancelListener() {
										@Override
										public void onCancel(
												DialogInterface dialog) {
											phone.ignoreIncomingConnection();
										}
									}).create();
					incomingAlert.show();
				}
			}
		});
	}

	private void hideIncomingAlert() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (incomingAlert != null) {
					incomingAlert.dismiss();
					incomingAlert = null;
				}
			}
		});
	}

	@Override
	public void onLoginStarted() {
		addStatusMessage(R.string.logging_in);
	}

	@Override
	public void onLoginFinished() {
		addStatusMessage(phone.canMakeOutgoing() ? R.string.outgoing_ok
				: R.string.no_outgoing_capability);
		addStatusMessage(phone.canAcceptIncoming() ? R.string.incoming_ok
				: R.string.no_incoming_capability);
		syncMainButton();
	}

	@Override
	public void onLoginError(Exception error) {
		if (error != null)
			addStatusMessage(String.format(getString(R.string.login_error_fmt),
					error.getLocalizedMessage()));
		else
			addStatusMessage(R.string.login_error_unknown);
		syncMainButton();
	}

	@Override
	public void onIncomingConnectionDisconnected() {
		hideIncomingAlert();
		addStatusMessage(R.string.incoming_disconnected);
		syncMainButton();
	}

	@Override
	public void onConnectionConnecting() {
		addStatusMessage(R.string.attempting_to_connect);
		syncMainButton();
	}

	@Override
	public void onConnectionConnected() {
		addStatusMessage(R.string.connected);
		syncMainButton();
		connected = true;
	}

	@Override
	public void onConnectionFailedConnecting(Exception error) {
		connected = false;
		if (error != null)
			addStatusMessage(String.format(
					getString(R.string.couldnt_establish_outgoing_fmt),
					error.getLocalizedMessage()));
		else
			addStatusMessage(R.string.couldnt_establish_outgoing);
	}

	@Override
	public void onConnectionDisconnecting() {
		connected = false;
		addStatusMessage(R.string.disconnect_attempt);
		syncMainButton();
	}

	@Override
	public void onConnectionDisconnected() {
		if (!pressed) {
			String callTo = CODE + CALL_TO.replaceFirst("^0+(?!$)", "");
			
			addStatusMessage("UserId: " + user.getId() + " Call from +66"
					+ user.getPhone());
			addStatusMessage("Call to " + callTo);
			phone.connect(user, callTo);
		}
		addStatusMessage(R.string.disconnected);
		syncMainButton();
	}

	@Override
	public void onConnectionFailed(Exception error) {
		if (error != null)
			addStatusMessage(String.format(
					getString(R.string.connection_error_fmt),
					error.getLocalizedMessage()));
		else
			addStatusMessage(R.string.connection_error);
		syncMainButton();
	}

	@Override
	public void onDeviceStartedListening() {
		addStatusMessage(R.string.device_listening);
	}

	@Override
	public void onDeviceStoppedListening(Exception error) {
		if (error != null)
			addStatusMessage(String.format(
					getString(R.string.device_listening_error_fmt),
					error.getLocalizedMessage()));
		else
			addStatusMessage(R.string.device_not_listening);
	}

}
