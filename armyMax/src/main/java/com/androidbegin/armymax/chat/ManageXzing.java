package com.androidbegin.armymax.chat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class ManageXzing {
	Context context;
	public ManageXzing(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public boolean checkIsSutup(){
		boolean isZxingInstalled = false;;
		try
		{
			PackageManager pm = context.getPackageManager();
			//ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.zxing.client.android", 0 );
			pm.getPackageInfo("com.google.zxing.client.android", PackageManager.GET_ACTIVITIES);
			isZxingInstalled = true;
		}
		catch(PackageManager.NameNotFoundException e){
		    isZxingInstalled=false;
		}
		return isZxingInstalled;
	}
	public void toSetup(){
		/*
	     *Checking whether PlayStore is installed in device or not?
	     */
	     boolean isPlayStoreInstalled;
	     try
	     {
	      ApplicationInfo i=context.getPackageManager().getApplicationInfo("com.google.vending", 0 );
	      	isPlayStoreInstalled = true;
	      }
	     catch(PackageManager.NameNotFoundException e){
	                isPlayStoreInstalled=false;
	          }

	      /*
	       * If it is the download Zxing
	       */   
	      if(isPlayStoreInstalled)
	       {
	        Intent DownloadZxing = new Intent(Intent.ACTION_VIEW,Uri.parse("market://detailsid=com.google.zxing.client.android"));
	        context.startActivity(DownloadZxing);
	        }
	      else //Toast message indicating No PlayStore Found
	       {
	         Toast.makeText(context,"Install PlayStore First",Toast.LENGTH_SHORT).show();
	        }
	}
}
