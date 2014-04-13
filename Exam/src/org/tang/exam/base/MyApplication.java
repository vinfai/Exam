package org.tang.exam.base;

public class MyApplication extends BaseApplication {
	private static MyApplication mInstance = null;

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
}
