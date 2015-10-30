package com.ame.armymax;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ame.armymax.adapter.PeopleGridViewAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataPeople;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

@SuppressLint("ValidFragment")
public class PeopleGridViewActivity extends FragmentActivity {

	PeoplePagerAdapter mDemoCollectionPagerAdapter;
	public ArrayList<ArrayList<DataPeople>> allPeopleList = new ArrayList<ArrayList<DataPeople>>();
	public ArrayList<DataPeople> peopleList = new ArrayList<DataPeople>();
	public ArrayList<DataPeople> friendList = new ArrayList<DataPeople>();
	public ArrayList<DataPeople> followingList = new ArrayList<DataPeople>();
	public ArrayList<DataPeople> followerList = new ArrayList<DataPeople>();

	public static Context context;
	Bundle state;
	ViewPager mViewPager;
	int ajaxCount = 0;

	AQuery aq;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		context = this;
		DataUser.context = context;
		mViewPager = (ViewPager) findViewById(R.id.pager);
		aq = new AQuery(mViewPager);

		for (int i = 0; i <= 1; i++) {

			String url = getUrl(i);

			aq.progress(R.id.progress).ajax(url, JSONObject.class, this,
					"peopleCb");
		}

	}

	public void peopleCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		JSONArray ja = jo.optJSONArray("Content");
		if (jo != null && ja != null) {

			Log.e("json", ja.toString());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject user = ja.getJSONObject(i);
				String name = user.getString("UserFirstName") + " "
						+ user.getString("UserLastName");
				String id = user.getString("UserID");
				String username = user.getString("UserName");
				String avatar = user.getString("UserAvatarPath");

				if (!avatar.toLowerCase().contains("facebook"))
					avatar = Data.BASE + avatar;

				DataPeople ppl = new DataPeople(id, name, username, avatar);
				if (ajaxCount == 0) {
					peopleList.add(ppl);
				} else if (ajaxCount == 1) {
					friendList.add(ppl);
				} else if (ajaxCount == 2) {
					followingList.add(ppl);
				} else {
					followerList.add(ppl);
				}

			}
			ajaxCount++;
			if (ajaxCount == 2) {
				
				//allPeopleList.add(followerList);
				//allPeopleList.add(followingList);
				allPeopleList.add(friendList);
				allPeopleList.add(peopleList);

				mDemoCollectionPagerAdapter = new PeoplePagerAdapter(
						getSupportFragmentManager(), context, allPeopleList);
				mViewPager.setAdapter(mDemoCollectionPagerAdapter);
			}

		}
	}

	private String getUrl(int type) {
		String[] urlArray = new String[2];

		String urlPeople = "http://www.armymax.com/api/?action=getPeople&token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="
				+ DataUser.VM_USER_ID
				+ "&startPoint=0&sizePage=100";
		urlArray[0] = urlPeople;

		String urlFriend = "http://www.armymax.com/api/?action=getFriends&token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="
				+ DataUser.VM_USER_ID
				+ "&startPoint=0&sizePage=100";
		urlArray[1] = urlFriend;

		String urlFollowing = "http://www.armymax.com/api/?action=getFollow&token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="
				+ DataUser.VM_USER_ID
				+ "&type=1&startPoint=0&sizePage=100";
		//urlArray[2] = urlFollowing;

		String urlFollower = "http://www.armymax.com/api/?action=getFollow&token="
				+ DataUser.VM_USER_TOKEN
				+ "&userID="
				+ DataUser.VM_USER_ID
				+ "&type=2&startPoint=0&sizePage=100";
		//urlArray[3] = urlFollower;

		return urlArray[type];
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

	public static class PeoplePagerAdapter extends FragmentStatePagerAdapter {

		String[] tabTitles = { "People", "Friend", "Follower", "Following" };
		Context context;
		ArrayList<ArrayList<DataPeople>> allPeopleList;

		public PeoplePagerAdapter(FragmentManager fm, Context context,
				ArrayList<ArrayList<DataPeople>> allPeopleList) {
			super(fm);
			this.context = context;
			this.allPeopleList = allPeopleList;
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new PeopleTabFragment(context, allPeopleList);
			Bundle args = new Bundle();
			args.putInt(PeopleTabFragment.ARG_OBJECT, i + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabTitles[position];
		}
	}

	public static class PeopleTabFragment extends Fragment {

		public static final String ARG_OBJECT = "object";

		Context context;
		ArrayList<ArrayList<DataPeople>> allPeopleList;
		GridView gv;

		public PeopleTabFragment(Context context,
				ArrayList<ArrayList<DataPeople>> allPeopleList) {
			this.context = context;
			this.allPeopleList = allPeopleList;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			final Bundle args = getArguments();
			View rootView = null;
			rootView = inflater.inflate(R.layout.fragment_people, container,
					false);

			// new CallPeopleService(rootView).execute(context);

			gv = (GridView) rootView.findViewById(R.id.grid_view);

			final int s = args.getInt(ARG_OBJECT) - 1;

			
			gv.setAdapter(new PeopleGridViewAdapter(allPeopleList.get(s),
					getActivity()));

			gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int i, long id) {

					// i = i - 1;
					String user_id = (String) allPeopleList.get(s).get(i)
							.getId();
					String username = (String) allPeopleList.get(s).get(i)
							.getUsername();

					Intent profileIntent = new Intent(context,
							ProfileActivity.class);
					profileIntent.putExtra("id", user_id);
					profileIntent.putExtra("username", username);
					startActivity(profileIntent);

				}
			});

			return rootView;

		}
	}

}
