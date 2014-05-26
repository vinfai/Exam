package org.tang.exam.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OrgPeopleFragment  extends Fragment implements OnItemClickListener{

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	public static OrgPeopleFragment newInstance() {
		OrgPeopleFragment newFragment = new OrgPeopleFragment();
		return newFragment;
	}

	public void onUserFilter(String query) {
		// TODO Auto-generated method stub
		
	}
	
}
