package org.tang.exam.adapter;

import java.util.List;

import org.tang.exam.R;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.ChatMsgEntity;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.ImageCacheManager;
import org.tang.exam.utils.DateTimeUtil;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMsgViewAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatMsgEntity> coll;

	private Context ctx;

	private LayoutInflater mInflater;
	
	private String toUserPicUrl="";
	private String fromUserPicUrl="";
	
//	private MediaPlayer mMediaPlayer = new MediaPlayer();

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}
	
	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll,String toUserPicUrl,String fromUserPicUrl) {
		this(context,coll);
		this.toUserPicUrl = toUserPicUrl;
		this.fromUserPicUrl = fromUserPicUrl;		
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ChatMsgEntity entity = coll.get(position);
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		if (entity.getFromUserId().equals(userInfo.getUserId())) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMsgEntity entity = coll.get(position);
		boolean isComMsg = ("1").equals(entity.getMsgType());
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		ViewHolder viewHolder = null;
		Bitmap bm = null;
		if (convertView == null) {
			
			viewHolder = new ViewHolder();
			
			if (entity.getFromUserId().equals(userInfo.getUserId())) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_text_right, null);
			}
			
			
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.ivHeader = (ImageView) convertView
					.findViewById(R.id.iv_userhead);
			
			viewHolder.isComMsg = isComMsg;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		

		
//		Drawable db = new BitmapDrawable(bm);
//		viewHolder.ivHeader.setBackgroundDrawable(db);
		ImageListener listener = ImageLoader.getImageListener(viewHolder.ivHeader,
				R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
		if (entity.getFromUserId().equals(userInfo.getUserId())) {
			 bm = ImageCacheManager.getInstance().getImageLoader().get(userInfo.getPicUrl(), listener).getBitmap();
		} else {
			 bm = ImageCacheManager.getInstance().getImageLoader().get(toUserPicUrl, listener).getBitmap();
		}
		viewHolder.tvSendTime.setText(DateTimeUtil.toStandardTime(entity.getCreateTime()));
		viewHolder.tvContent.setText(entity.getMsgText());			
		viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		viewHolder.tvTime.setText("");
		viewHolder.ivHeader.setImageBitmap(bm);
//		if (entity.getMsgText().contains(".amr")) {
//			viewHolder.tvContent.setText("");
//			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
//			viewHolder.tvTime.setText(entity.getCreateTime());
//		} else {
//			viewHolder.tvContent.setText(entity.getMsgText());			
//			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//			viewHolder.tvTime.setText("");
//		}
//		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				if (entity.getMsgText().contains(".amr")) {
//					playMusic(android.os.Environment.getExternalStorageDirectory()+"/"+entity.getMsgText()) ;
//				}
//			}
//		});
		viewHolder.tvUserName.setText(entity.getFromUserName());
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public ImageView ivHeader;
		public boolean isComMsg = true;
	}

	/**
	 * @Description
	 * @param name
	 */
//	private void playMusic(String name) {
//		try {
//			if (mMediaPlayer.isPlaying()) {
//				mMediaPlayer.stop();
//			}
//			mMediaPlayer.reset();
//			mMediaPlayer.setDataSource(name);
//			mMediaPlayer.prepare();
//			mMediaPlayer.start();
//			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
//				public void onCompletion(MediaPlayer mp) {
//
//				}
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	private void stop() {

	}

}
