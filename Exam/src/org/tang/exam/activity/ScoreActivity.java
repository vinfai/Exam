package org.tang.exam.activity;

import org.tang.exam.R;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.common.UserCache;
import org.tang.exam.rest.RequestController;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ScoreActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "ScoreActivity";
	private ProgressDialog prgDialog = null;
	private TextView tvForgotPwd;
	private EditText etUserId;
	private EditText etPassword;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.login));
		initView();
	}


	private void initView() {
		tvForgotPwd = (TextView) findViewById(R.id.tv_forgot_password);
		tvForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		etUserId = (EditText) findViewById(R.id.et_user_id);
		etPassword = (EditText) findViewById(R.id.et_password);
		//对用户信息进行判断
		UserCache userCache = UserCache.getInstance();
		if (userCache.getUserInfo() != null) {
			etUserId.setText(userCache.getUserInfo().getUserId());
		}
		
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}
	
	//点击了登录按钮
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			break;
		}
	}

}