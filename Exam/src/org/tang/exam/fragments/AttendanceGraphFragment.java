package org.tang.exam.fragments;

import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public class AttendanceGraphFragment extends Fragment implements OnClickListener {
	private RelativeLayout layoutAttendance;
	private View mView;

	public static AttendanceGraphFragment newInstance() {
		AttendanceGraphFragment newFragment = new AttendanceGraphFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 实例化并渲染视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_index, container, false);
		initView();
		return mView;
	}
	
	/**
	 * 初始化默认界面Fragment--上的控件
	 */
	private void initView() {
		layoutAttendance = (RelativeLayout) mView.findViewById(R.id.layout_attendance);
		layoutAttendance.setOnClickListener(this);
	}
	
	/**
	 * 点击触发到其他的activity中去
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_attendance:
			onAttendanceClick();
			break;
		}
	}



	private void onScoreClick() {
//		Intent intent = new Intent(getActivity(), ScoreActivity.class);
//		startActivity(intent);
	}

	private void onAttendanceClick() {
		Intent intent = new Intent(getActivity(), AttendanceActivity.class);
		startActivity(intent);
	}

}
