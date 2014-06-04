package org.tang.exam.fragments;

import java.util.ArrayList;
import java.util.Collections;

import org.tang.exam.R;
import org.tang.exam.activity.ChatActivity;
import org.tang.exam.adapter.UserListAdapter;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.ChatMsgDBAdapter;
import org.tang.exam.db.UserInfoDBAdapter;
import org.tang.exam.entity.UserComparator;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.view.IndexableListView;

import android.content.Intent;
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
	private Bundle bundle ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_list, container, false);
		return mView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	
	private void initData() {
		mUserList.clear();
		initUserList();
		initUserUnReadMsgList();
		lvUserList = (IndexableListView) mView.findViewById(R.id.lv_user_list);
//		mAdapter = new UserListAdapter(mView.getContext(), mUserList,UserListAdapter.NORMAL_MODE);
		mAdapter = new UserListAdapter(mView.getContext(), mUserList,bundle,UserListAdapter.NORMAL_MODE);
		lvUserList.setAdapter(mAdapter);
		lvUserList.setFastScrollEnabled(true);
		lvUserList.setTextFilterEnabled(true);
		lvUserList.setOnItemClickListener(this);
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
	
	
	private void initUserUnReadMsgList() {
		ChatMsgDBAdapter dBAdapter = new ChatMsgDBAdapter();
		try {
			dBAdapter.open();
			ArrayList<String> userIds = new ArrayList<String>();
			for(UserInfo userInfo : mUserList){
				userIds.add(userInfo.getUserId());
			}
			bundle = dBAdapter.getAllPeopleChatMsgUnReadCount(userIds);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dBAdapter.close();
		}
	}
	
	
	private void updateUnReadMsgCount(String fromUserId,String toUserId){
		ChatMsgDBAdapter cDBAdapter = new ChatMsgDBAdapter();
		try {
			cDBAdapter.open();
			cDBAdapter.updateUnReadMsgCount(fromUserId,toUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			cDBAdapter.close();
		}
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		UserCache userCache = UserCache.getInstance();
		Log.d(TAG, "点击了"+pos+"View:::::"+v.getId());
		switch (parent.getId()) {
			case R.id.lv_user_list:
				Log.d(TAG, "点击了"+mUserList.get(pos).getUserName());
				Intent intent = new Intent(getActivity(), ChatActivity.class);
	            Bundle bundle = new Bundle();
	            bundle.putString("toUserId", mUserList.get(pos).getUserId());
	            bundle.putString("toUserName", mUserList.get(pos).getUserName());
	            bundle.putString("toUserPicUrl", mUserList.get(pos).getPicUrl());
	            intent.putExtra("tag",bundle);
	            
	            updateUnReadMsgCount(mUserList.get(pos).getUserId(),userCache.getUserInfo().getUserId());
	            getActivity().startActivity(intent);
			break;
		}
	}

	public static OrgPeopleFragment newInstance() {
		OrgPeopleFragment newFragment = new OrgPeopleFragment();
		return newFragment;
	}

	public void onUserFilter(String query) {
		// TODO Auto-generated method stub
		
	}
	
}
