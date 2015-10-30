package com.androidbegin.armymax.chat;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

public class AddFriendActivity extends Activity implements OnClickListener {

	private LinearLayout layoutQRCode, layoutSearchByID;
	ImageView imgQRCode;
	
	String USER_NAME = DataUser.VM_USER_NAME;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_addfriend_activity);
		context = this;
		DataUser.context = this;

		initUI();
	}

	void initUI() {
		layoutQRCode = (LinearLayout) findViewById(R.id.layoutMenuQrcode);
		layoutSearchByID = (LinearLayout) findViewById(R.id.layoutMenuSearchByID);
		imgQRCode = (ImageView) findViewById(R.id.my_qr);
		
		imgQRCode.setImageBitmap(genarateQR(USER_NAME));

		layoutQRCode.setOnClickListener(this);
		layoutSearchByID.setOnClickListener(this);

	}
	
	Bitmap genarateQR(String username) {
		Bitmap bm = null;
		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(username, BarcodeFormat.QR_CODE,
					600, 600);
			bm = toBitmap(matrix);
			// Now what??
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bm;
	}

	public static Bitmap toBitmap(BitMatrix matrix) {
		int height = matrix.getHeight();
		int width = matrix.getWidth();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == layoutQRCode) {

			ManageXzing xzing = new ManageXzing(this);
			if (xzing.checkIsSutup()) {
				// String packageString = "com.ame.armymax.chat";
				// Intent intent = new
				// Intent("com.google.zxing.client.android.SCAN");
				// intent.setPackage(packageString);
				// intent.putExtra("SCAN_MODE", "SCAN_MODE");
				// startActivityForResult(intent, 0);

				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
				startActivityForResult(intent, 0);
			} else {
				// Intent DownloadZxing = new
				// Intent(Intent.ACTION_VIEW,Uri.parse("market://detailsid=com.google.zxing.client.android"));
				// startActivity(DownloadZxing);
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ "com.google.zxing.client.android")));
				// xzing.toSetup();
			}

		} else if (view == layoutSearchByID) {
			Intent toSearchByID = new Intent(this, SearchByIDActivity.class);
			startActivity(toSearchByID);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				String txtUserName_Result = intent
						.getStringExtra("SCAN_RESULT");
				// System.out.println("From QR : "+txtUserName_Result);
				// dialogComfirmFriend(txtId_Result);

				TaskSearchFriend taskSearchFriend = new TaskSearchFriend(this,
						txtUserName_Result, new OnSearchFriendListener() {

							@Override
							public void SearchFriendSuccess(
									ArrayList<FriendSearchModel> arrSearchFriend) {
								// TODO Auto-generated method stub
								if (arrSearchFriend.size() > 0) {
									FriendSearchModel friendModel = arrSearchFriend
											.get(0);
									dialogComfirmFriend(
											friendModel.getUserAvatarPath(),
											friendModel.getUserName(),
											friendModel.getUserID());
								} else {
									Toast.makeText(AddFriendActivity.this,
											"Friend Not found.",
											Toast.LENGTH_LONG).show();
								}
							}
						});
				taskSearchFriend.execute();

				// TaskAddFriend taskAddFriend = new
				// TaskAddFriend(AddFriendActivity.this, friendID, callback)
			} else if (resultCode == RESULT_CANCELED) {
				// System.out.println("Scan cancelled");
			}
		}
	}

	void dialogComfirmFriend(String avatar, String friednName, final String friendID) {
		final Dialog dialogComfirm = new Dialog(AddFriendActivity.this,R.style.Dialog);
		
		dialogComfirm.getWindow().setLayout(400, 400);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.chat_dialog_comfirmfriend, null);
		dialogComfirm.setContentView(view);
		ImageView friendAvatar = (ImageView) view.findViewById(R.id.friendAvatar);
		
		String avatarPath = Link.LINK_PHOTO + avatar;
		
		Picasso.with(context).load(avatarPath).into(friendAvatar);
		
		TextView textFriendName = (TextView) view
				.findViewById(R.id.textFriendName);
		textFriendName.setText(friednName);
		Button btYes = (Button) view.findViewById(R.id.btYes);
		btYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addFriend(friendID, dialogComfirm);
			}
		});
		Button btNo = (Button) view.findViewById(R.id.btNo);
		btNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogComfirm.dismiss();
			}
		});

		dialogComfirm.setTitle("เพิ่มเป็นเพื่อน ?");
		
		dialogComfirm.show();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialogComfirm.getWindow().getAttributes());
		lp.width = 450;
		lp.height = 550;
		dialogComfirm.getWindow().setAttributes(lp);
	}

	void addFriend(String friendID, final Dialog dialogComfirm) {
		TaskAddFriend task = new TaskAddFriend(AddFriendActivity.this,
				friendID, new OnAddfriendListener() {

					@Override
					public void addFriendSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(AddFriendActivity.this, "เพิ่มเพื่อนสำเร็จแล้ว",
								Toast.LENGTH_LONG).show();
						dialogComfirm.dismiss();
					}

					@Override
					public void addFriendFailed() {
						// TODO Auto-generated method stub
						Toast.makeText(AddFriendActivity.this,
								"เป็นเพื่อนกันอยู่แล้ว", Toast.LENGTH_LONG)
								.show();
						dialogComfirm.dismiss();
					}
				});
		task.execute();
	}
}
