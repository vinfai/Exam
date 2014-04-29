package org.tang.exam.fragments;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.activity.AttendanceActivity.GpsDataChangeListener;
import org.tang.exam.adapter.AttendanceRecordListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.AttendanceDBAdapter;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.attendance.QueryAttendanceRecordReq;
import org.tang.exam.rest.attendance.QueryAttendanceRecordResp;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 默认界面 Fragment--进行布局
 * @author lenovo
 *
 */
public final class AttendanceRecordListFragment extends Fragment implements OnItemClickListener,GpsDataChangeListener  {
	private static final String TAG = "AttendanceRecordListFragment";
	private ArrayList<AttendanceRecord> mAttendanceRecordList = new ArrayList<AttendanceRecord>();
	private AttendanceRecordListAdapter mAdapter;
	private DropDownListView lvAttendanceRecordList;
	private View mView;


	public static AttendanceRecordListFragment newInstance() {
		AttendanceRecordListFragment newFragment = new AttendanceRecordListFragment();
		return newFragment;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_attendance_record_list, container, false);
		return mView;
	}
	
	

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initData() {
		mAttendanceRecordList.clear();
		lvAttendanceRecordList = (DropDownListView) mView.findViewById(R.id.lv_attendance_record_list);
		mAdapter = new AttendanceRecordListAdapter(mView.getContext(), mAttendanceRecordList);
		lvAttendanceRecordList.setAdapter(mAdapter);
		lvAttendanceRecordList.setOnItemClickListener(this);
		lvAttendanceRecordList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshAttendanceRecordList();
			}});
		
		initAttendanceRecordList();
	}

	private void initAttendanceRecordList() {
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			mAttendanceRecordList.addAll(dbAdapter.getAttendanceRecord());
			mAdapter.notifyDataSetChanged();
			lvAttendanceRecordList.setSecondPositionVisible();
			lvAttendanceRecordList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	public void refreshAttendanceRecordList() {
		UserCache userCache = UserCache.getInstance();
		QueryAttendanceRecordReq reqData = new QueryAttendanceRecordReq();
		reqData.setUserId(userCache.getUserInfo().getUserId());
		
		String createTime = "0";
		
		if (mAttendanceRecordList.size() > 0) {
			createTime = mAttendanceRecordList.get(0).getCreateTime();
		}
		reqData.setCreateTime(createTime);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						String responseTmp = response.replace("\\", "").replace("\"", "");  
						checkResponse(responseTmp);
						lvAttendanceRecordList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvAttendanceRecordList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}

	private void checkResponse(String response) {
		try {
			QueryAttendanceRecordResp respData = new QueryAttendanceRecordResp(response);
			if (respData.getMsgFlag()==AppConstant.attendance_success) {
				doSuccess(respData);
			} 
			else{
				MessageBox.showAckError(mView.getContext(), respData.getMsgFlag());
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryAttendanceRecordResp respData) {
		mAttendanceRecordList.addAll(0, respData.getAttendanceRecordList());
		mAdapter.notifyDataSetChanged();
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addAttendanceRecord(respData.getAttendanceRecordList());
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//		switch (parent.getId()) {
//		case R.id.lv_attendance_record_list:
//			Intent intent = new Intent(getActivity(), AttendanceRecordDetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("AttendanceRecordList", mAttendanceRecordList);
//            bundle.putInt("index", pos);
//            intent.putExtras(bundle);
//            getActivity().startActivity(intent);
//            break;
//		}
	}



	@Override
	public void onGpsDataChangeListener(Context context,DropDownListView lvAttendanceRecordList) {
		this.lvAttendanceRecordList = lvAttendanceRecordList ;
		mAttendanceRecordList.clear();
		mAdapter = new AttendanceRecordListAdapter(context, mAttendanceRecordList);
		AttendanceDBAdapter dbAdapter = new AttendanceDBAdapter();
		try {
			dbAdapter.open();
			mAttendanceRecordList.addAll(dbAdapter.getAttendanceRecord());
			lvAttendanceRecordList.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
			lvAttendanceRecordList.setOnItemClickListener(this);
			lvAttendanceRecordList.setOnDropDownListener(new OnDropDownListener() {
				@Override
				public void onDropDown() {
					Log.d(TAG, "下拉点击");
					refreshAttendanceRecordList();
				}});
			lvAttendanceRecordList.onDropDownComplete();
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	

}
