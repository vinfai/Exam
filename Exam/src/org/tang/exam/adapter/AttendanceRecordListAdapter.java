package org.tang.exam.adapter;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.base.MyBaseAdatper;
import org.tang.exam.entity.AttendanceRecord;
import org.tang.exam.utils.DateTimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AttendanceRecordListAdapter extends MyBaseAdatper {
	private LayoutInflater mInflater;
	private ArrayList<AttendanceRecord> mNoticeList;

	public AttendanceRecordListAdapter(Context context, ArrayList<AttendanceRecord> noticeList) {
		super();
		mNoticeList = noticeList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mNoticeList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNoticeList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_attendance, null);

			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_attendance_create_time);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_attendance_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AttendanceRecord notice = mNoticeList.get(pos);
		if (notice != null) {
			holder.tvTime.setText(DateTimeUtil.toStandardTime(notice.getCreateTime()));
			holder.tvAddress.setText(notice.getAddress());
		}

		return convertView;
	}

	private final class ViewHolder {
		public TextView tvTime;
		public TextView tvAddress;
	}
}
