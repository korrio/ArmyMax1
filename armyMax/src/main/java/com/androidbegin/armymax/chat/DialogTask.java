package com.androidbegin.armymax.chat;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogTask {
	private ProgressDialog progressDialog;
	public DialogTask(Context context) {
		// TODO Auto-generated constructor stub
		progressDialog = ProgressDialog.show(context, "", "Loading...");
		progressDialog.setCanceledOnTouchOutside(false);
		
	}
	public void showDialog(){
		progressDialog.show();
	}
	public void dismissDialog(){
		progressDialog.dismiss();
	}
}
