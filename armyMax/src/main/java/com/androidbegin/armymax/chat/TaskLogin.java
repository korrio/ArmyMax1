package com.androidbegin.armymax.chat;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class TaskLogin extends AsyncTask<Void, Void, Void>{
	
	private Context context;
	private String username;
	private String password;
	private ProgressDialog progressDialog;
	private OnLogin callback;
	private boolean isSuccess = false;
	private ArrayList<LoginModel> arrLogin ;
	public TaskLogin(Context context , String username , String password , OnLogin callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.username = username;
		this.password = password;
		this.callback = callback;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(context, "", "Loading...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		List<NameValuePair> paramsLogin = new ArrayList<NameValuePair>();
		paramsLogin.add(new BasicNameValuePair("username", username));
		paramsLogin.add(new BasicNameValuePair("password", password));
		String txtResult = Utils.getHttpPost(Link.LOGIN, paramsLogin);
		//System.out.println("txtResult : "+txtResult);
		arrLogin = new ArrayList<LoginModel>();
		try {
			JSONObject Jobj = new JSONObject(txtResult);
			arrLogin.add(new LoginModel(
					Jobj.getString("status"),
					Jobj.getString("msg"),
					Jobj.getString("token"),
					Jobj.getString("userID"), 
					Jobj.getString("username"), 
					Jobj.getString("Name")));
			
			isSuccess = true;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		if (isSuccess) {
			callback.loginSuccess(arrLogin);
		}
		else {
			callback.loginFailed();
		}
		progressDialog.dismiss();
		super.onPostExecute(result);
	}

}
