package org.tang.exam.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.tang.exam.R;
import org.tang.exam.adapter.CommonPagerAdapter;
import org.tang.exam.base.BaseActionBarFragmentActivity;
import org.tang.exam.fragments.IndexFragment;
import org.tang.exam.rest.RequestController;
import org.tang.exam.utils.PushUtils;
import org.tang.exam.view.BadgeView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class MainActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private BadgeView mBadge = null;
	private TextView mMessageItem = null;
	private RelativeLayout mIndexView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabWidget();
		initPagerView();
		initPushService();
	}

	private void initPushService() {
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 这里把apikey存放于manifest文件中，只是一种存放方式，
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this, "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
		if (!PushUtils.hasBind(getApplicationContext())) {
			Log.d("YYY", "before start work at " + Calendar.getInstance().getTimeInMillis());
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, 
					PushUtils.getMetaValue(MainActivity.this, "api_key"));
			Log.d("YYY", "after start work at " + Calendar.getInstance().getTimeInMillis());
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
//			PushManager.enableLbs(getApplicationContext());
//			Log.d("YYY", "after enableLbs at " + Calendar.getInstance().getTimeInMillis());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		String action = intent.getAction();
		if (PushUtils.ACTION_LOGIN.equals(action)) {
			// Push: 百度账号初始化，用access token绑定
			String accessToken = intent
					.getStringExtra(PushUtils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
		}
			
		
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		PushUtils.setLogText(getApplicationContext(), PushUtils.logStringCache);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_exit:
			this.finish();
			break;
		}
		return true;
	}

	private void initTabWidget() {
		mIndexView = (RelativeLayout) findViewById(R.id.bottombar_index);
		
		mMessageItem = (TextView) findViewById(R.id.tab_item_message);
		mBadge = new BadgeView(this, mMessageItem);
		mBadge.setBadgeMargin(0);
		mIndexView.setOnClickListener(new MyOnClickListener(0));
	}

	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_container);
		mFragmentsList = new ArrayList<Fragment>();
		Fragment indexFrag = IndexFragment.newInstance();
		mFragmentsList.add(indexFrag);
		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(0);
	}

	private class MyOnClickListener implements OnClickListener {
		private int mIndex = 0;

		public MyOnClickListener(int index) {
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			setCurrentPage(mIndex);
		}
	}

	private void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);
		switch (index) {
		case 0:
			selectIndex();
			break;
		}
	}

	private void selectIndex() {
		mIndexView.setSelected(true);
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentPage(arg0);
	}
	
	
	
	final class RefreshUnreadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "[RefreshUnreadReceiver] Receive intent: \r\n" + intent);
//			int unreadCount = intent.getIntExtra(PushUtils.EXTRA_MESSAGE, 0);
			String unreadCount = intent.getStringExtra(PushUtils.EXTRA_MESSAGE);
			
			if (mBadge != null) {
				if (unreadCount!="") {
					mBadge.setText(String.valueOf(unreadCount));
					mBadge.show();
				} else {
					mBadge.hide();
				}
			}
		}
	}

}