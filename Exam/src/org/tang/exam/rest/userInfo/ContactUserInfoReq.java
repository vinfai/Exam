package org.tang.exam.rest.userInfo;

import java.util.HashMap;
import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class ContactUserInfoReq extends BaseRequest{

	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryContactUserInfo";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("orgId", this.orgId);
		return paramsHashMap;
	}

}
