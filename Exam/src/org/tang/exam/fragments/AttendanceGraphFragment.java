package org.tang.exam.fragments;

import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity;
import org.tang.exam.view.DropDownListView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public class AttendanceGraphFragment extends Fragment  implements OnItemClickListener  {
	private RelativeLayout layoutAttendance;
	private DropDownListView lvAttendanceRecordList;
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
		mView = inflater.inflate(R.layout.fragment_attendance_record_list, container, false);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	

}
