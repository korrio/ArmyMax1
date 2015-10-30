package com.androidbegin.armymax.chat;

import java.util.ArrayList;


public interface OnLogin {
	public void loginSuccess(ArrayList<LoginModel> arrLogin);
	public void loginFailed();
}
