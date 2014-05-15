package org.tang.exam.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.R;
import org.tang.exam.adapter.AttendanceRecordListAdapter;
import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.AttendanceDBAdapter;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.fragments.AttendanceGraphFragment;
import org.tang.exam.fragments.AttendanceRecordListFragment;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.attendance.SaveAttendanceRecordReq;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.MobileConstant;
import org.tang.exam.utils.MyLocationManager;
import org.tang.exam.utils.MyLocationManager.LocationCallBack;
import org.tang.exam.view.DropDownListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;

public class AttendanceActivity extends BaseActionBarActivity {
	private static final String TAG = "AttendanceActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	private ProgressDialog prgDialog = null;
	private MyLocationManager mLocation;
	private DropDownListView lvAttendanceRecordList;
	private GpsDataChangeListener gpsDataChangeListener;
	 // Container Activity must implement this interface  
    public interface GpsDataChangeListener {  
        public void onGpsDataChangeListener(Context context,DropDownListView lvAttendanceRecordList);  
    }  
	
	
	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在定位...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);

		ActionBar bar = getSupportActionBar();
		
		//设置actionbar的导航模式  
		bar.setNavigationMode(bar.NAVIGATION_MODE_LIST);  
		 //声明一个SimpleAdapter独享，设置数据与对应关系
		         SimpleAdapter simpleAdapter = new SimpleAdapter(
		                 this, getData(), R.layout.fragment_attendance_menu,
		                 new String[] { "ivLogo", "applicationName" }, new int[] {
		                         R.id.attendace_menu_imageview, R.id.attendace_menu_textview });
		
		 bar.setListNavigationCallbacks(simpleAdapter,new DropDownListener());  
		

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("出勤记录"),
				AttendanceRecordListFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("出勤路线"),
				AttendanceGraphFragment.class, null);
	}
	
	
    public List<Map<String, Object>> getData() {
        //生成数据源
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ivLogo", R.drawable.abc_ic_go);
        map.put("applicationName", getString(R.string.attendance_note));
        list.add(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ivLogo", R.drawable.abc_ic_go);
        map2.put("applicationName", getString(R.string.attendance_share));
        list.add(map2);
        return list;
    }
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.notice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		case R.id.action_remote_attendance:
			showProgressDialog();
			onRemoteAttendance();
			break;
		}
		return true;
	}
	
	private void onRemoteAttendance() {
		LocationGpsOrNetWork lgn = new LocationGpsOrNetWork().newInstance();
		mLocation = MyLocationManager.getInstance();// 获取实例
		mLocation.init(this, lgn);
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
									MessageBox.showMessage(AttendanceActivity.this, "远程考勤成功");
									refreshAttendanceRecordList();
								}
								else{
									MessageBox.showMessage(AttendanceActivity.this, "服务器异常");
								}
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
	
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showMessage(AttendanceActivity.this, "服务器异常");
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	/**
	 * 刷新Fragment
	 */
	private void refreshAttendanceRecordList() {
		lvAttendanceRecordList = (DropDownListView) findViewById(R.id.lv_attendance_record_list);
//		gpsDataChangeListener = (AttendanceRecordListFragment)mTabsAdapter.getItem(0);
		gpsDataChangeListener.onGpsDataChangeListener(this,lvAttendanceRecordList);
	}
	
	/**
	 * 手机定位与数据处理
	 * @author Administrator
	 *
	 */
	class LocationGpsOrNetWork implements LocationCallBack {

		private LocationGpsOrNetWork instance;

		public LocationGpsOrNetWork newInstance() {
			if (null == instance) {
				instance = new LocationGpsOrNetWork();
			}
			return instance;
		}

		@Override
		public void onCurrentLocation(Location location) {
			String address = "";
			StringBuilder sb = new StringBuilder();

			try {
				if (location != null) {
					// 处理地理编码
					Geocoder gc = new Geocoder(AttendanceActivity.this,Locale.getDefault());

					List<Address> add = gc.getFromLocation(
							location.getLatitude(), location.getLongitude(), 1);
					StringBuilder bb = new StringBuilder();
					if (add.size() > 0) {
						Address ad = add.get(0);
						bb.append(ad.getAddressLine(0));
						bb.append(ad.getAddressLine(1));
						bb.append(ad.getAddressLine(2));
						sb.append(bb);

						address = sb.toString();

						if (address.length() > 0) {
							Log.d(TAG, "当前经度：" + location.getLongitude()
									+ "\n当前纬度：" + location.getLatitude()
									+ "地址为：：" + address);
							AttendanceRecord a = new AttendanceRecord();
							String userId = UserCache.getInstance().getUserInfo().getUserId();
							String userName = UserCache.getInstance().getUserInfo().getUserName();
							a.setAddress(address);
							a.setGps(location.getLatitude() + "|"
									+ location.getLongitude());
							a.setLatitude(String.valueOf(location.getLatitude()));
							a.setLongitude(String.valueOf(location.getLongitude()));
							a.setId(UUID.randomUUID().toString());
							a.setCreateTime(DateTimeUtil.getCompactTime());
							a.setUserId(userId);
							saveAttendanceRecordToServer(a);
						}
						else{
							MessageBox.showMessage(AttendanceActivity.this, "手机定位失败,请重试");
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "手机GPS定位错误：" + e);
				MessageBox.showMessage(AttendanceActivity.this, "手机GPS定位异常,请重试");
			} finally {
				mLocation.destoryLocationManager();
				closeProgressDialog();
			}

		}
	}
	
    /**
     * 实现 ActionBar.OnNavigationListener接口
     */
    class DropDownListener implements OnNavigationListener
    {
        /* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
        public boolean onNavigationItemSelected(int itemPosition, long itemId)
        {
        	Log.d(TAG, getData().get(itemPosition).get("applicationName").toString());
            return true;
        }
    }
}