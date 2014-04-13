package org.tang.exam.rest.login;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class UserLoginReq extends BaseRequest {
	private String userName = "";
	private String userPwd = "";
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/userLogin";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userName", this.userName);
		paramsHashMap.put("userPwd", this.userPwd);
		return paramsHashMap;
	}
}
