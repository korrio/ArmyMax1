package com.ame.armymax.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ame.armymax.LoginActivity;
import com.ame.armymax.PhotoDetailActivity;
import com.ame.armymax.R;
import com.ame.armymax.adapter.PhotoAdapter;
import com.ame.armymax.model.Data;
import com.ame.armymax.model.DataPhoto;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

@SuppressLint("ValidFragment")
public class PhotoFragment extends Fragment {

    AQuery aq;
    View rootView;

    Context context;
    ListView listView;

    String LOVE_URL;
    String LOVE_STATUS;

    ArrayList<String> loveCommand = new ArrayList<String>();
    LinearLayout footer;

    public PhotoFragment() {
        // TODO Auto-generated constructor stub
    }

    public static PhotoFragment newInstance() {
        PhotoFragment myFragment = new PhotoFragment();
        return myFragment;
    }

    public PhotoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        context = getActivity();
        aq = new AQuery(rootView);

        footer = (LinearLayout) rootView.findViewById(R.id.footer_layout);
        footer.setVisibility(View.GONE);

        String url;
        String cbName;

        String token = DataUser.VM_USER_TOKEN;
        //String token = "asdf11";
        String surl = "https://www.armymax.com/api/?action=getTimeline";
        String app = "photo";
        String start = "0";
        String size = "50";

        url = surl + "&token=" + token + "&type=public&app=" + app
                + "&startPoint=" + start + "&sizePage=" + size;
        cbName = "feedPhotoCb";

        aq.progress(R.id.progress).ajax(url, JSONObject.class, this, cbName);

        listView = (ListView) rootView
                .findViewById(R.id.feed_listview);

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                String url = loveCommand.get(pos);
                aq.progress(R.id.progress).ajax(url, JSONObject.class, rootView, "loveCommandCb");
                return true;
            }
        });

        return rootView;
    }

    public void loveCommandCb(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        int jsonStatus = jo.optInt("status");
        Toast.makeText(getActivity(), jo.optString("msg"), Toast.LENGTH_SHORT).show();
        if (jsonStatus == 8001) {

        } else {

        }
    }

    public void feedPhotoCb(String url, JSONObject jo, AjaxStatus status)
            throws JSONException {
        int jsonStatus = jo.getInt("status");
        if (jsonStatus == 2003) {
            DataUser.clearAll();
            Intent login = new Intent(getActivity(), LoginActivity.class);
            getActivity().finish();
            startActivity(login);
        } else {
            JSONArray ja = jo.getJSONArray("data");
            if (ja != null) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject post = ja.getJSONObject(i);
                    String id = post.getString("ID");
                    String userId = post.getString("UserID");
                    String postType = post.getString("PostType");
                    DataPhoto.setPostType(postType);
                    String postId = post.getString("PostID");
                    String time = post.getString("Time");
                    String favoriteCount = post.getString("Favorits");
                    String loveCount = post.getString("Loves");
                    String commentCount = post.getString("Comments_n");
                    // post.getString("Comments");
                    String viewCount = post.getString("Views");
                    String userName = post.getString("UserName");
                    String avatar = post.getString("UserAvatarPath");
                    String userFirstName = post.getString("UserFirstName");
                    String userLastName = post.getString("UserLastName");
                    String statusText = post.getString("photoText");


                    loveCommand.add(LOVE_URL);

                    LOVE_URL = post.getJSONObject("Love").getString(
                            "Love_command");
                    LOVE_STATUS = post.getJSONObject("Love").getInt(
                            "Love_status")
                            + "";

                    String ago = post.getString("Time");

                    String name = post.getString("UserFirstName") + " "
                            + post.getString("UserLastName");

                    PrettyTime p = new PrettyTime();
                    long agoLong = Integer.parseInt(ago);
                    Date timeAgo = new java.util.Date((long) agoLong * 1000);

                    ago = p.format(timeAgo);

                    // general
                    DataPhoto.setName(name);
                    DataPhoto.setStatus(statusText);
                    DataPhoto.setAgo(ago);
                    DataPhoto.setCommentCount(commentCount);
                    DataPhoto.setLoveCount(loveCount);
                    DataPhoto.setPostId(postId);

                    if (avatar.contains("facebook"))
                        DataPhoto.setTbUrl(avatar);
                    else
                        DataPhoto.setTbUrl(Data.BASE + avatar);

                    String contentTbUrl = Data.BASE
                            + post.getString("photoSource");
                    DataPhoto.setContentTbUrl(contentTbUrl);
                    DataPhoto.setContentName("");
                    DataPhoto.setContentDesc("");
                    DataPhoto.setContentMeta("");

                }
            }
        }

        final PhotoAdapter adapter = new PhotoAdapter(getActivity(), 3);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long id) {
                i = i - 1;
                String postId = DataPhoto.getPostId(i);
                String status = DataPhoto.getStatus(i);
                String contentTbUrl = DataPhoto.getContentTbUrl(i);
                showPhotoDetails(postId, contentTbUrl, status);
            }
        });

        adapter.notifyDataSetChanged();


    }

    void showPhotoDetails(String postId, String imgUrl, String caption) {
        Intent photoDetailIntent = new Intent(context,
                PhotoDetailActivity.class);
        photoDetailIntent.putExtra("post_id", postId);
        photoDetailIntent.putExtra("url", imgUrl);
        photoDetailIntent.putExtra("caption", caption);
        startActivity(photoDetailIntent);
    }

}
