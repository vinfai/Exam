package org.tang.exam.activity;

import java.util.ArrayList;
import java.util.Calendar;
import org.tang.exam.R;
import org.tang.exam.adapter.CommonPagerAdapter;
import org.tang.exam.base.BaseActionBarFragmentActivity;
import org.tang.exam.base.MyApplication;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.fragments.ContactFragment;
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
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private BadgeView mBadge = null;
	private TextView mMessageItem = null;
	private RelativeLayout mIndexView = null;
	private RelativeLayout mContactView = null;
	private String pushUserId;
	private String pushChannelId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabWidget();
		initPagerView();
		initPushService();
		handleIntent(getIntent());
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
		if(intent!=null && intent.getExtras()!=null && intent.getExtras().getSerializable(PushUtils.EXTRA_MESSAGE)!=null){
			Log.d(TAG, (String) intent.getExtras().getSerializable(PushUtils.EXTRA_MESSAGE));
			String message = (String) intent.getExtras().getSerializable(PushUtils.EXTRA_MESSAGE);
			
			 pushUserId = (String)intent.getExtras().getSerializable("pushUserId"); 
			 pushChannelId = intent.getStringExtra("pushChannelId");
			 handleIntent(intent);
			 
			if (mBadge != null) {
				if (message!=null && !message.equals("")) {
					mBadge.setText(String.valueOf(1));
					mBadge.show();
				} else {
					mBadge.hide();
				}
			}
		}
		
		if (PushUtils.ACTION_LOGIN.equals(action)) {
			// Push: 百度账号初始化，用access token绑定
			String accessToken = intent
					.getStringExtra(PushUtils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
		}
		
	}
	
	
	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		UserCache userCache = UserCache.getInstance();
		if (!pushUserId.equals("")&&pushUserId!=null) {
			onBindSuccess(pushUserId, pushChannelId);
		} else if (PushUtils.ACTION_NOTIFICATION_ENTRY.equals(action)) {
			// 重置通知栏新消息数
			MyApplication.getInstance().clearNewsCount();
		}
	}

	
	private void onBindSuccess(String pushUserId, String pushChannelId) {
		Log.d(TAG, "[onBindSuccess] pushUserId: " + pushUserId);
		Log.d(TAG, "[onBindSuccess] pushChannelId: " + pushChannelId);
		
		RequestParams params = new RequestParams();
		params.put("userId", UserCache.getInstance().getUserInfo().getUserId());
		params.put("pushUserId", pushUserId);
		params.put("pushChannelId", pushChannelId);
		params.put("deviceType", "android");
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.post(AppConstant.BASE_URL + "mobile/addPushInfo", 
				params, new AsyncHttpResponseHandler(){
		    
		    @Override
		    public void onFailure(Throwable error, String content) {
		        super.onFailure(error, content);
		        Toast.makeText(MainActivity.this, "上传推送信息失败！"+content, Toast.LENGTH_LONG).show();
		    }
		    
		    @Override
		    public void onSuccess(int statusCode, String content) {
		        super.onSuccess(statusCode, content);
		    }
		});
		
		
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
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
		mContactView = (RelativeLayout) findViewById(R.id.bottombar_contact);
		
		mMessageItem = (TextView) findViewById(R.id.tab_item_index);
		mBadge = new BadgeView(this, mMessageItem);
		mBadge.setBadgeMargin(0);
		mIndexView.setOnClickListener(new TabOnClickListener(0));
		mContactView.setOnClickListener(new TabOnClickListener(1));
	}

	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_container);
		mFragmentsList = new ArrayList<Fragment>();
		Fragment indexFrag = IndexFragment.newInstance();
		Fragment contactFrag = ContactFragment.newInstance();
		
		mFragmentsList.add(indexFrag);
		mFragmentsList.add(contactFrag);
		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(0);
	}

	final class TabOnClickListener implements OnClickListener {
		private int mIndex = 0;

		public TabOnClickListener(int index) {
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
		case 1:
			selectContact();
			break;
		}
	}

	private void selectIndex() {
		mIndexView.setSelected(true);
	}
	
	private void selectContact() {
		mContactView.setSelected(true);
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
					Log.d(TAG, unreadCount);
					mBadge.setText(String.valueOf(1));
					mBadge.show();
				} else {
					mBadge.hide();
				}
			}
		}
	}

}