package org.tang.exam.rest.userInfo;

import java.util.ArrayList;
import org.tang.exam.entity.UserInfo;

public class ContactUserInfoResp {
	private int msgFlag;
	private ArrayList<UserInfo> response;
	private int userType;
	private String sessionKey;
	
	public int getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(int msgFlag) {
		this.msgFlag = msgFlag;
	}

	public ArrayList<UserInfo> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<UserInfo> response) {
		this.response = response;
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
