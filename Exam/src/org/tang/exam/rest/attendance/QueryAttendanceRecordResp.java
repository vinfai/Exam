package org.tang.exam.rest.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.BaseResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryAttendanceRecordResp   extends BaseResponse {
	private ArrayList<AttendanceRecord> attendanceRecordList;

	public ArrayList<AttendanceRecord> getAttendanceRecordList() {
		return attendanceRecordList;
	}

	public void setAttendanceRecordList(ArrayList<AttendanceRecord> attendanceRecordList) {
		this.attendanceRecordList = attendanceRecordList;
	}

	public QueryAttendanceRecordResp(String jsonStr) throws JSONException {
		parseResponseData(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	private void parseResponseData(String jsonStr) throws JSONException {
		JSONObject rootObj = new JSONObject(jsonStr);
		if(rootObj.getString("sessionKey")!=null && !rootObj.getString("sessionKey").equals("")){
			this.msgFlag = rootObj.getInt("msgFlag");
			this.sessionKey =  rootObj.getString("sessionKey");
			if (rootObj.has("response")) {
				JSONArray respObj = rootObj.getJSONArray("response");
				Gson json = new Gson();
				attendanceRecordList = (ArrayList<AttendanceRecord>) json.fromJson(respObj.toString(),  
						new TypeToken<List<AttendanceRecord>>() {}.getType());
			}
		}
	}
	

}
