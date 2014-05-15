package org.tang.exam.activity;

import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.fragments.AttendanceGraphFragment;
import org.tang.exam.fragments.AttendanceRecordListFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;

public class AttendanceGdActivity extends BaseActionBarActivity {
	private static final String TAG = "AttendanceGdActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);

		ActionBar bar = getSupportActionBar();
		//设置actionbar的导航模式  
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayHomeAsUpEnabled(true);
		 //声明一个SimpleAdapter独享，设置数据与对应关系
//		         SimpleAdapter simpleAdapter = new SimpleAdapter(
//		                 this, getData(), R.layout.fragment_attendance_menu,
//		                 new String[] { "ivLogo", "applicationName" }, new int[] {
//		                         R.id.attendace_menu_imageview, R.id.attendace_menu_textview });
//		
//		 bar.setListNavigationCallbacks(simpleAdapter,new DropDownListener());  
		

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("出勤记录"),
				AttendanceRecordListFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("出勤路线"),
				AttendanceGraphFragment.class, null);
	}
	
	
//    public List<Map<String, Object>> getData() {
//        //生成数据源
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        //每个Map结构为一条数据，key与Adapter中定义的String数组中定义的一一对应。
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("ivLogo", R.drawable.abc_ic_go);
//        map.put("applicationName", getString(R.string.attendance_note));
//        list.add(map);
//        Map<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("ivLogo", R.drawable.abc_ic_go);
//        map2.put("applicationName", getString(R.string.attendance_share));
//        list.add(map2);
//        return list;
//    }
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	
    /**
     * 实现 ActionBar.OnNavigationListener接口
     */
//    class DropDownListener implements OnNavigationListener
//    {
//        /* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
//        public boolean onNavigationItemSelected(int itemPosition, long itemId)
//        {
//        	Log.d(TAG, getData().get(itemPosition).get("applicationName").toString());
//            return true;
//        }
//    }
}