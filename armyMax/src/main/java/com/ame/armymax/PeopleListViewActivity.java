package com.ame.armymax;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ame.armymax.adapter.PeopleListViewAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataPeople;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkActivity;
import com.ame.armymax.xwalk.XWalkChatRoomActivity;
import com.androidbegin.armymax.chat.Link;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class PeopleListViewActivity extends FragmentActivity {

	PeoplePagerAdapter mDemoCollectionPagerAdapter;
	public ArrayList<ArrayList<DataPeople>> allPeopleList = new ArrayList<ArrayList<DataPeople>>();
	public ArrayList<DataPeople> peopleList = new ArrayList<DataPeople>();
	public ArrayList<DataPeople> friendList = new ArrayList<DataPeople>();

	public static Context context;
	Bundle state;
	ViewPager mViewPager;
	int ajaxCount = 0;

	AQuery aq;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pager_chat);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		context = this;
		DataUser.context = context;
		mViewPager = (ViewPager) findViewById(R.id.pager);
		aq = new AQuery(mViewPager);

		for (int i = 0; i <= 1; i++) {

			String url = getUrl(i);
			Log.e("aaa", url);
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
				String mobile = user.getString("UserAvatarPathMedium");

				String username = user.getString("UserName") + " (Tel:"
						+ mobile + ")";
				String avatar = user.getString("UserAvatarPath");

				if (!avatar.toLowerCase().contains("facebook"))
					avatar = Data.BASE + avatar;

				DataPeople ppl = new DataPeople(id, name, username, avatar);
				if (ajaxCount == 0) {
					peopleList.add(ppl);
				} else if (ajaxCount == 1) {
					friendList.add(ppl);
				}

			}
			ajaxCount++;
			if (ajaxCount == 2) {

				// allPeopleList.add(followerList);
				// allPeopleList.add(followingList);
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

	@Override
	public boolean onPrepareOptionsMenu(Menu aMenu) {

		return super.onPrepareOptionsMenu(aMenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public static class PeoplePagerAdapter extends FragmentStatePagerAdapter {

		String[] tabTitles = { "People", "Friend" };
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
		ListView lv;

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
			rootView = inflater.inflate(R.layout.fragment_feed_normal,
					container, false);

			// new CallPeopleService(rootView).execute(context);

			lv = (ListView) rootView.findViewById(R.id.feed_listview_normal);

			final int s = args.getInt(ARG_OBJECT) - 1;

			/*
			 * 
			 * if (args.getInt(ARG_OBJECT) == 1) {
			 * 
			 * lv.setAdapter(new
			 * PeopleGridViewAdapter(getActivity(),peopleList));
			 * 
			 * } else if (args.getInt(ARG_OBJECT) == 2) {
			 * 
			 * lv.setAdapter(new FriendGridViewAdapter(getActivity()));
			 * 
			 * } else if (args.getInt(ARG_OBJECT) == 3) { lv.setAdapter(new
			 * FollowingGridViewAdapter(getActivity()));
			 * 
			 * } else {
			 * 
			 * lv.setAdapter(new FollowerGridViewAdapter(getActivity()));
			 * 
			 * }
			 */
			lv.setAdapter(new PeopleListViewAdapter(allPeopleList.get(s),
					getActivity()));

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int i, long id) {

					// i = i - 1;
					final String friendId = (String) allPeopleList.get(s)
							.get(i).getId();
					final String username = (String) allPeopleList.get(s)
							.get(i).getUsername();

					final String friendName = (String) allPeopleList.get(s)
							.get(i).getName();
					String avatar = (String) allPeopleList.get(s).get(i)
							.getAvatar();

					Dialog dialog = new Dialog(getActivity(),R.style.Dialog);

					dialog.setContentView(R.layout.profile_dialog);

					dialog.setTitle(friendName);
					
					

					Button btn1 = (Button) dialog.findViewById(R.id.call_chat);
					btn1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent chatRoom = new Intent(getActivity(),
									XWalkActivity.class);
							chatRoom.putExtra("userId", friendId);
							chatRoom.putExtra("friendName", friendName);
							chatRoom.putExtra("url", Link.getLinkChat(friendId));
							chatRoom.putExtra("title", friendName);
							//Toast.makeText(context, "fid:" + friendId,
								//	Toast.LENGTH_SHORT).show();
							startActivity(chatRoom);
						}
					});

					Button btn2 = (Button) dialog.findViewById(R.id.call_audio);
					btn2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// String url =
							// "https://armymax.com/app/gotoaudiopost.php?session="+DataUser.VM_USER_NAME+"&userid="
							// + friendId;

							String url = "http://armymax.com/RTCMultiConnection/demos/phone/audio.html?session=asdf&userid=21";
							String app = "ArmyMax%20AudioCall";
							String m = DataUser.VM_USER_NAME
									+ "%20โทรด้วยเสียงหาคุณ";
							String notiUrl = "http://armymax.com/api/noti/?title="
									+ app
									+ "&m="
									+ m
									+ "&f="
									+ DataUser.VM_USER_ID
									+ "&n="
									+ DataUser.VM_USER_NAME
									+ "&t="
									+ friendId
									+ "&type=504";
							String status = "";
							notiUrl = notiUrl.replace(" ", "%20");

							Log.e("urlurl", notiUrl);

							Intent chatRoom = new Intent(getActivity(),
									XWalkChatRoomActivity.class);
							chatRoom.putExtra("userId", friendId);
							chatRoom.putExtra("friendName", friendName);
							chatRoom.putExtra("roomUrl", url);
							chatRoom.putExtra("notiUrl", notiUrl);
							//Toast.makeText(context, "url:" + url,
								//	Toast.LENGTH_SHORT).show();
							startActivity(chatRoom);

							/*
							 * 
							 * String url =
							 * "https://www.armymax.com/app/gotoaudiopost.php?session="
							 * +DataUser.VM_USER_NAME+"&userid=" + friendId;
							 * Intent browserIntent = new
							 * Intent(Intent.ACTION_VIEW, Uri.parse(url));
							 * Toast.makeText(context, "session:" +
							 * DataUser.VM_USER_NAME,
							 * Toast.LENGTH_SHORT).show();
							 * startActivity(browserIntent);
							 */
						}
					});

					Button btn3 = (Button) dialog.findViewById(R.id.call_video);
					btn3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							String url = "http://www.armymax.com/RTCMultiConnection/demos/AppRTC-Look.html";
							String app = "ArmyMax%20VideoCall";
							String m = DataUser.VM_USER_NAME
									+ "%20วิดิโอคอลหาคุณ";
							String notiUrl = "http://www.armymax.com/api/noti/?title="
									+ app
									+ "&m="
									+ m
									+ "&f="
									+ DataUser.VM_USER_ID
									+ "&n="
									+ DataUser.VM_USER_NAME
									+ "&t="
									+ friendId
									+ "&type=505";

							notiUrl = notiUrl.replace(" ", "%20");
							Log.e("urlurl", notiUrl);

							Intent chatRoom = new Intent(getActivity(),
									XWalkChatRoomActivity.class);
							chatRoom.putExtra("userId", friendId);
							chatRoom.putExtra("friendName", friendName);
							chatRoom.putExtra("roomUrl", url);
							chatRoom.putExtra("notiUrl", notiUrl);
							//Toast.makeText(context, "url:" + url,
								//	Toast.LENGTH_SHORT).show();
							startActivity(chatRoom);

						}
					});
					TextView titlemovie = (TextView) dialog
							.findViewById(R.id.text_title);
					titlemovie.setText(friendName);

					ImageView avatarView = (ImageView) dialog
							.findViewById(R.id.avatar);
					Picasso.with(context).load(avatar)
							.placeholder(R.drawable.avatar_placeholder)
							.error(R.drawable.avatar_placeholder)
							.into(avatarView);

					// dialog.dismiss();

					dialog.show();

				}
			});

			return rootView;

		}
	}

}
