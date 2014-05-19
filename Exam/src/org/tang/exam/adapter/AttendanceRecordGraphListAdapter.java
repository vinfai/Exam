package org.tang.exam.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.base.MyBaseAdatper;
import org.tang.exam.entity.AttendanceRecordGraph;
import org.tang.exam.utils.DateTimeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AttendanceRecordGraphListAdapter extends MyBaseAdatper {
	private LayoutInflater mInflater;
	private ArrayList<AttendanceRecordGraph> mNoticeList;

	public AttendanceRecordGraphListAdapter(Context context, ArrayList<AttendanceRecordGraph> noticeList) {
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
			convertView = mInflater.inflate(R.layout.item_attendance_graph, null);

			holder = new ViewHolder();
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_attendance_graph_create_time);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_attendance_graph_address);
			holder.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_attendance_graph_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AttendanceRecordGraph notice = mNoticeList.get(pos);
		if (notice != null) {
			holder.tvTime.setText(DateTimeUtil.toStandardTime(notice.getCreateTime()));
			holder.tvAddress.setText(notice.getAddress());
			Bitmap bitmap = getLoacalBitmap(notice.getPhotoUrl()); //从本地取图片(在cdcard中
			holder.ivPhoto.setImageBitmap(bitmap);
		}

		return convertView;
	}

	private final class ViewHolder {
		public TextView tvTime;
		public TextView tvAddress;
		public ImageView ivPhoto;
	}
	
	
    /**
    * 加载本地图片
    * @param url
    * @return
    */
    public static Bitmap getLoacalBitmap(String url) {
         try {
              FileInputStream fis = new FileInputStream(url);
              BitmapFactory.Options options = new BitmapFactory.Options();
              options.inPreferredConfig = Bitmap.Config.RGB_565;
              options.inPurgeable = true;
              options.inInputShareable = true;
              options.inJustDecodeBounds = false;
              options.inSampleSize = 1;   // width，hight设为原来的20分一
              Bitmap btp = BitmapFactory.decodeStream(fis, null, options);
              return btp;  ///把流转化为Bitmap图片        

           } catch (FileNotFoundException e) {
              e.printStackTrace();
              return null;
         }
    }
}
