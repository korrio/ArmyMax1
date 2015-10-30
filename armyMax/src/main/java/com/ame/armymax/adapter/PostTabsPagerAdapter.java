package com.ame.armymax.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PostTabsPagerAdapter extends FragmentPagerAdapter {

	public PostTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			//return new WritePostFragment();
		case 1:
			// Games fragment activity
			//return new SharePhotoFragment();
		case 2:
			// Movies fragment activity
			//return new UploadVideoFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
