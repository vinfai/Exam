package org.tang.exam.rest;


public class BaseResponse {
	protected int msgFlag;
	protected String sessionKey;
	
    public int getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(int msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getSessionKey() {
		return sessionKey;
	}



	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

}
