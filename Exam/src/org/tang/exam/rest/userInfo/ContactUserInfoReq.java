package org.tang.exam.rest.userInfo;

import java.util.HashMap;
import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class ContactUserInfoReq extends BaseRequest{

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryContactUserInfo";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		return paramsHashMap;
	}

}
