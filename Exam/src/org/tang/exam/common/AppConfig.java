package org.tang.exam.common;


import org.tang.exam.base.MyApplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 程序常量配置：配置运行时变量
 * @author Administrator
 *
 */
public class AppConfig {
	private static AppConfig mInstance = null;
	private final static String DB_NAME = "config.pref";
	private SharedPreferences.Editor mEditor;
	private SharedPreferences mSettings;

	private AppConfig() {
		Context context = MyApplication.getInstance().getApplicationContext();
		mSettings = context.getSharedPreferences(DB_NAME, 0);
		mEditor = mSettings.edit();
	}

	public static synchronized AppConfig getInstance() {
		if (mInstance == null) {
			mInstance = new AppConfig();
		}
		return mInstance;
	}
	
	/**
	 * 是否首次运行
	 * @return
	 */
	public boolean getFirstRun() {
		return mSettings.getBoolean("firstRun", true);
	}

	public void setFirstRun(boolean value) {
		mEditor.putBoolean("firstRun", value);
		mEditor.commit();
	}
}
