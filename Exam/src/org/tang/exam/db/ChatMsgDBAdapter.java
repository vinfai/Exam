package org.tang.exam.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.tang.exam.common.UserCache;
import org.tang.exam.entity.ChatMsgEntity;
import org.tang.exam.entity.UserInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;

public class ChatMsgDBAdapter extends DBAdapter {
	private static final String TAG = "ChatMsgDBAdapter";
	private int MAX_NUMBER = 20;
	
	public ArrayList<ChatMsgEntity> getChatMsgEntity(String fromUserId,String toUserId) {
		String where = String.format("fromUserId = '%s'  and  toUserId ='%s' " , fromUserId,toUserId);
		ArrayList<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();
		String orderBy = "createTime desc";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("ChatMsg", new String[] {"id", "fromUserId", "createTime","fromUserName","toUserName","msgtext", "toUserId", "msgType","msgState"},where, null, null, null, orderBy, limit);
		
		if(result!=null){
			if (result.moveToFirst()) {
				do {
					list.add(fetchChatMsgEntity(result));
				} while (result.moveToNext());
			}
		}
		return list;
	}
	
	
	public void updateUnReadMsgCount(String fromUserId,String toUserId) {
		String where = "";
			where = String.format("fromUserId = '%s' AND toUserId = '%s' AND msgState = 0", fromUserId,
					toUserId);

		ContentValues values = new ContentValues();
		values.put("msgState", "1");
		getDb().update("ChatMsg", values, where, null);
	}
	
	
	
	
	public Long getChatMsgUnReadCount() {
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		Cursor cursor = getDb().rawQuery("select count(*) from ChatMsg where msgState = '0' "
				+ " and toUserId=? ", new String[]{userInfo.getUserId()});  
		 if (cursor.moveToNext()) {  
	            return  cursor.getLong(0);  
	        }  
	     return 0L;  
	}
	
	
	
	public Bundle getAllPeopleChatMsgUnReadCount(ArrayList<String> userIds) {
		Bundle m = new Bundle();
		for(String userId :userIds){
			Cursor cursor = getDb().rawQuery("select count(*) from ChatMsg where msgState = '0' "
					+ " and fromUserId=? ", new String[]{userId});  
			 	if (cursor.moveToNext()) {  
		            Long tempnum =  cursor.getLong(0);  
		            m.putString(userId, String.valueOf(tempnum==0L?"":tempnum));
		        }  
		}
		return m;
	}
	
	
	
	
	private List<ChatMsgEntity> getChatMsgEntityExist(String fromUserId,String toUserId,String createTime) {
		String where = String.format(" fromUserId = '%s' and toUserId = '%s'  and createTime =  '%s' ", 
				fromUserId,toUserId,createTime);
		return getChatMsgEntityByWhere(where);
	}
	
	
	private ArrayList<ChatMsgEntity> getChatMsgEntityByWhere(String where) {
		ArrayList<ChatMsgEntity> list = new ArrayList<ChatMsgEntity>();

		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("ChatMsg", new String[] {"id", "fromUserId", "createTime","fromUserName","toUserName",
				"msgtext", "toUserId","msgType","msgState"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchChatMsgEntity(result));
			} while (result.moveToNext());
		}
		return list;
	}
	
	
	
	
	private ChatMsgEntity fetchChatMsgEntity(Cursor result) {
		ChatMsgEntity attendanceRecord = new ChatMsgEntity();
		attendanceRecord.setId(result.getString(result.getColumnIndex("id")));
		attendanceRecord.setFromUserId(result.getString(result.getColumnIndex("fromUserId")));
		attendanceRecord.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		attendanceRecord.setMsgText(result.getString(result.getColumnIndex("msgtext")));
		attendanceRecord.setToUserId(result.getString(result.getColumnIndex("toUserId")));
		attendanceRecord.setFromUserName(result.getString(result.getColumnIndex("fromUserName")));
		attendanceRecord.setToUserName(result.getString(result.getColumnIndex("toUserName")));
		attendanceRecord.setMsgState(result.getString(result.getColumnIndex("msgState")));
		attendanceRecord.setMsgType(result.getString(result.getColumnIndex("msgType")));
		return attendanceRecord;
	}
	
	public void addChatMsgEntity(ArrayList<ChatMsgEntity> list) throws SQLException {
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		for (ChatMsgEntity attendanceRecord : list) {
			ContentValues values = new ContentValues();
			values.put("id", UUID.randomUUID().toString());
			values.put("fromUserId", attendanceRecord.getFromUserId());
			values.put("toUserId", attendanceRecord.getToUserId());
			values.put("msgtext", attendanceRecord.getMsgText());
			values.put("createTime", attendanceRecord.getCreateTime());
			values.put("fromUserName", attendanceRecord.getFromUserName());
			values.put("toUserName", attendanceRecord.getToUserName());
			values.put("msgType", attendanceRecord.getMsgType());
			
			if(userInfo.getUserId().equals(attendanceRecord.getToUserId())){
				values.put("msgState", "0");
			}
			else{
				values.put("msgState", "1");
			}
			
			try {
				List<ChatMsgEntity> lh = this.getChatMsgEntityExist(attendanceRecord.getFromUserId(),attendanceRecord.getToUserId(),attendanceRecord.getCreateTime());
				if(lh.size()==0){
					getDb().insertOrThrow("ChatMsg", null, values);
				}
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
}