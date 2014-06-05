package org.tang.exam.common;

import java.util.ArrayList;

import org.tang.exam.common.AppConstant.Ack;
import org.tang.exam.db.UserInfoDBAdapter;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.userInfo.ContactUserInfoReq;
import org.tang.exam.rest.userInfo.ContactUserInfoResp;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 同步花名册--根据用户信息来判断是否是同步学生还是家长还是老师
 * @author lenovo
 *
 */
public class SyncRoster {
	private static final String TAG = "SyncMyRoster";
	private OnTaskListener taskListener = null;

	public interface OnTaskListener {
		public void onSuccess();

		public void onFailure();
	}

	public SyncRoster(OnTaskListener taskListener) {
		this.taskListener = taskListener;
	}

	public void execSync() {
		UserInfoDBAdapter dbAdapter = new UserInfoDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.deleteUserInfo();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
			taskListener.onFailure();
			return;
		} finally {
			dbAdapter.close();
		}

		syncUserInfo_OrgPeople();
	}

	private void syncUserInfo_OrgPeople() {
		UserCache userCache = UserCache.getInstance();
		ContactUserInfoReq reqData = new ContactUserInfoReq();
		reqData.setOrgId(userCache.getUserInfo().getOrgId());

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Response: " + response);
						try {
							UserCache userCache = UserCache.getInstance();
							 Gson gson = new Gson();  
							 ContactUserInfoResp d =  gson.fromJson(response, ContactUserInfoResp.class);
							
							if (d.getMsgFlag()==AppConstant.contact_query_fail) {
								taskListener.onFailure();
								return;
							}

							if (d.getMsgFlag() == AppConstant.contact_query_success) {
								 ArrayList<UserInfo> ulist  = gson.fromJson(gson.toJson(d.getResponse()),  new TypeToken<ArrayList<UserInfo>>(){}.getType());
								updateUserInfo(ulist);
							}
							taskListener.onSuccess();
						} catch (Exception e) {
							taskListener.onFailure();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						taskListener.onFailure();
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	

	private void updateUserInfo(ArrayList<UserInfo> list) {
		Log.d("[SyncRoster] updateUserInfo: ", String.valueOf(list.size()));
		for (UserInfo roster : list) {
			Log.d("[user: ]", roster.getUserName());
			Log.d("[user: ]", roster.getPicUrl());
		}

		UserInfoDBAdapter dbAdapter = new UserInfoDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addUserInfo(list);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			dbAdapter.close();
		}
	}


}
