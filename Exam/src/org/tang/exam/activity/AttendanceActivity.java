package org.tang.exam.activity;
import java.util.List;
import java.util.Locale;

import org.tang.exam.R;
import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.fragments.AttendanceGraphFragment;
import org.tang.exam.fragments.AttendanceRecordListFragment;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.MyLocationManager;
import org.tang.exam.utils.MyLocationManager.LocationCallBack;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AttendanceActivity extends BaseActionBarActivity  {
	private static final String TAG = "AttendanceActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	private MyLocationManager mLocation;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.notice));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("出勤记录"), AttendanceRecordListFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("出勤路线"), AttendanceGraphFragment.class, null);
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
			this.onRemoteAttendance();
			break;
		}
		return true;
	}
	
	
	
	private void onRemoteAttendance() {
		LocationGpsOrNetWork lgn = new LocationGpsOrNetWork().newInstance();
		 MyLocationManager.init(getApplicationContext(),  
				 lgn);//初始化  
	        mLocation = MyLocationManager.getInstance();//获取实例  
	}
	
	
	class LocationGpsOrNetWork implements  LocationCallBack{
		
		private LocationGpsOrNetWork instance;
		public LocationGpsOrNetWork newInstance() {
	        if (null == instance) {  
	            instance = new LocationGpsOrNetWork();  
	        }  
	        return instance;  
		}

		@Override
		public void onCurrentLocation(Location location) {
			String address = "没有找到地址";
			  StringBuilder sb=new StringBuilder();
			 if (location != null) {  
				 	//处理地理编码
				 	  Geocoder gc=new Geocoder(getApplicationContext(),Locale.getDefault());
				 	  try{
				 	   List<Address>add=gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				 	   StringBuilder bb=new StringBuilder();
				 	   if(add.size()>0)
				 	   {
				 	    Address ad=add.get(0);
				 	    bb.append(ad.getAddressLine(0)).append("\n");
				 	          bb.append(ad.getAddressLine(1)).append("\n");
				 	           bb.append(ad.getAddressLine(2)).append("\n");  
				 	           sb.append(bb);
				 	   }
				 	  }catch(Exception e){}
				 	  
				 	  address=sb.toString();
				 	
		            // 显示定位结果  
				 	MessageBox.showMessage(getApplicationContext(), "当前经度：" + location.getLongitude() + "\n当前纬度："  
		                    + location.getLatitude()+"地址为：："+address);
				 	
					Log.d(TAG, "当前经度：" + location.getLongitude() + "\n当前纬度："  
		                    + location.getLatitude()+"地址为：："+address);
					
					if(location!=null){
						mLocation.destoryLocationManager();
						mLocation = null;
					}
					
					
		        }  
		}
	}
	

}