package org.tang.exam.common;

import java.util.ArrayList;

import org.tang.exam.base.MyApplication;
import org.tang.exam.entity.SerializeHelper;
import org.tang.exam.entity.UserInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public final class UserCache {
	private static final String TAG = "UserCache";

	private static UserCache mInstance = null;
	private final static String DB_NAME = "logon.pref";
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mSettings;
	private Context mContext;
	private ArrayList<String> mTags = new ArrayList<String>();

	private boolean logon = false;
	private String sessionKey = "";
	private String password = "";
	private UserInfo userInfo = null;

	private UserCache() {
		mContext = MyApplication.getInstance().getApplicationContext();
		mSettings = mContext.getSharedPreferences(DB_NAME, 0);
		mEditor = mSettings.edit();
		initUserCache();
	}

	public static synchronized UserCache getInstance() {
		if (mInstance == null) {
			mInstance = new UserCache();
		}
		return mInstance;
	}

	private void initUserCache() {
		logon = mSettings.getBoolean("logon", false);
		if (logon) {
			try {
				sessionKey = mSettings.getString("sessionKey", "examtang01");
				password = mSettings.getString("password", "****");
				userInfo = SerializeHelper.restoreUserInfo(mContext);
			} catch (Exception e) {
				Log.e(TAG, "Failed to deserialize cache file! " + e.toString());
				logon = false;
			}
		}
	}

	public boolean isLogon() {
		return logon;
	}

	public void setLogon(boolean value) {
		this.logon = value;
		mEditor.putBoolean("logon", value);
		mEditor.commit();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		mEditor.putString("password", password);
		mEditor.commit();
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		mEditor.putString("sessionKey", sessionKey);
		mEditor.commit();
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		SerializeHelper.saveUserInfo(mContext, userInfo);
	}

	public ArrayList<String> getTags() {
		return mTags;
	}

}
