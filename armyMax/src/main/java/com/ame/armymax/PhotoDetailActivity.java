package com.ame.armymax;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.R.id;
import com.ame.armymax.R.layout;
import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.AQUtility;

@SuppressLint("ValidFragment")
public class PhotoDetailActivity extends Activity {

	static AQuery aq;
	View rootView;
	Context context;

	String imgUrl;
	String imgCaption;
	private String POST_ID = "";

	Button postComment;
	EditText commentBox;

	WebView web;
	Button comment;
	ProgressBar progress;

	public PhotoDetailActivity() {

	}

	public PhotoDetailActivity(String postId, String url, String statusText) {
		POST_ID = postId;
		imgUrl = url;
		imgCaption = statusText;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_photo_detail);

		context = this;
		DataUser.context = context;
		Intent i = getIntent();
		POST_ID = i.getStringExtra("post_id");
		imgUrl = i.getStringExtra("url");
		imgCaption = i.getStringExtra("caption");

		aq = new AQuery(context);

		web = (WebView) findViewById(R.id.webPhoto);
		progress = (ProgressBar) findViewById(R.id.progressBar1);
		WebSettings ws = web.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);
		ws.setDisplayZoomControls(false);
        //ws.setDisplayZoomControls(false);
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			web.setDrawingCacheEnabled(true);
		}

		String source = getSource(web.getContext());
		String html = source.replace("@src", imgUrl);

		// wv.setInitialScale(100);
		web.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
		//web.setBackgroundColor(R.color.black);
		// settings.setJavaScriptCanOpenWindowsAutomatically(true);
		// settings.setSupportMultipleWindows(true);
		ws.setUseWideViewPort(true);
		imgUrl = imgUrl.replace("/thumbnail_500", "");

		//web.loadUrl(imgUrl);

		// web.setWebViewClient(new myWebClient());
		
		
		
		web.setWebChromeClient(new WebChromeClient() {
			public void onPageFinished(WebView view, String url) {
				progress.setVisibility(View.GONE);
		    }
		});
		
		

		if(imgCaption != "" || imgCaption != null) {
		
		TextView caption = (TextView) findViewById(R.id.caption);
		caption.setText(imgCaption);
		
		}

		postComment = (Button) findViewById(R.id.button_recent);
		commentBox = (EditText) findViewById(R.id.comment_box);
		comment = (Button) findViewById(R.id.comment);
		
		postComment.setVisibility(View.GONE);
		commentBox.setVisibility(View.GONE);
		
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
			}
		});
		

		// postComment.setVisibility(View.GONE);

		postComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String postId = POST_ID;
				String commentText = commentBox.getEditableText().toString()
						.replace("\n", "%0A");
				String postCommentUrl = "http://www.armymax.com/api/action?=postComment";
				String postType = "3";
				postCommentUrl += "&token=" + DataUser.VM_USER_TOKEN
						+ "&postID=" + postId + "&postType=" + postType
						+ "&text=" + commentText;
				
				Log.e("postcomment",postCommentUrl);

				ProgressDialog dialog = new ProgressDialog(context);
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setInverseBackgroundForced(false);
				dialog.setCanceledOnTouchOutside(true);
				dialog.setTitle("Posting comment...");
				aq.progress(dialog).ajax(postCommentUrl, JSONObject.class,
						context, "commentCb");
				commentBox.setText("");
				// removeCurrentFragment();
			}
		});

		// imgUrl = imgUrl.replace("/thumbnail_500", "");
		//aq.id(R.id.webPhoto).progress(R.id.progress).webImage(imgUrl);

	}
	
	

	private static String template;
	private static String getSource(Context context) {

		if (template == null) {

			try {
				InputStream is = context.getClassLoader().getResourceAsStream(
						"com/androidquery/util/web_image.html");
				template = new String(AQUtility.toBytes(is));
			} catch (Exception e) {
				AQUtility.debug(e);
			}

		}

		return template;

	}

	public void commentCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		Log.e("mycomment", jo.toString());
		if (jo != null) {

			Toast.makeText(context, jo.getString("msg"), Toast.LENGTH_SHORT)
					.show();
		}
	}

}
