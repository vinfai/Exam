package org.tang.exam.activity;

import org.tang.exam.R;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.utils.AMapUtil;
import org.tang.exam.utils.ToastUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;


/**
 * 演示MapView的基本用法
 */
public class MapActivity extends BaseActionBarActivity  implements
OnGeocodeSearchListener {
	
	final static String TAG = "MainActivity";
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String addressName;
	private AMap aMap;
	private MapView mapView;
	private LatLonPoint latLonPoint ;
	private Marker regeoMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.attendance_current_place));
		bar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		bar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.geocoder_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		Bundle bundle = getIntent().getBundleExtra("tag");  
		Double longitude = bundle.getDouble("longitude");
		Double latitude = bundle.getDouble("latitude");
		latLonPoint = new LatLonPoint(latitude,longitude);
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}
	
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 17));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
			regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
		}
		
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
	}
	
	
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
	

	@Override
	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
		
	}

	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 17));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				ToastUtil.show(MapActivity.this, addressName);
			} else {
				ToastUtil.show(MapActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(MapActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(MapActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(MapActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}
	
	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

}
