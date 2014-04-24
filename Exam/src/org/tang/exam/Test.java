package org.tang.exam;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class Test {
	public static void main(String args[]) throws JSONException{
		String s = "{'response':[{'id':1,'gps':111,'createTime':20140420120001,'userName':'tang','address':'长沙','userId':'F00D8F6848F046DBE040007F0100468C'}],'sessionKey':'examTang','msgFlag':1002}";
		JSONObject rootObj = new JSONObject(s);
		Gson json = new Gson();
	
	}
}
