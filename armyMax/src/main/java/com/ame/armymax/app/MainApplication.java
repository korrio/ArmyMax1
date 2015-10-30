/*******************************************************************************
 * Copyright 2012 AndroidQuery (tinyeeliu@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Additional Note:
 * 1. You cannot use AndroidQuery's Facebook app account in your own apps.
 * 2. You cannot republish the app as is with advertisements.
 ******************************************************************************/
package com.ame.armymax.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ame.armymax.push.ManagePush;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.simplefeed.util.ErrorReporter;
import com.androidquery.util.AQUtility;
import com.parse.Parse;
import com.parse.PushService;


public class MainApplication extends Application implements
		Thread.UncaughtExceptionHandler {

	public static final String TAG = MainApplication.class.getSimpleName();

	private static MainApplication mInstance;
	
	public static synchronized MainApplication getInstance() {
		return mInstance;
	}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
	
	@Override
	public void onCreate() {
		
		mInstance = this;
		
		String applicationID = "vpfrcHlEbk1ZcQeGptUkkikIlv4lP2mkTj5WqkcI";
		String clientKey = "8pMgx6KvKBZ09S0V0fUwAFryjAr5xAf4jfbPlRFS";

		Parse.initialize(this, applicationID, clientKey);
		PushService.setDefaultPushCallback(this,
				ManagePush.class);
		PushService
				.subscribe(this, "EN", ManagePush.class);
		
		AQUtility.setContext(this);
		AQUtility.setDebug(true);

		ErrorReporter.installReporter(AQUtility.getContext());

		AQUtility.setExceptionHandler(this);
		AQUtility.setCacheDir(null);
		
		AjaxCallback.setNetworkLimit(8);

		BitmapAjaxCallback.setIconCacheLimit(200);
		BitmapAjaxCallback.setCacheLimit(80);
		BitmapAjaxCallback.setPixelLimit(400 * 400);
		BitmapAjaxCallback.setMaxPixelLimit(2000000);

		super.onCreate();
	}
	
	public static void clearCache() {
		AQUtility.cleanCacheAsync(mInstance);
	}
	
	public static Context getContext() {
		return AQUtility.getContext();
	}
	
	public static String get(int id) {
		return getContext().getString(id);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
        BitmapAjaxCallback.clearCache();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ErrorReporter.report(ex, true);
	}

}
