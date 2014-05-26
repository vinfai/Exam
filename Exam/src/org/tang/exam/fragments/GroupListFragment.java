package org.tang.exam.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class GroupListFragment  extends Fragment implements OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public static GroupListFragment newInstance() {
		GroupListFragment newGroupListFragment = new  GroupListFragment();
		return newGroupListFragment;
	}

}
