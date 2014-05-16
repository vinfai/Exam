package org.tang.exam.activity;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;

import org.tang.exam.R;
import org.tang.exam.fragments.AttendanceGraphFragment;
import org.tang.exam.utils.CameraPreview;
import org.tang.exam.utils.DateTimeUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;

public class CameraActivity extends Activity {
	 protected static final String TAG = "CameraActivity";
	    private Camera mCamera;
	    private CameraPreview mPreview;
	    private String photoUrl;
	    
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_camera);

	        mCamera = getCameraInstance();

	        // 创建预览类，并与Camera关联，最后添加到界面布局中
	        mPreview = new CameraPreview(this, mCamera);
	        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mPreview);


	        Button captureButton = (Button) findViewById(R.id.button_capture);
	        captureButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                // 在捕获图片前进行自动对焦
	                mCamera.autoFocus(new AutoFocusCallback() {
	                    
	                    @Override
	                    public void onAutoFocus(boolean success, Camera camera) {
	                        // 从Camera捕获图片
	                        mCamera.takePicture(null, null, mPicture);
	                    }
	                });         
	                
	                Intent intent = new Intent(CameraActivity.this, AttendanceGraphFragment.class);  
	                intent.putExtra("photoUrl", photoUrl);  
	                setResult(RESULT_OK, intent);  
	                finish();  
	            }
	        });
	    }

	    /** 检测设备是否存在Camera硬件 */
	    private boolean checkCameraHardware(Context context) {
	        if (context.getPackageManager().hasSystemFeature(
	                PackageManager.FEATURE_CAMERA)) {
	            // 存在
	            return true;
	        } else {
	            // 不存在
	            return false;
	        }
	    }

	    /** 打开一个Camera */
	    public static Camera getCameraInstance() {
	        Camera c = null;
	        try {
	            c = Camera.open(); 
	        } catch (Exception e) {
	            Log.d(TAG, "打开Camera失败失败");
	        }
	        return c; 
	    }

	    private PictureCallback mPicture = new PictureCallback() {

	        @Override
	        public void onPictureTaken(byte[] data, Camera camera) {
	        	File f = new File("/data/data/org.tang.exam/attendance/");
	        	
	        	if(!f.exists()){
	        		f.mkdir();
	        	}
	        	
	            // 获取Jpeg图片，并保存在sd卡上
	            File pictureFile = new File("/data/data/org.tang.exam/attendance/" + DateTimeUtil.getCompactTime()
	                    + ".jpg");
	            
	            try {
	                FileOutputStream fos = new FileOutputStream(pictureFile);
	                fos.write(data);
	                fos.flush();
	                fos.close();
	                Log.d(TAG, "图片路径"+pictureFile.getAbsolutePath());
	                photoUrl = pictureFile.getAbsolutePath();
	            } catch (Exception e) {
	                Log.d(TAG, "保存图片失败");
	            }
	        }
	    };

	    @Override
	    protected void onDestroy() {
	        // 回收Camera资源
	        if(mCamera!=null){
	            mCamera.stopPreview();
	            mCamera.release();
	            mCamera=null;
	        }
	        super.onDestroy();
	    }
}
