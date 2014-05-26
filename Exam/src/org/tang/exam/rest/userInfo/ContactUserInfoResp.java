package org.tang.exam.rest.userInfo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.entity.ClassInfo;
import org.tang.exam.entity.StudentRoster;
import org.tang.exam.entity.UserInfo;

public class ContactUserInfoResp {
	private int ack = 0;
	private ArrayList<UserInfo> userInfoList = new ArrayList<UserInfo>();

	public int getAck() {
		return ack;
	}

	public ArrayList<UserInfo> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(ArrayList<UserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public ContactUserInfoResp(String jsonStr) throws JSONException {
		parseUserInfo(jsonStr);
	}

	private void parseUserInfo(String jsonStr) throws JSONException {
		userInfoList.clear();
		JSONObject rootObj = new JSONObject(jsonStr);
		this.ack = rootObj.getInt("ack");

		if (rootObj.has("response")) {
			JSONObject respObj = rootObj.getJSONObject("response");
			JSONArray userArray = respObj.getJSONArray("userInfoList");
			for (int i = 0; i < userArray.length(); i++) {
				JSONObject userObj = userArray.getJSONObject(i);
				UserInfo student = new UserInfo();
				student.setUserId(userObj.getString("userId"));
				student.setUserName(userObj.getString("userName"));
				student.setSex(userObj.getInt("sex"));
				student.setPhone(userObj.getString("phone"));
				student.setPicUrl(userObj.getString("picUrl"));
				student.setOrgId(userObj.getString("orgId"));
				student.setDepartId(userObj.getString("departId"));
				this.userInfoList.add(student);
			}
		}
	}
}
