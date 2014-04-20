package org.tang.exam.rest.attendance;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class QueryAttendanceRecordReq extends BaseRequest {
	private String userId;
	private String createTime;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryAttendance";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("createTime", this.createTime);
		return paramsHashMap;
	}

}
