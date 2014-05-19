package org.tang.exam.db;

import java.util.ArrayList;

import org.tang.exam.base.MyApplication;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.entity.AttendanceRecordGraph;
import org.tang.exam.entity.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AttendanceGraphDBAdapter extends DBAdapter {
	private static final String TAG = "AttendanceGraphDBAdapter";
	private int MAX_NUMBER = 20;
	private String mUserId="";
	
	public ArrayList<AttendanceRecordGraph> getAttendanceRecord() {
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		String where = String.format("userId = '%s' " , mUserId);
		ArrayList<AttendanceRecordGraph> list = new ArrayList<AttendanceRecordGraph>();
		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("AttendanceGraph", new String[] {"id", "userId", "createTime",
				"address", "gps","latitude","longitude","photoUrl"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchAttendanceRecordGraph(result));
			} while (result.moveToNext());
		}

		return list;
	}
	
	private AttendanceRecordGraph fetchAttendanceRecordGraph(Cursor result) {
		AttendanceRecordGraph attendanceRecord = new AttendanceRecordGraph();
		attendanceRecord.setId(result.getString(result.getColumnIndex("id")));
		attendanceRecord.setUserId(result.getString(result.getColumnIndex("userId")));
		attendanceRecord.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		attendanceRecord.setAddress(result.getString(result.getColumnIndex("address")));
		attendanceRecord.setGps(result.getString(result.getColumnIndex("gps")));
		attendanceRecord.setLatitude(result.getString(result.getColumnIndex("latitude")));
		attendanceRecord.setLongitude(result.getString(result.getColumnIndex("longitude")));
		attendanceRecord.setPhotoUrl(result.getString(result.getColumnIndex("photoUrl")));
		return attendanceRecord;
	}
	
	public void addAttendanceRecordGraph(ArrayList<AttendanceRecordGraph> list) throws SQLException {
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		for (AttendanceRecordGraph attendanceRecord : list) {
			ContentValues values = new ContentValues();
			values.put("id", attendanceRecord.getId());
			values.put("userId", mUserId);
			values.put("address", attendanceRecord.getAddress());
			values.put("gps", attendanceRecord.getGps());
			values.put("createTime", attendanceRecord.getCreateTime());
			values.put("latitude", attendanceRecord.getLatitude());
			values.put("longitude", attendanceRecord.getLongitude());
			values.put("photoUrl", attendanceRecord.getPhotoUrl());
			getDb().insertOrThrow("AttendanceGraph", null, values);
		}
	}
}