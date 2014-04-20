package org.tang.exam.activity;
import org.tang.exam.R;
import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.fragments.AttendanceGraphFragment;
import org.tang.exam.fragments.AttendanceRecordListFragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AttendanceActivity extends BaseActionBarActivity  {
	private static final String TAG = "AttendanceActivity";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;

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
		}
		return true;
	}
	

}