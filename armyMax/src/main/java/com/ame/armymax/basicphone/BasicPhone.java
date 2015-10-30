/*
 *  Copyright (c) 2011 by Twilio, Inc., all rights reserved.
 *
 *  Use of this software is subject to the terms and conditions of 
 *  the Twilio Terms of Service located at http://www.twilio.com/legal/tos
 */

package com.ame.armymax.basicphone;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.Device.Capability;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

public class BasicPhone implements DeviceListener, ConnectionListener {

	private static String MC_TOKEN = "";
	private static String CAP_TOKEN = "";


	AQuery aq;
	private static final String TAG = "BasicPhone";
	
	public interface LoginListener {
		public void onLoginStarted();

		public void onLoginFinished();

		public void onLoginError(Exception error);
	}

	public interface BasicConnectionListener {
		public void onIncomingConnectionDisconnected();

		public void onConnectionConnecting();

		public void onConnectionConnected();

		public void onConnectionFailedConnecting(Exception error);

		public void onConnectionDisconnecting();

		public void onConnectionDisconnected();

		public void onConnectionFailed(Exception error);
	}

	public interface BasicDeviceListener {
		public void onDeviceStartedListening();

		public void onDeviceStoppedListening(Exception error);
	}

	private static BasicPhone instance;

	public static final BasicPhone getInstance(Context context) {

		if (instance == null)
			instance = new BasicPhone(context);
		return instance;
	}

	private Context context;
	private LoginListener loginListener;
	private BasicConnectionListener basicConnectionListener;
	private BasicDeviceListener basicDeviceListener;

	private static boolean twilioSdkInited;
	private static boolean twilioSdkInitInProgress;
	private boolean queuedConnect;

	private Device device;
	private Connection pendingIncomingConnection;
	private Connection connection;
	private boolean speakerEnabled;

	private BasicPhone(Context context) {
		this.context = context;
	}

	public void setListeners(LoginListener loginListener,
			BasicConnectionListener basicConnectionListener,
			BasicDeviceListener basicDeviceListener) {
		this.loginListener = loginListener;
		this.basicConnectionListener = basicConnectionListener;
		this.basicDeviceListener = basicDeviceListener;
	}

	private String getCapabilityToken() throws Exception {
		// This runs synchronously for simplicity! In a real application you'd
		// want to do network I/O on a separate thread to avoid ANRs.

		return CAP_TOKEN;
	}

	private boolean isCapabilityTokenValid() {
		if (device == null || device.getCapabilities() == null)
			return false;
		long expTime = (Long) device.getCapabilities().get(
				Capability.EXPIRATION);
		return expTime - System.currentTimeMillis() / 1000 > 0;
	}

	private void updateAudioRoute() {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setSpeakerphoneOn(speakerEnabled);
	}

	public void login() {
		login(MC_TOKEN,CAP_TOKEN);
	}
	
	public void login(String token,String capToken) {
		MC_TOKEN = token;
		CAP_TOKEN = capToken;

		if (loginListener != null)
			loginListener.onLoginStarted();

		if (!twilioSdkInited) {
			if (twilioSdkInitInProgress)
				return;

			twilioSdkInitInProgress = true;

			Twilio.initialize(context, new Twilio.InitListener() {
				@Override
				public void onInitialized() {
					twilioSdkInited = true;
					twilioSdkInitInProgress = false;
					reallyLogin();
				}

				@Override
				public void onError(Exception error) {
					twilioSdkInitInProgress = false;
					if (loginListener != null)
						loginListener.onLoginError(error);
				}
			});
		} else
			reallyLogin();

	}

	private void reallyLogin() {
		try {
			String capabilityToken = getCapabilityToken();
			try {
				capabilityToken = getCapabilityToken();
				Log.e("capToken",capabilityToken);
				if (device == null) {
					device = Twilio.createDevice(capabilityToken, this);
					Intent intent = new Intent(context,
							PhoneCallActivity.class);
					PendingIntent pendingIntent = PendingIntent
							.getActivity(context, 0, intent,
									PendingIntent.FLAG_UPDATE_CURRENT);
					device.setIncomingIntent(pendingIntent);
				} else
					device.updateCapabilityToken(capabilityToken);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (loginListener != null)
				loginListener.onLoginFinished();

			if (queuedConnect) {
				// If someone called connect() before we finished
				// initializing
				// the SDK, let's take care of that here.
				connect();
				queuedConnect = false;
			}
			

		} catch (Exception e) {
			if (device != null)
				device.release();
			device = null;

			if (loginListener != null) {
				Log.e("error", "errorLogin");
				loginListener.onLoginError(e);
			}
				
		}
	}

	

				

	public void setSpeakerEnabled(boolean speakerEnabled) {
		if (speakerEnabled != this.speakerEnabled) {
			this.speakerEnabled = speakerEnabled;
			updateAudioRoute();
		}
	}
	
	public void connect(User user,String callTo) {
		if (twilioSdkInitInProgress) {
			// If someone calls connect() before the SDK is initialized, we'll
			// remember
			// that fact and try to connect later.
			queuedConnect = true;
			return;
		}

		if (!isCapabilityTokenValid())
			login();

		if (device == null)
			return;

		if (canMakeOutgoing()) {
			disconnect();

			Map<String, String> params = new HashMap<String, String>();
			params.put("PhoneNumber",  callTo);
			params.put("Country", "TH");
			params.put("UserId", user.getId());
			params.put("CallerId", user.getPrefix() + user.getPhone());
			
			connection = device.connect(params, this);
			if (connection == null && basicConnectionListener != null)
				basicConnectionListener
						.onConnectionFailedConnecting(new Exception(
								"Couldn't create new connection"));
		}
	}

	public void connect() {
		if (twilioSdkInitInProgress) {
			// If someone calls connect() before the SDK is initialized, we'll
			// remember
			// that fact and try to connect later.
			queuedConnect = true;
			return;
		}

		if (!isCapabilityTokenValid())
			login();

		if (device == null)
			return;

		if (canMakeOutgoing()) {
			disconnect();
			String numberInput = "855533558";

			Map<String, String> params = new HashMap<String, String>();
			params.put("PhoneNumber", "+66" + numberInput);
			params.put("Country", "TH");
			params.put("UserId", "84");
			params.put("CallerId", "+66917366196");
			
			connection = device.connect(null, this);
			if (connection == null && basicConnectionListener != null)
				basicConnectionListener
						.onConnectionFailedConnecting(new Exception(
								"Couldn't create new connection"));
		}
	}

	public void disconnect() {
		if (connection != null) {
			connection.disconnect(); // will null out in onDisconnected()
			if (basicConnectionListener != null)
				basicConnectionListener.onConnectionDisconnecting();
		}
	}

	public void acceptConnection() {
		if (pendingIncomingConnection != null) {
			if (connection != null)
				disconnect();

			pendingIncomingConnection.accept();
			connection = pendingIncomingConnection;
			pendingIncomingConnection = null;
		}
	}

	public void ignoreIncomingConnection() {
		if (pendingIncomingConnection != null) {
			pendingIncomingConnection.ignore();
		}
	}

	public boolean isConnected() {
		return connection != null
				&& connection.getState() == Connection.State.CONNECTED;
	}

	public Connection.State getConnectionState() {
		return connection != null ? connection.getState()
				: Connection.State.DISCONNECTED;
	}

	public boolean hasPendingConnection() {
		return pendingIncomingConnection != null;
	}

	public boolean handleIncomingIntent(Intent intent) {
		Device inDevice = intent.getParcelableExtra(Device.EXTRA_DEVICE);
		Connection inConnection = intent
				.getParcelableExtra(Device.EXTRA_CONNECTION);
		if (inDevice == null && inConnection == null)
			return false;

		intent.removeExtra(Device.EXTRA_DEVICE);
		intent.removeExtra(Device.EXTRA_CONNECTION);

		if (pendingIncomingConnection != null) {
			Log.i(TAG, "A pending connection already exists");
			inConnection.ignore();
			return false;
		}

		pendingIncomingConnection = inConnection;
		pendingIncomingConnection.setConnectionListener(this);

		return true;
	}

	public boolean canMakeOutgoing() {
		if (device == null)
			return false;

		Map<Capability, Object> caps = device.getCapabilities();
		return caps.containsKey(Capability.OUTGOING)
				&& (Boolean) caps.get(Capability.OUTGOING);
	}

	public boolean canAcceptIncoming() {
		if (device == null)
			return false;

		Map<Capability, Object> caps = device.getCapabilities();
		return caps.containsKey(Capability.INCOMING)
				&& (Boolean) caps.get(Capability.INCOMING);
	}

	@Override
	/* DeviceListener */
	public void onStartListening(Device inDevice) {
		if (basicDeviceListener != null)
			basicDeviceListener.onDeviceStartedListening();
	}

	@Override
	/* DeviceListener */
	public void onStopListening(Device inDevice) {
		if (basicDeviceListener != null)
			basicDeviceListener.onDeviceStoppedListening(null);
	}

	@Override
	/* DeviceListener */
	public void onStopListening(Device inDevice, int inErrorCode,
			String inErrorMessage) {
		if (basicDeviceListener != null)
			basicDeviceListener.onDeviceStoppedListening(new Exception(
					inErrorMessage));
	}

	@Override
	/* DeviceListener */
	public boolean receivePresenceEvents(Device inDevice) {
		return false;
	}

	@Override
	/* DeviceListener */
	public void onPresenceChanged(Device inDevice, PresenceEvent inPresenceEvent) {
	}

	@Override
	/* ConnectionListener */
	public void onConnecting(Connection inConnection) {
		if (basicConnectionListener != null)
			basicConnectionListener.onConnectionConnecting();
	}

	@Override
	/* ConnectionListener */
	public void onConnected(Connection inConnection) {
		updateAudioRoute();
		if (basicConnectionListener != null)
			basicConnectionListener.onConnectionConnected();
	}

	@Override
	/* ConnectionListener */
	public void onDisconnected(Connection inConnection) {
		if (inConnection == connection) {
			connection = null;
			if (basicConnectionListener != null)
				basicConnectionListener.onConnectionDisconnected();
		} else if (inConnection == pendingIncomingConnection) {
			pendingIncomingConnection = null;
			if (basicConnectionListener != null)
				basicConnectionListener.onIncomingConnectionDisconnected();
		}
	}

	@Override
	/* ConnectionListener */
	public void onDisconnected(Connection inConnection, int inErrorCode,
			String inErrorMessage) {
		if (inConnection == connection) {
			connection = null;
			if (basicConnectionListener != null)
				basicConnectionListener
						.onConnectionFailedConnecting(new Exception(
								inErrorMessage));
		}
	}
}
