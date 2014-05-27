package org.tang.exam.fragments;

import java.util.ArrayList;
import java.util.Collections;
import org.tang.exam.R;
import org.tang.exam.adapter.UserListAdapter;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.UserInfoDBAdapter;
import org.tang.exam.entity.UserComparator;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.view.IndexableListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OrgPeopleFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "OrgPeopleFragment";
	private View mView;
	private ArrayList<UserInfo> mUserList = new ArrayList<UserInfo>();
	private IndexableListView lvUserList;
	private UserListAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_list, container, false);
		initData();
		return mView;
	}
	
	private void initData() {
		mUserList.clear();
		lvUserList = (IndexableListView) mView.findViewById(R.id.lv_user_list);
		mAdapter = new UserListAdapter(mView.getContext(), mUserList,UserListAdapter.NORMAL_MODE);
		lvUserList.setAdapter(mAdapter);
		lvUserList.setFastScrollEnabled(true);
		lvUserList.setTextFilterEnabled(true);
		lvUserList.setOnItemClickListener(this);
		initUserList();
	}

	private void initUserList() {
		UserInfoDBAdapter userInfoDBAdapter = new UserInfoDBAdapter();
		UserCache userCache = UserCache.getInstance();

		try {
			userInfoDBAdapter.open();
			mUserList.clear();
			
			mUserList.addAll(userInfoDBAdapter.getUserInfoInOrg(userCache.getUserInfo().getOrgId()));
			
			Collections.sort(mUserList, new UserComparator());
			
			mAdapter.updateListData(mUserList);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			userInfoDBAdapter.close();
		}
	}
	
	
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
