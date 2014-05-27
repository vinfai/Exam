package org.tang.exam.adapter;

import java.util.ArrayList;

import org.tang.exam.entity.UserInfo;

import android.text.TextUtils;
import android.widget.Filter;

public class UserFilter extends Filter {
	private ArrayList<UserInfo> mFilterList;
	private ArrayList<UserInfo> mOriginalList;
	private UserListAdapter mAdapter;

	public UserFilter(ArrayList<UserInfo> originalList, ArrayList<UserInfo> filterList,
			UserListAdapter adapter) {
		mOriginalList = originalList;
		mFilterList = filterList;
		mAdapter = adapter;
	}

	@Override
	protected FilterResults performFiltering(CharSequence constraint) {
		FilterResults results = new FilterResults();
		mFilterList.clear();

		if (TextUtils.isEmpty(constraint)) {
			mFilterList.addAll(mOriginalList);
		} else {
			String searchText = constraint.toString().trim().toUpperCase();
			for (UserInfo userInfo : mOriginalList) {
				if (userInfo.getUserName().toUpperCase().contains(searchText)
						|| userInfo.getPinYin().toUpperCase().contains(searchText)) {
					mFilterList.add(userInfo);
				} 
			}
		}

		results.values = mFilterList;
		results.count = mFilterList.size();
		return results;
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results) {
		mAdapter.notifyDataSetChanged();
	}
}
