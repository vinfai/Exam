package org.tang.exam.rest.login;

import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.utils.JSONHelper;

import com.google.gson.Gson;

public class UserLoginParse {
	
	private int msgFlag;
	private String response;
	private int userType;
	private UserInfo userInfo;
	private String sessionKey;
	
	public UserLoginParse() {}
	
	public UserLoginParse(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if(rootObj.getString("sessionKey")!=null && !rootObj.getString("sessionKey").equals("")){
			this.msgFlag = rootObj.getInt("msgFlag");
			this.sessionKey =  rootObj.getString("sessionKey");
			if (rootObj.has("response")) {
				JSONObject respObj = rootObj.getJSONObject("response");
				Gson json = new Gson();
				userInfo = json.fromJson(respObj.toString(),  UserInfo.class);
			}
		}
	}
	
	public int getMsgFlag() {
		return msgFlag;
	}
	public void setMsgFlag(int msgFlag) {
		this.msgFlag = msgFlag;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
}
