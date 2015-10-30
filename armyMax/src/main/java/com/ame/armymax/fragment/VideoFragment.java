package com.ame.armymax.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ame.armymax.LoginActivity;
import com.ame.armymax.PhotoDetailActivity;
import com.ame.armymax.R;
import com.ame.armymax.RouteActivity;
import com.ame.armymax.VideoDetailActivity;
import com.ame.armymax.adapter.EverythingAdapter;
import com.ame.armymax.app.MainApplication;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataFeedEverything;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.post.PostPhotoActivity;
import com.ame.armymax.post.PostStatusAcitivity;
import com.ame.armymax.post.PostVideoActivity;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

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

@SuppressLint({"ValidFragment", "NewApi", "CutPasteId"})


public class VideoFragment extends Fragment {

    AQuery aq;
    AQuery aq2;
    View rootView;

    int LOAD_JSON_STATUS = 0;
    int START_FROM = 0;
    int COUNT = 0;

    Context context;

    public ArrayList<DataFeedEverything> feedList = new ArrayList<DataFeedEverything>();
    public ListView listView;
    public EverythingAdapter adapter;

    LinearLayout footer;
    ListView scrollView;

    public VideoFragment() {

    }

    public VideoFragment(Context context) {
        super();
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AQUtility.cleanCacheAsync(context);
        MainApplication.clearCache();

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        feedList.clear();

        aq = new AQuery(rootView);
        aq2 = new AQuery(rootView);

        listView = (ListView) rootView
                .findViewById(R.id.feed_listview);

        footer = (LinearLayout) rootView.findViewById(R.id.footer_layout);
        scrollView = (ListView) rootView
                .findViewById(R.id.feed_listview);
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

        Button postStatus = (Button) rootView.findViewById(R.id.button_recent);
        Button postPhoto = (Button) rootView.findViewById(R.id.button_photo);
        Button postVideo = (Button) rootView.findViewById(R.id.button_video);

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

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, RouteActivity.class);
                intent.putExtra("type", "profile");
                intent.putExtra("user_id", feedList.get(pos).getUserId());
                startActivity(intent);
                return true;
            }
        });

        /*
        listView.setOnRefreshListener(new OnRefreshListener() {

            public void onRefresh() {

                feedList.clear();

                AQUtility.cleanCacheAsync(context);
                MainApplication.clearCache();

                LOAD_JSON_STATUS = 2;

                initAjax(0, 0, 50);

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
                    }
                }, 1500);

            }
        });
        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v,
                                    int index, long id) {
                index = index - 1;
                String postId = feedList.get(index).getPostId();
                if (feedList.get(index).getPostType().equals("1")) {
                    showVideoDetails(index);
                } else if (feedList.get(index).getPostType().equals("2")) {
                    showVideoDetails(index);
                    /*
					
					i.putExtra("isPlay", "1");
					i.putExtra("roomId", feedList.get(index).getVideoSource());
					i.putExtra("roomTag", "0");
					startActivity(i);
					 */
                } else if (feedList.get(index).getPostType().equals("3")) {
                    //String status = feedList.get(index).getStatus();
                    //String contentTbUrl = feedList.get(index).getContentTbUrl();
                    // Toast.makeText(context, contentTbUrl,
                    // Toast.LENGTH_LONG).show();
                    // Log.e("myurl", contentTbUrl);
                    // showPhotoDetails(postId, contentTbUrl, status);

                    showVideoDetails(index);
                } else {
                    String contentMeta = feedList.get(index).getContentMeta();
                    String videoSource = feedList.get(index).getVideoSource();


                    showVideoDetails(index);

                }

            }
        });
        feedList.clear();
        adapter = new EverythingAdapter(feedList, getActivity());
        adapter.resetData();
        listView.setAdapter(adapter);

        initAjax(0, 0, 50);

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
        final CharSequence[] items = {"Record Video", "Choose from Library",
                "Cancel"};

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

    File tempFile;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CHOOSE_PHOTO = 2;

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

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
                } else if (items[item].equals("Choose from Library")) {
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
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    Intent postPhotoIntent = new Intent(getActivity(),
                            PostPhotoActivity.class);
                    postPhotoIntent.putExtra("photo", f.getAbsolutePath());
                    startActivity(postPhotoIntent);

                    String path = tempFile.getAbsolutePath();
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

                String path = getRealPathFromURI(context, selectedImageUri);

                Intent postPhotoIntent = new Intent(getActivity(),
                        PostPhotoActivity.class);
                postPhotoIntent.putExtra("photo", path);
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
            String[] proj = {MediaStore.Images.Media.DATA};
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
        String cbName;

        String surl = "http://www.armymax.com/api/?action=getTimeline";
        // debug!!!
        // DataUser.VM_USER_TOKEN = "asdf11";
        // String surl = "https://api.vdomax.com/service/getTimeline/mobile/";
        String app = "all";

        url = surl + "&token=" + DataUser.VM_USER_TOKEN + "&type=public&app="
                + app + "&startPoint=" + start + "&sizePage=" + size;
        cbName = "feedAllCb";

        Log.e("myurl", url);

        //listView.onRefreshComplete();

        aq.progress(R.id.progress).ajax(url, JSONObject.class, expire, this,
                cbName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void feedAllCb(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {

        if (jo.optInt("status") != 2003) {
            JSONArray ja = jo.optJSONArray("data");
            if (jo != null && ja != null) {
                feedList.clear();
                Log.e("myjson", jo.toString());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject post = ja.getJSONObject(i);
                    String id = post.getString("ID");
                    String userId = post.getString("UserID");

                    String postId = post.getString("PostID");
                    String postType = post.getString("PostType");
                    String loveCount = post.getString("Loves");
                    String commentCount = post.getString("Comments_n");
                    // String viewCount = post.getString("Views");
                    String userName = post.getString("UserName");
                    String avatar = post.getString("UserAvatarPath");
                    String userFirstName = post.getString("UserFirstName");
                    String userLastName = post.getString("UserLastName");
                    String statusText = "";

                    String name = userFirstName + " " + userLastName;

                    String ago = post.getString("Time");
                    PrettyTime p = new PrettyTime();
                    long agoLong = Integer.parseInt(ago);
                    Date timeAgo = new java.util.Date((long) agoLong * 1000);
                    ago = p.format(timeAgo);

                    if (!avatar.contains("facebook"))
                        avatar = Data.BASE + avatar;

                    int type = Integer.parseInt(postType);

                    String contentName = post.getString("videoTitle");
                    String contentDesc = post.getString("videoDesc");
                    String contentMeta = post.getString("videoDesc");
                    String contentTbUrl = post.getString("videoThumbnail");
                    String videoSource = post.getString("videoSource");
                    String loveUrl = post.getJSONObject("Love").getString(
                            "Love_command");
                    String loveStatus = post.getJSONObject("Love").getInt(
                            "Love_status")
                            + "";

                    switch (type) {
                        case 1:
                            statusText = post.getString("newsText");

                            break;
                        case 2:
                            statusText = post.getString("liveTitle");
                            contentName = post.getString("liveTitle");

                            if (post.getString("livePhoto").equals("vdomaxscreen")) {
                                contentTbUrl = Data.BASE
                                        + post.getString("livePhoto");
                            } else {
                                contentTbUrl = Data.BASE + post.getString("livePhoto")
                                        + "thumbnail_500";
                            }

                            JSONObject streamUrls = post.getJSONObject("stream_url");
                            String rtmp = streamUrls.optString("rtmp");

                            // videoSource = "http://"+Data.LIVE+":8080/hls/" + userName
                            // + ".m3u8";
                            videoSource = userName;

                            post.getString("livePermission");

                            break;
                        case 3:
                            statusText = post.getString("photoText");

                            Log.i("myphoto", post.getString("photoSource"));

                            contentTbUrl = Data.BASE + post.getString("photoSource");

                            break;
                        case 4:
                            statusText = post.getString("videoText");
                            contentMeta = post.getString("videoType");

                            if (contentMeta.equals("youtube")
                                    || post.getString("videoThumbnail").contains(
                                    "ytimg")) {
                                contentTbUrl = post.getString("videoThumbnail");
                            } else {
                                contentTbUrl = Data.BASE
                                        + post.getString("videoThumbnail");
                            }

                            if (contentMeta.equals("youtube")) {
                                videoSource = "http://www.youtube.com/watch?v="
                                        + videoSource;
                            } else {
                                videoSource = "http://www.armymax.com/vdofile/" + videoSource;

                            }

                            contentTbUrl = Data.BASE
                                    + post.getString("videoThumbnail");


                            break;
                        default:
                            System.out.print("default");
                            break;
                    }
                    COUNT++;
                    // statusText = COUNT + "]";

                    DataFeedEverything feedElement = new DataFeedEverything(postId,
                            postType, avatar, name, ago, statusText, contentTbUrl,
                            contentName, contentDesc, contentMeta, loveCount,
                            commentCount, videoSource, loveUrl, loveStatus, userId);

                    feedList.add(feedElement);

                }
            }

            //adapter.append(feedList);
            new AdapterHelper().update((ArrayAdapter<DataFeedEverything>) adapter,
                    new ArrayList<Object>(feedList));
            adapter.notifyDataSetChanged();
            // Utility.setListViewHeightBasedOnChildren(listView);
            //listView.onRefreshComplete();
        } else {
            Toast.makeText(context, jo.optString("msg"), Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        }


    }

    public class AdapterHelper {
        @SuppressWarnings({"rawtypes", "unchecked"})
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
