package com.ame.armymax.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ame.armymax.LoginActivity;
import com.ame.armymax.PhotoDetailActivity;
import com.ame.armymax.R;
import com.ame.armymax.VideoDetailActivity;
import com.ame.armymax.adapter.EverythingAdapter;
import com.ame.armymax.app.MainApplication;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.post.PostPhotoActivity;
import com.ame.armymax.post.PostStatusAcitivity;
import com.ame.armymax.post.PostVideoActivity;
import com.ame.armymax.pref.UserHelper;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;
import com.yalantis.pulltorefresh.library.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EverythingFragment extends Fragment {

	AQuery aq;
	AQuery aq2;
	View rootView;

	int LOAD_JSON_STATUS = 0;
	int START_FROM = 0;
	int COUNT = 0;
	long expire = -1;

	Context context;
	String app;

	public ArrayList<DataFeedEverything> feedList = new ArrayList<DataFeedEverything>();
    public EverythingAdapter adapter;


    @InjectView(R.id.button_recent)
    public Button postStatus;

    @InjectView(R.id.button_photo)
    public Button postPhoto;

    @InjectView(R.id.button_video)
    public Button postVideo;



    @InjectView(R.id.feed_listview)
    public ListView listView;

    @InjectView(R.id.feed_listview)
    public ListView scrollView;

    @InjectView(R.id.footer_layout)
    public LinearLayout footer;

    @InjectView(R.id.pull_to_refresh)
    public PullToRefreshView mPullToRefreshView;

    public EverythingFragment() {

    }

    public EverythingFragment(Context context, String app) {
        this.context = context;
        this.app = app;
    }

    public static int REFRESH_DELAY = 2000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AQUtility.cleanCacheAsync(context);
		MainApplication.clearCache();

		rootView = inflater.inflate(R.layout.fragment_feed, container, false);
		feedList.clear();
        ButterKnife.inject(this,rootView);

		aq = new AQuery(rootView);
		aq2 = new AQuery(rootView);
		if(app == null)
			app = "all";

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initAjax(-1, 0, 50);
               /*
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);

                    }
                }, REFRESH_DELAY);
                */
            }
        });

        scrollView.setOnTouchListener(new OnTouchListener() {
			final int DISTANCE = 3;

			float startY = 0;
			float dist = 0;
			boolean isMenuHide = false;

			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();

				if (action == MotionEvent.ACTION_DOWN) {
					startY = event.getY();
				} else if (action == MotionEvent.ACTION_MOVE) {
					dist = event.getY() - startY;

					if ((pxToDp((int) dist) <= -DISTANCE) && !isMenuHide) {
						isMenuHide = true;
						hideMenuBar();
					} else if ((pxToDp((int) dist) > DISTANCE) && isMenuHide) {
						isMenuHide = false;
						showMenuBar();
					}

					if ((isMenuHide && (pxToDp((int) dist) <= -DISTANCE))
							|| (!isMenuHide && (pxToDp((int) dist) > 0))) {
						startY = event.getY();
					}
				} else if (action == MotionEvent.ACTION_UP) {
					startY = 0;
				}

				return false;
			}
		});



		postStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// Toast.makeText(context, "status", Toast.LENGTH_SHORT).show();
				Intent postStatusIntent = new Intent(getActivity(),
						PostStatusAcitivity.class);
				startActivity(postStatusIntent);
			}
		});

		postPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				selectImage();
				// Toast.makeText(context, "image", Toast.LENGTH_SHORT).show();
			}
		});

		postVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(context, "video", Toast.LENGTH_SHORT).show();
				selectVideo();
			}
		});


       /*
       Intent intent = new Intent(context, RouteActivity.class);
				intent.putExtra("type", "profile");
				intent.putExtra("user_id", feedList.get(pos).getUserId());
				startActivity(intent);
        */

    /*
		listView.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {

				feedList.clear();

				AQUtility.cleanCacheAsync(context);
				MainApplication.clearCache();

				LOAD_JSON_STATUS = 2;

				initAjax(-1, 0, 50);

				listView.onRefreshComplete();

			}
		});

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {

            public void onLoadMore() {

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        listView.onLoadMoreComplete();
                        listView.onRefreshComplete();
                    }
                }, 1500);

            }
        });
        */

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int index, long id) {
				//index = index - 1;
				String postId = feedList.get(index).getPostId();
				if (feedList.get(index).getPostType().equals("1")) {
					showVideoDetails(index);
				} else if (feedList.get(index).getPostType().equals("2")) {
					showVideoDetails(index);

				} else if (feedList.get(index).getPostType().equals("3")) {

					showVideoDetails(index);
				} else {

					showVideoDetails(index);

				}

			}
		});
		feedList.clear();
		adapter = new EverythingAdapter(feedList, getActivity());
		adapter.resetData();
		listView.setAdapter(adapter);

		initAjax(-1, 0, 50);

		return rootView;
	}

	public int pxToDp(int px) {
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		int dp = Math.round(px
				/ (dm.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	public void showMenuBar() {
		AnimatorSet animSet = new AnimatorSet();

		ObjectAnimator anim1 = ObjectAnimator.ofFloat(footer,
				View.TRANSLATION_Y, 0);

		animSet.playTogether(anim1);
		animSet.setDuration(300);
		animSet.start();
	}

	public void hideMenuBar() {
		AnimatorSet animSet = new AnimatorSet();

		ObjectAnimator anim1 = ObjectAnimator.ofFloat(footer,
				View.TRANSLATION_Y, footer.getHeight());

		animSet.playTogether(anim1);
		animSet.setDuration(300);
		animSet.start();
	}

	private void selectVideo() {
		final CharSequence[] items = { "Record Video", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Video!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Record Video")) {
					recordVideo();
				} else if (items[item].equals("Choose from Library")) {
					pickFile();
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

    File tempFile;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;
	private static final int RESULT_PICK_VIDEO = 4;
	private static final int RESULT_VIDEO_CAP = 5;
	private Uri mFileURI = null;

	public void pickFile() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("video/*");
		startActivityForResult(intent, RESULT_PICK_VIDEO);
	}

	public void recordVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, RESULT_VIDEO_CAP);
	}



	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_TAKE_PHOTO);
				} else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setDataAndType(
							MediaStore.Images.Media.INTERNAL_CONTENT_URI,
							"image/*");
					intent.setType("image/*");
					// intent.putExtra("crop", "true");
					// intent.putExtra("aspectX", 1);
					// intent.putExtra("aspectY", 1);
					// intent.putExtra("outputX", PHOTO_SIZE_WIDTH);
					// intent.putExtra("outputY", PHOTO_SIZE_HEIGHT);
					startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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
					String path = f.getAbsolutePath();
					bm = BitmapFactory.decodeFile(path, btmapOptions);

					int rotate = getCameraPhotoOrientation(path);
					
					

					Intent postPhotoIntent = new Intent(getActivity(),
							PostPhotoActivity.class);
					postPhotoIntent.putExtra("photo", path);
					postPhotoIntent.putExtra("rotate", rotate + "");
					// Toast.makeText(context, "rotate:" + rotate + "",
					// Toast.LENGTH_SHORT).show();

					startActivity(postPhotoIntent);

					f.delete();
					OutputStream fOut = null;
					File file = new File(path);
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
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

				String path = getRealPathFromURI(context, selectedImageUri);

				int rotate = getCameraPhotoOrientation(path);
				Intent postPhotoIntent = new Intent(getActivity(),
						PostPhotoActivity.class);
				postPhotoIntent.putExtra("photo", path);
				postPhotoIntent.putExtra("rotate", rotate + "");
				startActivity(postPhotoIntent);

			} else if (requestCode == RESULT_PICK_VIDEO) {
				if (resultCode == Activity.RESULT_OK) {
					mFileURI = data.getData();
					if (mFileURI != null) {
						Intent intent = new Intent(context,
								PostVideoActivity.class);
						intent.setData(mFileURI);
						startActivity(intent);
					}
				}
			} else if (requestCode == RESULT_VIDEO_CAP) {
				if (resultCode == Activity.RESULT_OK) {
					mFileURI = data.getData();
					if (mFileURI != null) {
						Intent intent = new Intent(context,
								PostVideoActivity.class);
						intent.setData(mFileURI);
						startActivity(intent);
					}
				}
			}
		}
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

	/*
	 * public void refresh() {
	 * 
	 * initAjax(-1, 0, 20);
	 * 
	 * }
	 */

	private void initAjax(int expire, int start, int size) {
		LOAD_JSON_STATUS = 1;
		String url;
		String cbName = "feedAllCb";
		String surl = "http://www.armymax.com/api/?action=getTimeline";

		url = surl + "&token=" + DataUser.VM_USER_TOKEN + "&type=public&app="
				+ app + "&startPoint=" + start + "&sizePage=" + size;

		Log.e("myurl", url);

        // ChatUIActivity.connectToRoom(getActivity(), roomName, true);

		aq.ajax(url, JSONObject.class, expire, this,
				cbName);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void feedAllCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

		if (jo.optInt("status") == 6001) {
			JSONArray ja = jo.optJSONArray("data");
			if (jo != null && ja != null) {
				feedList.clear();
				Log.e("myjson", jo.toString());
				for (int i = 0; i < ja.length(); i++) {
					JSONObject post = ja.getJSONObject(i);
					// String id = post.optString("ID");
					String userId = post.optString("UserID");

					String postId = post.optString("PostID");
					String postType = post.optString("PostType");
					String loveCount = post.optString("Loves");
					String commentCount = post.optString("Comments_n");
					// String viewCount = post.optString("Views");
					String userName = post.optString("UserName");
					String avatar = post.optString("UserAvatarPath");
					String userFirstName = post.optString("UserFirstName");
					String userLastName = post.optString("UserLastName");
					String statusText = "";

					String name = userFirstName + " " + userLastName;


					String ago = post.optString("Time");
                    if(!ago.equals("")) {
                        PrettyTime p = new PrettyTime();
                        long agoLong = Integer.parseInt(ago);
                        Date timeAgo = new java.util.Date((long) agoLong * 1000);
                        ago = p.format(timeAgo);
                    } else {
                        ago = "";
                    }


					if (!avatar.contains("facebook"))
						avatar = Data.BASE + avatar;

                    int type = 0;
                    if(postType != "")
                        type = Integer.parseInt(postType);


					String contentName = post.optString("videoTitle");
					String contentDesc = post.optString("videoDesc");
					String contentMeta = post.optString("videoType");
					String contentTbUrl = post.optString("videoThumbnail");
					String videoSource = post.optString("videoSource");

					if (app.equals("all")) {
						contentName = post.optString("videoTitle");
						contentDesc = post.optString("videoDesc");
						contentMeta = post.optString("videoDesc");
						contentTbUrl = post.optString("videoThumbnail");
						videoSource = post.optString("videoSource");

					} else if (app.equals("live")) {
						contentName = post.optString("liveTitle");
						contentDesc = post.optString("liveDesc");
						contentMeta = post.getJSONObject("stream_url")
								.optString("rtmp");
						contentTbUrl = post.optString("livePhoto");
						videoSource = post.optString("UserName");

					} else if (app.equals("photo")) {
						contentName = post.optString("photoText");
						contentDesc = post.optString("UserName");
						contentMeta = Data.BASE + post.optString("photoSource");
						contentTbUrl = Data.BASE + "thumbnail_800/"
								+ post.optString("photoSource") + ".png";
						videoSource = post.optString("UserName");
					}

					String loveUrl = post.getJSONObject("Love").optString(
							"Love_command");
					String loveStatus = post.getJSONObject("Love").getInt(
							"Love_status")
							+ "";

					switch (type) {
					case 1:
						statusText = post.optString("newsText");
						contentName = post.optString("newsText");
						break;
					case 2:
						statusText = post.optString("liveTitle");
						contentName = post.optString("liveTitle");

						/*
						if (post.optString("livePhoto").equals("vdomaxscreen")) {
							contentTbUrl = Data.BASE
									+ post.optString("livePhoto");
						} else {
						*/
					
							contentTbUrl = "http://www.armymax.com/live-photo/"+post.optString("UserName")+".png?"+ System.currentTimeMillis();
						//}

						JSONObject streamUrls = post
								.getJSONObject("stream_url");
						String rtmp = streamUrls.optString("rtmp");

						videoSource = userName;

						post.optString("livePermission");

						break;
					case 3:
						statusText = post.optString("photoText");

						contentName = "รูปภาพ โดย " + userName;

						contentMeta = Data.BASE + post.optString("photoSource");
						contentTbUrl = "http://www.armymax.com/photo/thumbnail_500/"
								+ post.optString("photoSource") + ".png";

						break;
					case 4:
						statusText = post.optString("videoText");
						contentMeta = post.optString("videoType");

						if (contentMeta.equals("youtube")
								|| post.optString("videoThumbnail").contains(
										"ytimg")) {
							contentTbUrl = post.optString("videoThumbnail");
						} else {
							contentTbUrl = Data.BASE
									+ post.optString("videoThumbnail");
						}

						if (contentMeta.equals("youtube")) {
							videoSource = "http://www.youtube.com/watch?v="
									+ videoSource;
						} else {
							
							videoSource = "http://www.armymax.com/vdofile/"
									+ videoSource;

						}

						contentTbUrl = Data.BASE
								+ post.optString("videoThumbnail");

						break;
					default:
						System.out.print("default");
						break;
					}
					COUNT++;

					DataFeedEverything feedElement = new DataFeedEverything(
							postId, postType, avatar, name, ago, statusText,
							contentTbUrl, contentName, contentDesc,
							contentMeta, loveCount, commentCount, videoSource,
							loveUrl, loveStatus, userId);

					feedList.add(feedElement);

				}
			}
			// adapter.append(feedList);
			new AdapterHelper().update(
					(ArrayAdapter<DataFeedEverything>) adapter,
					new ArrayList<Object>(feedList));
			adapter.notifyDataSetChanged();
            mPullToRefreshView.setRefreshing(false);
			// Utility.setListViewHeightBasedOnChildren(listView);
			//listView.onRefreshComplete();
            //listView.onLoadMoreComplete();
		} else {
			status.invalidate();
			Intent i = new Intent(getActivity(), LoginActivity.class);
			i.putExtra("logout", true);
			UserHelper user = new UserHelper(context);
			user.deleteSession();
			startActivity(i);
		}

	}

	public class AdapterHelper {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void update(ArrayAdapter arrayAdapter,
				ArrayList<Object> listOfObject) {
			arrayAdapter.clear();
			for (Object object : listOfObject) {
				arrayAdapter.add(object);
			}
		}
	}

	void showPhotoDetails(String postId, String imgUrl, String caption) {

		Intent photoDetailIntent = new Intent(context,
				PhotoDetailActivity.class);
		photoDetailIntent.putExtra("post_id", postId);
		photoDetailIntent.putExtra("url", imgUrl);
		photoDetailIntent.putExtra("caption", caption);
		startActivity(photoDetailIntent);
	}

	void showVideoDetails(int i) {
		Intent videoDetailIntent = new Intent(getActivity(),
				VideoDetailActivity.class);
		videoDetailIntent.putParcelableArrayListExtra("data", feedList);
		videoDetailIntent.putExtra("position", "" + i);
		startActivity(videoDetailIntent);
	}
}
