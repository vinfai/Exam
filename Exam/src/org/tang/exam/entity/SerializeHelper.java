package org.tang.exam.entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.util.Log;

public class SerializeHelper {
	private static final String TAG = "SerializeHelper";
	private static final String userFile = "userInfo.dat";
	private static final String classFile = "class.dat";
	private static final String childFile = "child.dat";

	public static void saveUserInfo(Context context, UserInfo userInfo) {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + userFile;
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(userInfo);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			Log.e(TAG, "Failed to saveUserInfo! " + e.toString());
		}
	}

	public static UserInfo restoreUserInfo(Context context) throws Exception {
		try {
			String filePath = context.getFilesDir().getPath() + "/" + userFile;
			FileInputStream fls = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fls);

			UserInfo userInfo = (UserInfo) ois.readObject();
			return userInfo;
		} catch (Exception e) {
			Log.e(TAG, "Failed to restoreUserInfo! " + e.toString());
			throw e;
		}
	}


}
