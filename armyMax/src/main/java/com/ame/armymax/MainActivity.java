package com.ame.armymax;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.adapter.ExampleAdapter;
import com.ame.armymax.adapter.NavDrawerListAdapter;
import com.ame.armymax.fragment.EverythingFragment;
import com.ame.armymax.fragment.LiveDialogFragment;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataSearch;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.model.NavDrawerItem;
import com.ame.armymax.pref.UserHelper;
import com.ame.armymax.xwalk.XWalkPhoneActivity;
import com.androidbegin.armymax.chat.AChatActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.simplefeed.util.CircleTransform;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.parse.PushService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.janmuller.android.simplecropimage.CropImage;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	public Context context;

	AQuery aq;
	ViewGroup mTop;
	ViewGroup mBottom;
	ImageView tb;
	TextView name;

	SearchView search;

	boolean doubleBackToExitPressedOnce = false;
	BootstrapEditText searchBox;
	TextView editProfile;
	TextView headerName;
	ImageView avatarSidebar;

	// LinearLayout buttonContainer;

	int common_n = 0;
	int chat_n = 0;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.setTheme(R.style.MyTheme);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

        //initCallPref(this);

		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.actionbar_layout);

		context = this;
		DataUser.context = context;
		mTitle = mDrawerTitle = getTitle();

		aq = new AQuery(this);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		mTop = (ViewGroup) mInflater.inflate(R.layout.drawer_list_header,
				mDrawerList, false);
		/*
		 * mTop.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { selectImage(); } });
		 */

		mBottom = (ViewGroup) mInflater.inflate(R.layout.drawer_list_footer,
				mDrawerList, false);

		avatarSidebar = (ImageView) mTop.findViewById(R.id.tbSidebar);
		avatarSidebar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});

		editProfile = (TextView) mTop.findViewById(R.id.edit_profile);
		editProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						EditProfileActivity.class);
				startActivity(i);
			}
		});

		headerName = (TextView) mTop.findViewById(R.id.header_name);
		headerName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this,
						EditProfileActivity.class);
				startActivity(i);
			}
		});

		searchBox = (BootstrapEditText) mTop.findViewById(R.id.room_name);
		searchBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchIntent = new Intent(context, SearchActivity.class);
				startActivity(searchIntent);
			}
		});
		searchBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					Intent searchIntent = new Intent(context,
							SearchActivity.class);
					searchIntent.putExtra("query", searchBox.getEditableText()
							.toString().trim());
					startActivity(searchIntent);
					return true;
				}
				return false;
			}
		});

		getNotiBadge();
		getChatBadge();

		String profileUrl = "http://www.armymax.com/api/?action=getUserInfo&token="
				+ DataUser.VM_USER_TOKEN + "&userName=" + DataUser.VM_USER_NAME;

		// debug!!!

		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Loading...");
		dialog.setMessage("กำลังโหลดโปรไฟล์");
		aq.progress(dialog).ajax(profileUrl, JSONObject.class, this,
				"profileInfoCb");

		mDrawerList.addHeaderView(mTop);

		mDrawerList.setSelectionAfterHeaderView();

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		final int actionBarColor = getResources().getColor(R.color.action_bar);
		getActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Menu");
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item

			displayView(4);
		}

		// mDrawerList.addFooterView(mBottom);
	}

	public void getNotiBadge() {
		String userId = DataUser.VM_USER_ID;
		String surl = "http://armymax.com/api/noti/noti.php?a=notiBadge&user_id="
				+ userId;

		Log.e("notiurl", surl);

		aq.ajax(surl, JSONObject.class, this, "notiBadgeCb");
	}

	public void notiBadgeCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

		common_n = jo.optInt("count");

	}

	public void getChatBadge() {
		String userId = DataUser.VM_USER_ID;
		String surl = "http://armymax.com/api/noti/noti.php?a=chatBadge&user_id="
				+ userId;

		Log.e("notiurl", surl);

		aq.ajax(surl, JSONObject.class, this, "chatBadgeCb");
	}

	public void chatBadgeCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

		chat_n = jo.optInt("count");

	}

	public void profileInfoCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			if (jo.optInt("status") != 3104) {
				Log.e("myurl", jo.toString());
				JSONObject jo2 = (JSONObject) jo.opt("data");

				String MC_user = jo2.getString("UserAvatarPathMedium");
				String MC_pass = jo2.getString("UserAvatarPathSmall");

				DataUser.VM_USER_FLING_COUNT = MC_user;
				DataUser.VM_USER_FLER_COUNT = MC_pass;

				common_n = jo2.optInt("noti_badge");
				chat_n = jo2.optInt("chat_badge");

				DataUser.VM_CHAT_N = chat_n;
				DataUser.VM_NOTI_N = common_n;

				String avatar = jo2.getString("UserAvatarPath");

				if (!avatar.contains("facebook"))
					DataUser.VM_USER_AVATAR = Data.BASE + avatar;
				else
					DataUser.VM_USER_AVATAR = avatar;

				Log.e("avatar", DataUser.VM_USER_AVATAR);

				tb = (ImageView) mTop.findViewById(R.id.tbSidebar);
				name = (TextView) mTop.findViewById(R.id.header_name);

				name.setText(DataUser.VM_NAME);

				Picasso.with(context)
						.load(DataUser.VM_USER_AVATAR)
						.placeholder(R.drawable.avatar_placeholder)
						.error(R.drawable.avatar_placeholder)
						.resizeDimen(R.dimen.grid_image_size,
								R.dimen.grid_image_size)
						.transform(new CircleTransform()).into(tb);
			} else {
				Toast.makeText(context, jo.optString("msg"), Toast.LENGTH_LONG)
						.show();
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(i);
			}

			// adding nav drawer items to array
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
					.getResourceId(0, -1)));
			// Everything
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
					.getResourceId(1, -1)));

			// Live
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
					.getResourceId(2, -1)));
			// Everything
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
					.getResourceId(3, -1)));
			// People
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
					.getResourceId(4, -1)));
			// Photo
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
					.getResourceId(5, -1)));
			// Video
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
					.getResourceId(6, -1)));

			// int common_n = jo.optInt("badge_common");
			// int chat_n = jo.optInt("badge_chat");
			// setting the nav drawer list adapter
			NavDrawerItem notiMenu;
			if (DataUser.VM_NOTI_N == 0) {
				notiMenu = new NavDrawerItem(navMenuTitles[7],
						navMenuIcons.getResourceId(7, -1));
			} else {
				notiMenu = new NavDrawerItem(navMenuTitles[7],
						navMenuIcons.getResourceId(7, -1), true, ""
								+ DataUser.VM_NOTI_N);
			}
			// Noti
			navDrawerItems.add(notiMenu);

			NavDrawerItem chatMenu;
			if (DataUser.VM_CHAT_N == 0) {
				chatMenu = new NavDrawerItem(navMenuTitles[8],
						navMenuIcons.getResourceId(8, -1));
			} else {
				chatMenu = new NavDrawerItem(navMenuTitles[8],
						navMenuIcons.getResourceId(8, -1), true, ""
								+ DataUser.VM_CHAT_N);
			}
			// Chat
			navDrawerItems.add(chatMenu);

			// Setting
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons
					.getResourceId(9, -1)));

			// Logout
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[10],
					navMenuIcons.getResourceId(10, -1)));

			// Recycle the typed array
			navMenuIcons.recycle();
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
			mDrawerList.setAdapter(adapter);
			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		}

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// buttonContainer.setVisibility(View.GONE);
			displayView(position);
		}

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			/*
			 * SearchManager searchManager = (SearchManager)
			 * getSystemService(Context.SEARCH_SERVICE); search = (SearchView)
			 * menu.findItem(R.id.action_search) .getActionView();
			 * search.setSearchableInfo(searchManager
			 * .getSearchableInfo(getComponentName()));
			 * search.setOnQueryTextListener(new OnQueryTextListener() {
			 * 
			 * @Override public boolean onQueryTextSubmit(String arg0) { // TODO
			 * Auto-generated method stub return false; }
			 * 
			 * @Override public boolean onQueryTextChange(String query) {
			 * loadData(query, menu); return false; } });
			 */
		}

		return true;
	}

	private List<DataSearch> items;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadData(String query, Menu menu) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			// Load data from list to cursor

			String searchUrl = "";
			aq.ajax(searchUrl, JSONObject.class, this, "searchCb");

		}

	}

	public void searchCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			String[] columns = new String[] { "name", "userName" };

			MatrixCursor cursor = new MatrixCursor(columns);

			DataSearch p = new DataSearch("", "aaa", "bbb", "ccc");
			items.add(p);

			DataSearch p2 = new DataSearch("", "aa", "bb", "cc");
			items.add(p2);

			DataSearch p3 = new DataSearch("", "a", "b", "c");
			items.add(p3);

			search.setSuggestionsAdapter(new ExampleAdapter(this, cursor, items));
		}

	}

	String LIVE_TITLE = "";
	String LIVE_DESC = "";

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed in the action
			// bar.
			// Create a simple intent that starts the hierarchical parent
			// activity and
			// use NavUtils in the Support Package to ensure proper handling of
			// Up.
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so
				// create a new task
				// with a synthesized back stack.
				TaskStackBuilder.from(this)
				// If there are ancestor activities, they should be added here.
						.addNextIntent(upIntent).startActivities();
				finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;

			/*
			 * case R.id.action_post: Intent post = new Intent(this,
			 * PostFragmentActivity.class); startActivity(post); //
			 * Toast.makeText(context, "Post!", Toast.LENGTH_SHORT).show();
			 * return true;
			 */
		case R.id.action_live:

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            String title = "ถ่ายทอดสดโดย " + DataUser.VM_NAME;
            String desc = "ถ่ายทอดสดจากมือถือ";

            postLive(title, desc);

            LiveDialogFragment dialog
                    = LiveDialogFragment.newInstance(
                    title,desc);

            dialog.show(getFragmentManager(), "blur_sample");

/*
			//Dialog dialog = new Dialog(context, R.style.Dialog);

			dialog.setContentView(R.layout.activity_live);

			dialog.setTitle("ถ่ายทอดสด");

			dialog.getWindow().setBackgroundDrawableResource(
					R.drawable.bg_gray_texture);


			BootstrapButton button_live = (BootstrapButton) dialog
					.findViewById(R.id.button_live);

			button_live.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					postLive(titleText.getEditableText().toString(), descText
							.getEditableText().toString());

				}
			});

			dialog.show();
			*/


			/*
			 * Intent live = new Intent(this, LivePostActivity.class);
			 * startActivity(live);
			 */
			// Toast.makeText(context, "Live!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_logout:
			DataUser.clearAll();
			Intent loginIntent = new Intent(context, LoginActivity.class);
			startActivity(loginIntent);
			finish();
			return true;
		case R.id.action_setting:
			Intent setting = new Intent(this, SettingsActivity.class);
			startActivity(setting);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void postLive(String title, String desc) {
		String url = "http://www.armymax.com/api/?action=postLive";

		Map<String, Object> params = new HashMap<String, Object>();

		String myTitle = title.toString().replace("\n", "%0A");
		String myDesc = desc.toString().replace("\n", "%0A");

		LIVE_TITLE = myTitle;
		LIVE_DESC = myDesc;

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



		} else {
			Toast.makeText(context, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void liveCb(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			Toast.makeText(context, json.optString("msg"), Toast.LENGTH_LONG)
					.show();
			if (json.optInt("status") == 5101) {

                //getFragmentManager().beginTransaction().add(R.id.container, new LiveDialogFragment()).commit();

                LiveDialogFragment fragment
                        = LiveDialogFragment.newInstance(
                        LIVE_TITLE,LIVE_DESC
                );
                fragment.show(getFragmentManager(), "blur_sample");

                /*
				Intent startLive = new Intent(MainActivity.this,
						LiveNowActivity.class);
				startLive.putExtra("title", LIVE_TITLE);
				startLive.putExtra("desc", LIVE_DESC);
				finish();
				startActivity(startLive);
				*/

				Toast.makeText(context, "You are ready to Live",
						Toast.LENGTH_LONG).show();
			} else {

			}
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (NoSuchMethodException e) {
					Log.e("heyhey", "onMenuOpened", e);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			File f = new File(Environment.getExternalStorageDirectory()
					.toString());
			for (File temp : f.listFiles()) {
				if (temp.getName().equals("temp.jpg")) {
					f = temp;
					break;
				}
			}

			if (requestCode == REQUEST_TAKE_PHOTO) {

				try {
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					tempFile = f;
					bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					String path = tempFile.getAbsolutePath();
					// setAvatar(path);
					startCropImage(path);

					f.delete();
					OutputStream fOut = null;
					File file = new File(path);
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_CHOOSE_PHOTO) {

				Uri selectedImageUri = data.getData();

				// this one
				String path = getRealPathFromURI(context, selectedImageUri);

				tempFile = new File(path);
				// setAvatar(path);
				startCropImage(path);

			} else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {
					return;
				}
				uploadAvatar();
				// setAvatar(path);

			}
		}
	}

	public int getCameraPhotoOrientation(String imagePath) {
		int rotate = 0;
		try {
			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public void uploadAvatar() {
		String url = "http://www.armymax.com/api/?action=updateProfileAvatar";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", DataUser.VM_USER_TOKEN);
		params.put("userID", DataUser.VM_USER_ID);
		params.put("pos", "0::0::0::0::0::0");
		params.put("photo", tempFile);
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Uploading avatar ");
		dialog.setMessage(DataUser.VM_USER_FB_FNAME + " "
				+ DataUser.VM_USER_FB_LNAME);
		aq.progress(dialog).ajax(url, params, JSONObject.class, context,
				"uploadAvatarCb");

	}

	public void uploadAvatarCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			if (jo.optInt("status") == 4101) {

				Toast.makeText(context, "Updated avatar", Toast.LENGTH_LONG)
						.show();
				String path = tempFile.getAbsolutePath();
				setAvatar(path);
			}
		}
	}

	private void setAvatar(String path) {
		Bitmap b = BitmapFactory.decodeFile(path);
		Picasso.with(context).load(new File(path))
				.resizeDimen(R.dimen.grid_image_size, R.dimen.grid_image_size)
				.transform(new CircleTransform()).into(tb);
		// tb.setImageBitmap(b);
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	File tempFile;
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int REQUEST_CHOOSE_PHOTO = 2;
	private static final int REQUEST_CODE_CROP_IMAGE = 3;

	private static final int PHOTO_SIZE_WIDTH = 100;
	private static final int PHOTO_SIZE_HEIGHT = 100;

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Change avatar!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					// intent.putExtra("crop", "true");
					startActivityForResult(intent, REQUEST_TAKE_PHOTO);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setDataAndType(
							MediaStore.Images.Media.INTERNAL_CONTENT_URI,
							"image/*");
					intent.setType("image/*");
					// intent.putExtra("crop", "true");
					intent.putExtra("scale", true);
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", PHOTO_SIZE_WIDTH);
					intent.putExtra("outputY", PHOTO_SIZE_HEIGHT);
					startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void startCropImage(String path) {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, path);
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_setting).setVisible(!drawerOpen);
		menu.findItem(R.id.action_setting).setVisible(false);
		menu.findItem(R.id.action_logout).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		FragmentActivity fa = null;
		switch (position) {
		case 3:
			fragment = new EverythingFragment(context, "live");
			break;
		case 4:
			fragment = new EverythingFragment(context, "all");
			break;
		case 6:
			fragment = new EverythingFragment(context, "photo");
			break;
		case 7:
			fragment = new EverythingFragment(context, "video");
			break;
		

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

		} else {
			switch (position) {
			case 0:
				Intent profileIntent = new Intent(this, ProfileActivity.class);
				profileIntent.putExtra("id", DataUser.VM_USER_ID);
				profileIntent.putExtra("name", DataUser.VM_NAME);
				profileIntent.putExtra("username", DataUser.VM_USER_NAME);
				profileIntent.putExtra("url", DataUser.VM_USER_AVATAR);
				startActivity(profileIntent);
				// finish();
				break;
			case 1:
				Intent phoneCall = new Intent(this,
						XWalkPhoneActivity.class);
                long unixTime = System.currentTimeMillis() / 1000L;
				phoneCall.putExtra("url", "http://maxcalling.com/assets/armymax.php?timestamp=" + unixTime);
				startActivity(phoneCall);
				/*
				Intent i1 = new Intent(this, DialpadActivity.class);
				//Intent i1 = new Intent(this, ChromeWebView.class);
				// i1.putExtra("user", DataUser.VM_USER_FLING_COUNT);
				// i1.putExtra("pass", DataUser.VM_USER_FLER_COUNT);
				startActivity(i1);
				*/
				break;
			case 2:
				Intent i2 = new Intent(this, ConferenceActivity.class);
				startActivity(i2);


				break;
			case 5:
				Intent i5 = new Intent(this, PeopleGridViewActivity.class);
				startActivity(i5);
				// finish();
				break;
			case 8:
				Intent i8 = new Intent(this, NotiHistoryActivity.class);
				startActivity(i8);
				break;
			case 9:
				Intent i9 = new Intent(this, AChatActivity.class);
				startActivity(i9);
				break;
			case 10:
				Intent setting = new Intent(this, SettingsActivity.class);
				startActivity(setting);
				break;
			case 11:
				DataUser.clearAll();

				PushService.unsubscribe(this, "channel1");

				UserHelper user = new UserHelper(this);
				user.deleteSession();

				// handle.unauth();

				Intent loginIntent = new Intent(this, LoginActivity.class);
				loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				loginIntent.putExtra("logout", true);
				startActivity(loginIntent);
				finish();

			default:
				break;
			}

		}
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		if (position - 1 < 0)
			setTitle("Everything");
		else
			setTitle(navMenuTitles[position - 1]);

		mDrawerLayout.closeDrawer(mDrawerList);
		// buttonContainer.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
	    if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "แตะ BACK อีกครั้งเพื่อออกจาก ArmyMax", Toast.LENGTH_SHORT).show();

	    new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
	}

//    public static void connectToRoom(Activity activity,String roomName, boolean videoCall) {
//        // Get room name (random for loopback).
//        String roomId = roomName;
//        int runTimeMs = 0;
//
//        initCallPref(activity);
//
//
//        String roomUrl = "http://150.107.31.11:3000";
//
//        // Video call enabled flag.
//        boolean videoCallEnabled = videoCall;
//
//        // Get default codecs.
//        String videoCodec = sharedPref.getString(keyprefVideoCodec,
//                activity.getString(R.string.pref_videocodec_default));
//        String audioCodec = sharedPref.getString(keyprefAudioCodec,
//                activity.getString(R.string.pref_audiocodec_default));
//
//        // Check HW codec flag.
//        boolean hwCodec = sharedPref.getBoolean(keyprefHwCodecAcceleration,
//                Boolean.valueOf(activity.getString(R.string.pref_hwcodec_default)));
//
//        // Check Disable Audio Processing flag.
//        boolean noAudioProcessing = sharedPref.getBoolean(
//                keyprefNoAudioProcessingPipeline,
//                Boolean.valueOf(activity.getString(R.string.pref_noaudioprocessing_default)));
//
//
//
//        // Get video resolution from settings.
//        int videoWidth = 0;
//        int videoHeight = 0;
//        String resolution = sharedPref.getString(keyprefResolution,
//                activity.getString(R.string.pref_resolution_default));
//        String[] dimensions = resolution.split("[ x]+");
//        if (dimensions.length == 2) {
//            try {
//                videoWidth = Integer.parseInt(dimensions[0]);
//                videoHeight = Integer.parseInt(dimensions[1]);
//            } catch (NumberFormatException e) {
//                videoWidth = 0;
//                videoHeight = 0;
//                Log.e("HEYHEYHEY", "Wrong video resolution setting: " + resolution);
//            }
//        }
//
//        // Get camera fps from settings.
//        int cameraFps = 0;
//        String fps = sharedPref.getString(keyprefFps,
//                activity.getString(R.string.pref_fps_default));
//        String[] fpsValues = fps.split("[ x]+");
//        if (fpsValues.length == 2) {
//            try {
//                cameraFps = Integer.parseInt(fpsValues[0]);
//            } catch (NumberFormatException e) {
//                Log.e("HEYHEYHEY", "Wrong camera fps setting: " + fps);
//            }
//        }
//
//        // Get video and audio start bitrate.
//        int videoStartBitrate = 0;
//        String bitrateTypeDefault = activity.getString(
//                R.string.pref_startvideobitrate_default);
//        String bitrateType = sharedPref.getString(
//                keyprefVideoBitrateType, bitrateTypeDefault);
//        if (!bitrateType.equals(bitrateTypeDefault)) {
//            String bitrateValue = sharedPref.getString(keyprefVideoBitrateValue,
//                    activity.getString(R.string.pref_startvideobitratevalue_default));
//            videoStartBitrate = Integer.parseInt(bitrateValue);
//        }
//        int audioStartBitrate = 0;
//        bitrateTypeDefault = activity.getString(R.string.pref_startaudiobitrate_default);
//        bitrateType = sharedPref.getString(
//                keyprefAudioBitrateType, bitrateTypeDefault);
//        if (!bitrateType.equals(bitrateTypeDefault)) {
//            String bitrateValue = sharedPref.getString(keyprefAudioBitrateValue,
//                    activity.getString(R.string.pref_startaudiobitratevalue_default));
//            audioStartBitrate = Integer.parseInt(bitrateValue);
//        }
//
//        // Test if CpuOveruseDetection should be disabled. By default is on.
//        boolean cpuOveruseDetection = sharedPref.getBoolean(
//                keyprefCpuUsageDetection,
//                Boolean.valueOf(
//                        activity.getString(R.string.pref_cpu_usage_detection_default)));
//
//        // Check statistics display option.
//        boolean displayHud = sharedPref.getBoolean(keyprefDisplayHud,
//                Boolean.valueOf(activity.getString(R.string.pref_displayhud_default)));
//
//        // Start AppRTCDemo activity.
//
//
//
//        Log.d("HEYHEYHEY", "Connecting to room " + roomId + " at URL " + roomUrl);
//        if (validateUrl(roomUrl)) {
//            Uri uri = Uri.parse(roomUrl);
//            Intent intent = new Intent(activity, CallActivity.class);
//            intent.setData(uri);
//            intent.putExtra(CallActivity.EXTRA_ROOMID, roomId);
//            intent.putExtra(CallActivity.EXTRA_LOOPBACK, loopback);
//            intent.putExtra(CallActivity.EXTRA_VIDEO_CALL, videoCallEnabled);
//            intent.putExtra(CallActivity.EXTRA_VIDEO_WIDTH, videoWidth);
//            intent.putExtra(CallActivity.EXTRA_VIDEO_HEIGHT, videoHeight);
//            intent.putExtra(CallActivity.EXTRA_VIDEO_FPS, cameraFps);
//            intent.putExtra(CallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate);
//            intent.putExtra(CallActivity.EXTRA_VIDEOCODEC, videoCodec);
//            intent.putExtra(CallActivity.EXTRA_HWCODEC_ENABLED, hwCodec);
//            intent.putExtra(CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED,
//                    noAudioProcessing);
//            intent.putExtra(CallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate);
//            intent.putExtra(CallActivity.EXTRA_AUDIOCODEC, audioCodec);
//            intent.putExtra(CallActivity.EXTRA_CPUOVERUSE_DETECTION,
//                    cpuOveruseDetection);
//            intent.putExtra(CallActivity.EXTRA_DISPLAY_HUD, displayHud);
//            intent.putExtra(CallActivity.EXTRA_CMDLINE, commandLineRun);
//            intent.putExtra(CallActivity.EXTRA_RUNTIME, runTimeMs);
//            intent.putExtra(CallActivity.EXTRA_AVATAR_PATH, defaultMaleAvatar);
//
//            activity.startActivityForResult(intent, CONNECTION_REQUEST);
//        }
//    }
//
//    public static String defaultMaleAvatar = "https://www.vdomax.com/themes/vdomax1.1/images/default-male-avatar.png";
//
//    private static final int CONNECTION_REQUEST = 1111;
//
//    private static boolean commandLineRun = false;
//    private static boolean loopback = false;
//
//    public static boolean validateUrl(String url) {
//        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
//            return true;
//        }
//        return false;
//    }
//
//    public static void initCallPref(Activity activity) {
//        // Get setting keys.
//        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false);
//        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
//        keyprefVideoCallEnabled = activity.getString(R.string.pref_videocall_key);
//        keyprefResolution = activity.getString(R.string.pref_resolution_key);
//        keyprefFps = activity.getString(R.string.pref_fps_key);
//        keyprefVideoBitrateType = activity.getString(R.string.pref_startvideobitrate_key);
//        keyprefVideoBitrateValue = activity.getString(R.string.pref_startvideobitratevalue_key);
//        keyprefVideoCodec = activity.getString(R.string.pref_videocodec_key);
//        keyprefHwCodecAcceleration = activity.getString(R.string.pref_hwcodec_key);
//        keyprefAudioBitrateType = activity.getString(R.string.pref_startaudiobitrate_key);
//        keyprefAudioBitrateValue = activity.getString(R.string.pref_startaudiobitratevalue_key);
//        keyprefAudioCodec = activity.getString(R.string.pref_audiocodec_key);
//        keyprefNoAudioProcessingPipeline = activity.getString(R.string.pref_noaudioprocessing_key);
//        keyprefCpuUsageDetection = activity.getString(R.string.pref_cpu_usage_detection_key);
//        keyprefDisplayHud = activity.getString(R.string.pref_displayhud_key);
//        keyprefRoomServerUrl = activity.getString(R.string.pref_room_server_url_key);
//        keyprefRoom = activity.getString(R.string.pref_room_key);
//        keyprefRoomList = activity.getString(R.string.pref_room_list_key);
//    }
//
//    private static SharedPreferences sharedPref;
//    private static String keyprefVideoCallEnabled;
//    private static String keyprefResolution;
//    private static String keyprefFps;
//    private static String keyprefVideoBitrateType;
//    private static String keyprefVideoBitrateValue;
//    private static String keyprefVideoCodec;
//    private static String keyprefAudioBitrateType;
//    private static String keyprefAudioBitrateValue;
//    private static String keyprefAudioCodec;
//    private static String keyprefHwCodecAcceleration;
//    private static String keyprefNoAudioProcessingPipeline;
//    private static String keyprefCpuUsageDetection;
//    private static String keyprefDisplayHud;
//    private static String keyprefRoomServerUrl;
//    private static String keyprefRoom;
//    private static String keyprefRoomList;

}
