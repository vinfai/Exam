package org.tang.exam.rest.attendance;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.tang.exam.common.AppConstant;
import org.tang.exam.entity.AttendanceRecord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryAttendanceRecordResp  {
	private int msgFlag;
	private String sessionKey;
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

	private ArrayList<AttendanceRecord> response;
	
	public ArrayList<AttendanceRecord> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<AttendanceRecord> response) {
		this.response = response;
	}

	public QueryAttendanceRecordResp(String jsonStr) throws JSONException {
		parseResponseData(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	private void parseResponseData(String jsonStr) throws JSONException {
		Gson json = new Gson();
		QueryAttendanceRecordResp d =  json.fromJson(jsonStr, QueryAttendanceRecordResp.class);

		if(d!=null && d.getMsgFlag()==AppConstant.attendance_success){
			this.msgFlag = d.getMsgFlag();
			this.sessionKey = d.getSessionKey();
			this.response = (ArrayList<AttendanceRecord>) json.fromJson(json.toJson(d.getResponse()),  
					new TypeToken<List<AttendanceRecord>>() {}.getType());
		}

	}
	

}
