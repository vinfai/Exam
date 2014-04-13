package org.tang.exam.activity;

import org.tang.exam.R;
import org.tang.exam.base.BaseActivity;
import org.tang.exam.common.AppConfig;
import org.tang.exam.common.UserCache;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/**
 * 程序运行时，首先进入
 * @author Administrator
 *
 */
public class WelcomeActivity extends BaseActivity {
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int SPLASH_DELAY_MILLIS = 1000;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		
		/**
		 * 根据延时消息进行消息类型的判断：运行过的，进入登录界面，没有运行过的，进入指导页面
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		nextActivity();
	}
	
	private void nextActivity() {
		boolean firstRun = AppConfig.getInstance().getFirstRun();
		if (firstRun) {
			/**
			 * 用于线程内部消息循环
				主要是用作在将来定时做某个动作，或者循环性，周期性的做某个动作。主要的接口就是
				Handler.sendEmptyMessageDelayed(int msgid, long after);
				Handler.sendMessageDelayed(Message msg, long after);
				Handler.postDelayed(Runnable task, long after);
				Handler.sendMessageAtTime(Message msg, long timeMillis);
				Handler.sendEmptyMessageAtTime(int id, long timeMiilis);
				Handler.postAtTime(Runnable task, long timeMillis);
			 */
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
		AppConfig.getInstance().setFirstRun(false);
	}

	private void goHome() {
		boolean logon = UserCache.getInstance().isLogon();
		if (logon) {
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		} else {
			Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}
	}

	private void goGuide() {
		Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
		WelcomeActivity.this.startActivity(intent);
		WelcomeActivity.this.finish();
	}
}
