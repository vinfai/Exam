package org.tang.exam.entity;

import java.io.Serializable;

public class AttendanceRecord  implements Serializable {
	private static final long serialVersionUID = 2353219003803979291L;
	private String id;
	private String userId;
	private String createTime;
	private String address;
	private String gps;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
