package org.tang.exam.rest.attendance;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class SaveAttendanceRecordReq extends BaseRequest {
	private String id;
	private String userId;
	private String createTime;
	private String address;
	private String gps;
	private String latitude;
	private String longitude;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/addAttendance";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("id", this.id);
		paramsHashMap.put("userId", this.userId);
		paramsHashMap.put("address", this.address);
		paramsHashMap.put("gps", this.gps);
		paramsHashMap.put("createTime", this.createTime);
		paramsHashMap.put("latitude", this.latitude);
		paramsHashMap.put("longitude", this.longitude);
		return paramsHashMap;
	}

}
