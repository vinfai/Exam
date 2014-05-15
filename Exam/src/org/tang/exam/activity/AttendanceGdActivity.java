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

public class AttendanceGdActivity extends BaseActionBarActivity {
	private static final String TAG = "AttendanceGdActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	private ProgressDialog prgDialog = null;
	
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