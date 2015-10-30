package com.androidbegin.armymax.chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.pref.UserHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class AProfileActivity extends Activity{
	private ImageView imgQRCode; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_profile_activity);
		DataUser.context = this;
		
		imgQRCode = (ImageView)findViewById(R.id.imageQRCoed);
		imgQRCode.setImageBitmap(genarateQR());
		
	}
	Bitmap genarateQR(){
		Bitmap bm = null;
		QRCodeWriter writer = new QRCodeWriter();
		try {
			UserHelper user = new UserHelper(AProfileActivity.this);
		    BitMatrix matrix = writer.encode(
		        user.getUserName()/*Add User Name to QRCode */
		        , BarcodeFormat.QR_CODE, 600, 600
		    );
		    bm = toBitmap(matrix);
		    // Now what??
		} catch (WriterException e) {
		    e.printStackTrace();
		}
		return bm;
	}
	public static Bitmap toBitmap(BitMatrix matrix){
	    int height = matrix.getHeight();
	    int width = matrix.getWidth();
	    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    for (int x = 0; x < width; x++){
	        for (int y = 0; y < height; y++){
	            bmp.setPixel(x, y, matrix.get(x,y) ? Color.BLACK : Color.WHITE);
	        }
	    }
	    return bmp;
	}

}
