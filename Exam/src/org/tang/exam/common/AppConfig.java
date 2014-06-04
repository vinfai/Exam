package org.tang.exam.common;


import java.util.List;

import org.tang.exam.base.MyApplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
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
	
	
	public static boolean isTopActivity(Context activity,String shortActivityName){  
        String currentClassName = shortActivityName;  
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);  
        List<RunningTaskInfo>  tasksInfo = activityManager.getRunningTasks(1);    
        if(tasksInfo.size() > 0){    
            System.out.println("---------------包名-----------"+tasksInfo.get(0).topActivity.getClassName());  
            //应用程序位于堆栈的顶层    
            if(currentClassName.equals(tasksInfo.get(0).topActivity.getClassName())){    
                return true;    
            }    
        }    
        return false;  
    }  
}
