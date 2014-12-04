package com.liuhao.mobilesafe.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 使用单例模式，保证这个类只有一个实例
 * 
 * @author liuhao
 * 
 */
public class GpsInfoProvider {

	private static GpsInfoProvider myGpsInfoProvider;
	private static Context context;
	private static MyLocationListener listener;

	// 1.私有化构造方法
	private GpsInfoProvider() {
	}

	// 2.提供一个静态方法，可以返回一个它的实例
	// synchronized保证这个方法会完整的执行
	public static synchronized GpsInfoProvider getInstance(Context context) {
		if (myGpsInfoProvider == null) {
			myGpsInfoProvider = new GpsInfoProvider();
			GpsInfoProvider.context = context;
		}
		return myGpsInfoProvider;
	}

	/**
	 * 获取GPS信息
	 * 
	 * @return
	 */
	public String getLocation() {
		// 获取系统位置服务管理
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// 获取最佳的位置提供者
		String provider = getProvider(manager);
		
		// 注册位置的监听器
		manager.requestLocationUpdates(provider, 60000, 50, getListener());

		// 将位置信息写入SharedPreference
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String location = sp.getString("location", "");

		return location;
	}

	/**
	 * 停止GPS监听
	 */
	public void stopGpsListener() {
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		manager.removeUpdates(getListener());
	}

	private synchronized MyLocationListener getListener() {
		if (listener == null) {
			listener = new MyLocationListener();
		}
		return listener;
	}

	private class MyLocationListener implements LocationListener {

		/**
		 * 当手机位置发生改变时调用的方法
		 */
		@Override
		public void onLocationChanged(Location location) {
			String longitude = "longitude" + location.getLongitude();// 经度
			String latitude = "latitude" + location.getLatitude();// 纬度

			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", longitude + "-" + latitude);
			editor.commit();// 最近一次获取的位置信息存放在SharedPreference中
		}

		/**
		 * 某一个设备的状态发生改变（可用-->不可用，不可用-->可用）时调用
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * 某个设备被打开时
		 */
		@Override
		public void onProviderEnabled(String provider) {

		}

		/**
		 * 某个设备被关闭
		 */
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	/**
	 * @param manager
	 *            位置管理服务
	 * @return 最好的位置提供者
	 */
	private String getProvider(LocationManager manager) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 设置定位精确度  Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAltitudeRequired(false);// 设置是否需要海拔信息
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 设置对功耗的需求
		criteria.setSpeedRequired(true);// 设置是否要求速度
		criteria.setCostAllowed(true);// 设置是否允许运营商收费
		return manager.getBestProvider(criteria, true);
	}
}
