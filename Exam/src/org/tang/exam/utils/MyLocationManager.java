package org.tang.exam.utils;

import android.content.Context;  
import android.location.Location;  
import android.location.LocationListener;  
import android.location.LocationManager;  
import android.os.Bundle;  
  
public class MyLocationManager {  
    private static Context mContext;  
    private LocationManager gpsLocationManager;  
    private LocationManager networkLocationManager;  
    private static final int MINTIME = 200;  
    private static final int MININSTANCE = 2;  
    private static MyLocationManager instance;  
    private Location lastLocation = null;  
    private static LocationCallBack mCallback;  
  
    public   void init(Context c, LocationCallBack callback) {  
        mContext = c;  
        mCallback = callback;  
        
        // Gps 定位  
        gpsLocationManager = (LocationManager) mContext  
                .getSystemService(Context.LOCATION_SERVICE);  
        Location gpsLocation = gpsLocationManager  
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
                MINTIME, MININSTANCE, locationListener);  
        // 基站定位  
        networkLocationManager = (LocationManager) mContext  
                .getSystemService(Context.LOCATION_SERVICE);  
        Location networkLocation = gpsLocationManager  
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        networkLocationManager.requestLocationUpdates(  
                LocationManager.NETWORK_PROVIDER, MINTIME, MININSTANCE,  
                locationListener);  
    }  
  
    public MyLocationManager() {}  
  
    public static  MyLocationManager getInstance() {  
        if (null == instance) {  
            instance = new MyLocationManager();  
        }  
        return instance;  
    }  
  
    private void updateLocation(Location location) {  
        lastLocation = location;  
        mCallback.onCurrentLocation(location);  
    }  
  
    private final LocationListener locationListener = new LocationListener() {  
    	
    	   // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数   
        public void onStatusChanged(String provider, int status, Bundle extras) {  
        }  
        
        //  Provider被enable时触发此函数，比如GPS被打开   
        public void onProviderEnabled(String provider) {  
        }  
        
        // Provider被disable时触发此函数，比如GPS被关闭   
        public void onProviderDisabled(String provider) {  
        	
        }  
  
        public void onLocationChanged(Location location) {  
            updateLocation(location);  
        }  
    };  
  
    public Location getMyLocation() {  
        return lastLocation;  
    }  
  
    private static int ENOUGH_LONG = 1000 * 60;  
  
    public interface LocationCallBack {  
        /** 
         * 当前位置 
         *  
         * @param location 
         */  
        void onCurrentLocation(Location location);  
    }  
  
    public void destoryLocationManager() {  
        gpsLocationManager.removeUpdates(locationListener);  
        networkLocationManager.removeUpdates(locationListener);  
    }  
}  