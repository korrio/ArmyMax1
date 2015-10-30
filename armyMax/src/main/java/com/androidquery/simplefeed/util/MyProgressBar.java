package com.androidquery.simplefeed.util;

import android.content.Context;
import android.widget.ProgressBar;

public class MyProgressBar extends ProgressBar {

	private OnProgressListener listener;
	private int mMax = 100;

	public MyProgressBar(Context context) {
		super(context);
	}

	@Override
	public synchronized void setMax(int max) {
		mMax = max;
		super.setMax(max);
	}

	@Override
	public synchronized void setProgress(final int progress) {

		if (listener != null) {
			post(new Runnable() {

				@Override
				public void run() {
					listener.onProgress(mMax, progress);

				}
			});

		}
		super.setProgress(progress);
	}

	public void setOnProgressListener(OnProgressListener listener) {
		this.listener = listener;
	}

	public interface OnProgressListener {

		public void onProgress(int max, int progress);

	}
}
