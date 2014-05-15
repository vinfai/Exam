package org.tang.exam.fragments;

import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity;
import org.tang.exam.activity.AttendanceGdActivity;
import org.tang.exam.activity.MapActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public class IndexFragment extends Fragment implements OnClickListener {
	private RelativeLayout layoutAttendance;
	private RelativeLayout layoutMap;
	private View mView;

	public static IndexFragment newInstance() {
		IndexFragment newFragment = new IndexFragment();
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
		layoutMap = (RelativeLayout) mView.findViewById(R.id.layout_map);
		layoutAttendance.setOnClickListener(this);
		layoutMap.setOnClickListener(this);
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
		case R.id.layout_map:
			onMapClick();
			break;
		}
	}



	private void onMapClick() {
		Intent intent = new Intent(getActivity(),MapActivity.class);
		startActivity(intent);
	}

	private void onAttendanceClick() {
		Intent intent = new Intent(getActivity(), AttendanceGdActivity.class);
		startActivity(intent);
	}

}
