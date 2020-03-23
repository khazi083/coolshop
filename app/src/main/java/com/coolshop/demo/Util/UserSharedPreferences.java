package com.coolshop.demo.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreferences {
    private static final String PREF_NAME = "coolshop.dat";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL_ID = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String ACCESS_TOKEN = "token";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;


    @SuppressLint("CommitPrefEdits")
    public UserSharedPreferences(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String token) {
        editor.putString(ACCESS_TOKEN, token);
        editor.commit();
    }

    public String getEmailId() {
        return pref.getString(KEY_USER_EMAIL_ID, "");
    }

    public void setEmailId(String emailId) {
        editor.putString(KEY_USER_EMAIL_ID, emailId);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(KEY_USER_PASSWORD, "");
    }

    public void setPassword(String password) {
        editor.putString(KEY_USER_PASSWORD, password);
        editor.commit();
    }


    public void clearSession() {
        editor.clear();
    }

}