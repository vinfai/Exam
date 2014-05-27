package org.tang.exam.db;

import java.util.ArrayList;
import java.util.List;

import org.tang.exam.common.UserCache;
import org.tang.exam.entity.UserInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class UserInfoDBAdapter extends DBAdapter {
	private static final String TAG = "UserInfoDBAdapter";
	private int MAX_NUMBER = 20;
	private String mUserId="";
	
	
	private List<UserInfo> getUserInfoExist(String userId) {
		String where = String.format(" userId = '%s'  ", 
				userId);
		return getUserInfoByWhere(where);
	}
	
	public List<UserInfo> getUserInfoInOrg(String orgid) {
		String where = String.format(" orgid = '%s'  ", 
				orgid);
		return getUserInfoByWhere(where);
	}
	
	
	private ArrayList<UserInfo> getUserInfoByWhere(String where) {
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		String orderBy = "userId desc ";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("UserInfo", new String[] {"userId", "userName", "sex",
				"phone", "picUrl","pinYin","initial","userType","orgId","departId"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchUserInfo(result));
			} while (result.moveToNext());
		}

		return list;
	}
	
	
	public ArrayList<UserInfo> getUserInfo() {
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		String where = String.format("userId = '%s' " , mUserId);
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);
		
		Cursor result = getDb().query("UserInfo", new String[] {"userId", "userName", "sex",
				"phone", "picUrl","pinYin","initial","userType","orgId","departId"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchUserInfo(result));
			} while (result.moveToNext());
		}

		return list;
	}
	
	private UserInfo fetchUserInfo(Cursor result) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(result.getString(result.getColumnIndex("userId")));
		userInfo.setUserName(result.getString(result.getColumnIndex("userName")));
		userInfo.setSex(result.getInt(result.getColumnIndex("sex")));
		userInfo.setPhone(result.getString(result.getColumnIndex("phone")));
		userInfo.setPicUrl(result.getString(result.getColumnIndex("picUrl")));
		userInfo.setUserType(result.getString(result.getColumnIndex("userType")));
		userInfo.setOrgId(result.getString(result.getColumnIndex("orgId")));
		userInfo.setDepartId(result.getString(result.getColumnIndex("departId")));
		return userInfo;
	}
	
	public void addUserInfo(ArrayList<UserInfo> list) throws SQLException {
		for (UserInfo userInfo : list) {
			ContentValues values = new ContentValues();
			values.put("userId", userInfo.getUserId());
			values.put("userName", userInfo.getUserName());
			values.put("sex", userInfo.getSex());
			values.put("phone", userInfo.getPhone());
			values.put("userType", userInfo.getUserType());
			values.put("orgId", userInfo.getOrgId());
			values.put("departId", userInfo.getDepartId());
			
			values.put("pinYin", userInfo.getPinYin());
			values.put("initial", userInfo.getInitial());
			
			try {
				List<UserInfo> lh = this.getUserInfoExist(userInfo.getUserId());
				if(lh.size()==0){
					getDb().insertOrThrow("UserInfo", null, values);
				}
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
	
	
	public void deleteUserInfo() {
		getDb().delete("UserInfo", null, null);
	}
}