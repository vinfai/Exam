package org.tang.exam.fragments;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity;
import org.tang.exam.activity.MapActivity;
import org.tang.exam.adapter.AttendanceRecordListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.AttendanceDBAdapter;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.attendance.QueryAttendanceRecordReq;
import org.tang.exam.rest.attendance.QueryAttendanceRecordResp;
import org.tang.exam.rest.attendance.SaveAttendanceRecordReq;
import org.tang.exam.utils.AMapUtil;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.MobileConstant;
import org.tang.exam.utils.ToastUtil;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.Marker;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public  class AttendanceRecordListFragment extends
	Fragment implements OnItemClickListener 
	{
	private static final String TAG = "AttendanceRecordListFragment";
	private ArrayList<AttendanceRecord> mAttendanceRecordList = new ArrayList<AttendanceRecord>();
	private AttendanceRecordListAdapter mAdapter;
	private DropDownListView lvAttendanceRecordList;
	private View mView;
	private AttendanceRecord attendanceRecord;
	private ProgressDialog progDialog = null;
	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	public static AttendanceRecordListFragment newInstance() {
		AttendanceRecordListFragment newFragment = new AttendanceRecordListFragment();
		return newFragment;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_attendance_record_list, container, false);
		return mView;
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.attendance, menu);
     }
	
	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         Log.d(TAG, "点击了菜单"+item.getTitle());
 		switch (item.getItemId()) {
 		case android.R.id.home:
 			getActivity().finish();
 			break;
 		case R.id.action_remote_attendance:
 			getGPSLocation(getActivity());
 			break;
 		}
 		return true;
     }
	
	

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	

	
	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initData() {
		progDialog = new ProgressDialog(getActivity());
		mAttendanceRecordList.clear();
		lvAttendanceRecordList = (DropDownListView) mView.findViewById(R.id.lv_attendance_record_list);
		mAdapter = new AttendanceRecordListAdapter(mView.getContext(), mAttendanceRecordList);
		lvAttendanceRecordList.setAdapter(mAdapter);
		lvAttendanceRecordList.setOnItemClickListener(this);
		lvAttendanceRecordList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshAttendanceRecordList();
			}});
		
		initAttendanceRecordList();
	}

	private void initAttendanceRecordList() {
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			mAttendanceRecordList.addAll(dbAdapter.getAttendanceRecord());
			mAdapter.notifyDataSetChanged();
			lvAttendanceRecordList.setSecondPositionVisible();
			lvAttendanceRecordList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	public void refreshAttendanceRecordList() {
		UserCache userCache = UserCache.getInstance();
		QueryAttendanceRecordReq reqData = new QueryAttendanceRecordReq();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		
		String createTime = "0";
		
		if (mAttendanceRecordList.size() > 0) {
			createTime = mAttendanceRecordList.get(0).getCreateTime();
		}
		reqData.setCreateTime(createTime);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						String responseTmp = response.replace("\\", "").replace("\"", "");  
						checkResponse(responseTmp);
						lvAttendanceRecordList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvAttendanceRecordList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryAttendanceRecordResp respData = new QueryAttendanceRecordResp(response);
			if (respData.getMsgFlag()==AppConstant.attendance_success) {
				doSuccess(respData);
			} 
			else{
				MessageBox.showAckError(mView.getContext(), respData.getMsgFlag());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryAttendanceRecordResp respData) {
		mAttendanceRecordList.addAll(0, respData.getAttendanceRecordList());
		mAdapter.notifyDataSetChanged();
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addAttendanceRecord(respData.getAttendanceRecordList());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {
		case R.id.lv_attendance_record_list:
			Intent intent = new Intent(getActivity(), MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("AttendanceRecordList", mAttendanceRecordList);
            
            double latitude = Double.parseDouble(mAttendanceRecordList.get(pos==0?0:pos-1).getLatitude());
            double longitude = Double.parseDouble(mAttendanceRecordList.get(pos==0?0:pos-1).getLongitude());
            bundle.putInt("index", pos);
            bundle.putDouble("latitude", latitude);
            bundle.putDouble("longitude", longitude);
            intent.putExtra("tag",bundle);
            getActivity().startActivity(intent);
            break;
		}
	}

	
	private void getGPSLocation(Context c){
		showDialog();
		GPSLocation gps = new GPSLocation(c);
	}
	
	
	/**
	 * 向服务器提交数据
	 * @param a
	 */
	private void saveAttendanceRecordToServer(final AttendanceRecord a){
		SaveAttendanceRecordReq sreq = new SaveAttendanceRecordReq();
		sreq.setId(a.getId());
		sreq.setAddress(a.getAddress());
		sreq.setCreateTime(a.getCreateTime());
		sreq.setGps(a.getGps());
		sreq.setUserId(a.getUserId());
		sreq.setLatitude(a.getLatitude());
		sreq.setLongitude(a.getLongitude());
		
		MyStringRequest req = new MyStringRequest(Method.GET, sreq.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "添加远程考勤记录: " + response);
						String responseTmp = response.replace("\\", "").replace("\"", "");  
						JSONObject rootObj;
						try {
							rootObj = new JSONObject(responseTmp);
							if(rootObj.getString("sessionKey")!=null && !rootObj.getString("sessionKey").equals("")){
								if(MobileConstant.attendance_upload_success==rootObj.getInt("msgFlag")){
									ArrayList<AttendanceRecord> alist = new ArrayList<AttendanceRecord>();
									alist.add(a);
									addAttendanceRecordList(alist);
									mAttendanceRecordList.add(a);
									mAdapter.notifyDataSetChanged();
									lvAttendanceRecordList.onDropDownComplete();
									MessageBox.showMessage(getActivity(), "远程考勤成功");
									dismissDialog();
									
								}
								else{
									MessageBox.showMessage(getActivity(), "服务器异常");
								}
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
	
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showMessage(getActivity(), "服务器异常");
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	private void addAttendanceRecordList(
			ArrayList<AttendanceRecord> attendancelist) {
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			if (attendancelist != null && attendancelist.size() > 0) {
				dbAdapter.addAttendanceRecord(attendancelist);
			}

		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	
	class GPSLocation  implements
		AMapLocationListener, Runnable{
		
		private LocationManagerProxy aMapLocManager = null;
		private AMapLocation aMapLocation;// 用于判断定位超时
		private Handler handler = new Handler();
		
		public GPSLocation(Context c){
			aMapLocManager = LocationManagerProxy.getInstance(c);
			aMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
			handler.postDelayed(this, 4000);// 设置超过4秒还没有定位到就停止定位
		}
		
		
		@Override
		public void onLocationChanged(Location location) {
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		@Override
		public void run() {
			if(attendanceRecord!=null){
				saveAttendanceRecordToServer(attendanceRecord);
			}
			
			if (aMapLocation == null) {
				stopLocation();// 销毁掉定位
			}
		}

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				attendanceRecord = new AttendanceRecord();
				this.aMapLocation = location;// 判断超时机制
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				String cityCode = "";
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = (
						 location.getProvince() + location.getCity()
						+  location.getDistrict()  + location.getAdCode())+desc;
				String userId = UserCache.getInstance().getUserInfo().getUserId();
				attendanceRecord.setAddress(str);
				attendanceRecord.setGps(location.getLatitude() + "|"
						+ location.getLongitude());
				attendanceRecord.setLatitude(String.valueOf(location.getLatitude()));
				attendanceRecord.setLongitude(String.valueOf(location.getLongitude()));
				attendanceRecord.setId(UUID.randomUUID().toString());
				attendanceRecord.setCreateTime(DateTimeUtil.getCompactTime());
				attendanceRecord.setUserId(userId);
				stopLocation();
			}
			
		}
		
		/**
		 * 销毁定位
		 */
		private void stopLocation() {
			if (aMapLocManager != null) {
				aMapLocManager.removeUpdates(this);
				aMapLocManager.destory();
			}
			aMapLocManager = null;
		}
		
	}

}
