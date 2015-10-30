package com.ame.armymax.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserManager {

    /**
     * KEY_PREFS ไว้สำหรับเป็น key ของ SharedPreferences
     */
    private final String KEY_PREFS = "prefs_user";

    /**
     * ชื่อ key ที่ไว้เซฟ username ใน SharedPreferences
     */
    private final String KEY_USERNAME = "username";

    /**
     * ชื่อ key ที่ไว้เซฟ password ใน SharedPreferences.
     */
    private final String KEY_PASSWORD = "password";


    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    /**
     * รับค่า Context เพื่อเอาไว้ใช้สำหรับ getSharedPreferences
     * @param context
     */
    public UserManager(Context context) {
        mPrefs = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    /**
     * ทำการเช็ค Username กับ Password ใน SharedPreferences<br />
     * โดยเงื่อนไข EditText ของ Username และ password ต้องไม่เป็นค่าว่าง <br />
     * และค่าที่ได้ ต้องตรงกับใน SharedPreferences
     * @param username - username จาก EditText ที่ใส่
     * @param password - password จาก EditText ที่ใส่
     * @return หากใส่ข้อมูล ให้ส่งค่ากลับเป็น true, ถ้าใส่ผิดก็ส่ง false
     */
    public boolean checkLoginValidate(String username, String password) {
        String realUsername = mPrefs.getString(KEY_USERNAME, "");
        String realPassword = mPrefs.getString(KEY_PASSWORD, "");

        if ( (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) &&
                username.equals(realUsername) &&
                password.equals(realPassword)) {
            return true;
        }
        return false;
    }

    /**
     * เมธอดสำหรับลงทะเบียนสมาชิกใหม่ โดยส่งค่า username และ password มา<br />
     * จากนั้นจะเซฟลง SharedPreferences 
     * @param username - username จาก EditText ที่ใส่
     * @param password - password จาก EditText ที่ใส่
     * @return ส่งค่ากลับไปเป็น false หากลงทะเบียนไม่สำเร็จ <br />
     * เป็น true หากลงทะเบียนสำเร็จ
     */
    public boolean registerUser(String username, String password) {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return false;
        }

        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_PASSWORD, password);
        return mEditor.commit();
    }
}
