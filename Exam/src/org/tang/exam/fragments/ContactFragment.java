package org.tang.exam.fragments;

import org.tang.exam.R;
import org.tang.exam.utils.FragmentSwitcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener {
	private OrgPeopleFragment mOrgPeopleFrag;
	private GroupListFragment mGroupFrag;
	private Fragment mCurrentFrag;
	private RadioGroup mRadioGroup;
	private MenuItem mSearchItem;
	private View mView;

	public static ContactFragment newInstance() {
		ContactFragment newFragment = new ContactFragment();
		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_contact, container, false);
		initView();
		return mView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.contact, menu);
		mSearchItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
		searchView.setOnQueryTextListener(this);
	}

	private void initView() {
		mOrgPeopleFrag = OrgPeopleFragment.newInstance();
		mGroupFrag = GroupListFragment.newInstance();

		mRadioGroup = (RadioGroup) mView.findViewById(R.id.group_contact);
		mRadioGroup.setOnCheckedChangeListener(listener);

		getChildFragmentManager().beginTransaction().add(R.id.fragment_container, mOrgPeopleFrag).commit();
		mCurrentFrag = mOrgPeopleFrag;
	}

	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (group.getCheckedRadioButtonId()) {
			case R.id.rb_orgpeople:
				mSearchItem.setVisible(true);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mOrgPeopleFrag);
				mCurrentFrag = mOrgPeopleFrag;
				break;
			case R.id.rb_group:
				mSearchItem.setVisible(false);
				FragmentSwitcher.switchFragment(getChildFragmentManager(), mCurrentFrag, mGroupFrag);
				mCurrentFrag = mGroupFrag;
				break;
			}
		}
	};

	@Override
	public boolean onQueryTextChange(String query) {
		if (mOrgPeopleFrag.isAdded()) {
			mOrgPeopleFrag.onUserFilter(query);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
}