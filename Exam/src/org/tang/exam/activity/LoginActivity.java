package org.tang.exam.activity;

import org.tang.exam.R;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.SyncRoster;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.login.UserLoginParse;
import org.tang.exam.rest.login.UserLoginReq;
import org.tang.exam.utils.MessageBox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class LoginActivity extends BaseActionBarActivity implements OnClickListener {
	private static final String TAG = "LoginActivity";
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

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在登录...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	private void initView() {
		tvForgotPwd = (TextView) findViewById(R.id.tv_forgot_password);
		tvForgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

		etUserId = (EditText) findViewById(R.id.et_user_id);
		etPassword = (EditText) findViewById(R.id.et_password);
		//对用户信息进行判断
		UserCache userCache = UserCache.getInstance();
		if (userCache.getUserInfo() != null) {
			etUserId.setText(userCache.getUserInfo().getUserName());
		}
		
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}
	
	//点击了登录按钮
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			onLogin(v);
			break;
		}
	}
	
	/**
	 * 通过网络发起用户登录请求
	 * @param v
	 */
	private void onLogin(View v) {
		UserLoginReq reqData = new UserLoginReq();
		reqData.setUserName(etUserId.getText().toString());
		reqData.setUserPwd(etPassword.getText().toString());

		showProgressDialog();
		UserCache.getInstance().setLogon(false);
		MyStringRequest req = new MyStringRequest(Method.POST, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						
						String responseTmp = response.replace("\\", "").replace("\"", "");  
						
						checkResponse(responseTmp);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeProgressDialog();
						MessageBox.showServerError(LoginActivity.this);
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			UserLoginParse respData = new UserLoginParse(response);
			if (respData.getMsgFlag() == AppConstant.login_success) {
				loginSuccess(respData);
			} else {
				closeProgressDialog();
				MessageBox.showAckError(LoginActivity.this, respData.getMsgFlag());
			}
		} catch (Exception e) {
			closeProgressDialog();
			MessageBox.showParserError(LoginActivity.this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}

	private void loginSuccess(UserLoginParse respData) {
		UserInfo userInfo = respData.getUserInfo();
		UserCache userCache = UserCache.getInstance();

		userCache.setSessionKey(respData.getSessionKey());
		// TBD: Encrypt password with MD5 or DES
		userCache.setPassword(etPassword.getText().toString());
		userCache.setUserInfo(userInfo);
		
		/**
		 * 花名册同步成功后才会进去主界面
		 */
		SyncRoster syncRoster = new SyncRoster(new SyncRoster.OnTaskListener() {

			@Override
			public void onSuccess() {
				closeProgressDialog();
				UserCache userCache = UserCache.getInstance();
				userCache.setLogon(true);
				
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}

			@Override
			public void onFailure() {
				closeProgressDialog();
				MessageBox.showLongMessage(LoginActivity.this, "同步通讯录失败!");
			}
		});
		syncRoster.execSync();

	}

}