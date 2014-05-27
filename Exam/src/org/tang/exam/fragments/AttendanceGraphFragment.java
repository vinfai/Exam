package org.tang.exam.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity;
import org.tang.exam.activity.CameraActivity;
import org.tang.exam.adapter.AttendanceRecordGraphListAdapter;
import org.tang.exam.adapter.AttendanceRecordListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.AttendanceDBAdapter;
import org.tang.exam.db.AttendanceGraphDBAdapter;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.entity.AttendanceRecordGraph;
import org.tang.exam.fragments.AttendanceRecordListFragment.GPSLocation;
import org.tang.exam.rest.BaseResponse;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.attendance.SaveAttendanceRecordReq;
import org.tang.exam.rest.userInfo.ContactUserInfoResp;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.MobileConstant;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public class AttendanceGraphFragment extends Fragment  implements OnItemClickListener  {
	private static final String TAG = "AttendanceGraphFragment";
	private RelativeLayout layoutAttendance;
	private DropDownListView lvAttendanceRecordList;
	private ArrayList<AttendanceRecordGraph> mAttendanceRecordGraphList = new ArrayList<AttendanceRecordGraph>();
	private View mView;
	private AttendanceRecordGraph attendanceRecordGraph;
	private ProgressDialog progDialog = null;
	private String photoUrl;
	private AttendanceRecordGraphListAdapter mAdapter;
	
	public ProgressDialog getProgDialog() {
		return progDialog;
	}

	public void setProgDialog(ProgressDialog progDialog) {
		this.progDialog = progDialog;
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setTitle("title");
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("请稍等...");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.cancel();
			progDialog.dismiss();
		}
	}
	
	
	
	public static AttendanceGraphFragment newInstance() {
		AttendanceGraphFragment newFragment = new AttendanceGraphFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setHasOptionsMenu(true);
	}
	
	/**
	 * 实例化并渲染视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_attendance_record_list, container, false);
		return mView;
	}
	
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.attendance_camera, menu);
     }
	
	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         Log.d(TAG, "点击了菜单"+item.getTitle());
 		switch (item.getItemId()) {
 		case android.R.id.home:
 			getActivity().finish();
 			break;
 		case R.id.attendance_camera:
 			makeCamera();
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
		public void onStop() {
              super.onStop();
              progDialog.dismiss();
          }
		
		@Override
		public void onPause() {
			super.onPause();
			progDialog.dismiss();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			progDialog.dismiss();
		}

		private void initData() {
			progDialog = new ProgressDialog(getActivity());
			mAttendanceRecordGraphList.clear();
			lvAttendanceRecordList = (DropDownListView) mView.findViewById(R.id.lv_attendance_record_list);
			mAdapter = new AttendanceRecordGraphListAdapter(mView.getContext(), mAttendanceRecordGraphList);
			lvAttendanceRecordList.setAdapter(mAdapter);
			lvAttendanceRecordList.setOnItemClickListener(this);
			lvAttendanceRecordList.setOnDropDownListener(new OnDropDownListener() {
				@Override
				public void onDropDown() {
					Log.d(TAG, "下拉点击");
				}});
			
			initAttendanceRecordList();
		}

	private void initAttendanceRecordList() {
		AttendanceGraphDBAdapter dbAdapter = new AttendanceGraphDBAdapter();
		try {
			dbAdapter.open();
			mAttendanceRecordGraphList.addAll(dbAdapter.getAttendanceRecord());
			mAdapter.notifyDataSetChanged();
			lvAttendanceRecordList.setSecondPositionVisible();
			lvAttendanceRecordList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	 
	 public void makeCamera(){
		 Intent intent = new Intent(getActivity(),CameraActivity.class);
		 startActivityForResult(intent, 0);  
	 }
	 
	 
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)  
	    {  
	        switch (requestCode)  
	        {  
	        case 0:  
	            Bundle MarsBuddle = data.getExtras();  
	            photoUrl = MarsBuddle.getString("photoUrl");  
            	getGPSLocation(getActivity());
	            break;  
	        }  
	    }  
	 
	 
	 
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	
	private void saveAttendanceGraphLocal(ArrayList<AttendanceRecordGraph>  alist){
		AttendanceGraphDBAdapter dbAdapter = new AttendanceGraphDBAdapter();
		mAttendanceRecordGraphList.clear();
		try {
			dbAdapter.open();
			dbAdapter.addAttendanceRecordGraph(alist);
			mAttendanceRecordGraphList.addAll(dbAdapter.getAttendanceRecord());
			mAdapter.notifyDataSetChanged();
			lvAttendanceRecordList.setSecondPositionVisible();
			lvAttendanceRecordList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	
	
	
	
	/**
	 * 向服务器提交数据
	 * @param a
	 * @throws FileNotFoundException 
	 */
	private void saveAttendanceRecordToServer(final AttendanceRecordGraph a) throws FileNotFoundException{
		progDialog=ProgressDialog.show(getActivity(), "请稍等", "正在上传..",true);
		if(photoUrl==null || ("").equals(photoUrl)){
			  Toast.makeText(getActivity(), "照片路径为空", Toast.LENGTH_LONG).show();
		}
		else{
			RequestParams params = new RequestParams();
			params.put("id", a.getId());
			params.put("userId", a.getUserId());
			params.put("createTime", a.getCreateTime());
			params.put("address", a.getAddress());
			params.put("gps", a.getGps());
			params.put("latitude", a.getLatitude());
			params.put("longitude", a.getLongitude());
			params.put("photo", new File(photoUrl));
			
			AsyncHttpClient client = new AsyncHttpClient();
			
			client.post(AppConstant.BASE_URL + "mobile/addAttendanceGraph", 
					params, new AsyncHttpResponseHandler(){
			    
			    @Override
			    public void onFailure(Throwable error, String content) {
			        Toast.makeText(getActivity(), "上传失败！"+content, Toast.LENGTH_LONG).show();
			    }
			    
			    @Override
			    public void onSuccess(int statusCode, String content) {
			        
			        Gson gson = new Gson();  
			        
			        BaseResponse d =  gson.fromJson(content, BaseResponse.class);
			        if(d.getMsgFlag()==AppConstant.attendance_upload_success){
			        	 Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_LONG).show();
			        	 progDialog.cancel();
			        }
			        else{
			        	 Toast.makeText(getActivity(), "上传失败！", Toast.LENGTH_LONG).show();
			        }
			        ArrayList list =  new ArrayList();
			        a.setPhotoUrl(photoUrl);
			        list.add(a);
			        saveAttendanceGraphLocal(list);
			    }
			    
			});
			
		}
		
	}
	
	
	
	
	
	
	
	private void getGPSLocation(Context c){
		GPSLocation gps = new GPSLocation(c);
	}
	
	
	
	class GPSLocation  implements
		AMapLocationListener, Runnable{
	
		private LocationManagerProxy aMapLocManager = null;
		private AMapLocation aMapLocation;// 用于判断定位超时
		
		Handler handler = new Handler();
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
			if(attendanceRecordGraph!=null){
				try {
					saveAttendanceRecordToServer(attendanceRecordGraph);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			if (aMapLocation == null) {
				stopLocation();// 销毁掉定位
			}
		}
	
		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				attendanceRecordGraph = new AttendanceRecordGraph();
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
//				String str = (
//						 location.getProvince() + location.getCity()
//						+  location.getDistrict()  + location.getAdCode())+desc;
				
				String str = desc;
				
				
				String userId = UserCache.getInstance().getUserInfo().getUserId();
				attendanceRecordGraph.setAddress(str.trim());
				attendanceRecordGraph.setGps(location.getLatitude() + "|"
						+ location.getLongitude());
				attendanceRecordGraph.setLatitude(String.valueOf(location.getLatitude()));
				attendanceRecordGraph.setLongitude(String.valueOf(location.getLongitude()));
				attendanceRecordGraph.setId(UUID.randomUUID().toString());
				attendanceRecordGraph.setCreateTime(DateTimeUtil.getCompactTime());
				attendanceRecordGraph.setUserId(userId);
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
