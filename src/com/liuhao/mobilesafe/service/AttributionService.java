package com.liuhao.mobilesafe.service;

import com.liuhao.mobilesafe.engine.NumberAttributionService;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class AttributionService extends Service {

	public static final String TAG = "AttributionService";
	private TelephonyManager manager;
	private MyPhoneListener listener;
	private WindowManager windowManager;
	private TextView tv;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		listener = new MyPhoneListener();
		// 注册系统电话管理服务的监听器
		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneListener extends PhoneStateListener {

		// 电话状态发生改变时调用的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 处于静止状态，没有呼叫
				if(tv != null){
					windowManager.removeView(tv);
					tv = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				Log.i(TAG, "来电号码为：" + incomingNumber);
				String attribution = NumberAttributionService
						.getAttribution(incomingNumber);
				Log.i(TAG, "归属地为：" + attribution);
				// Toast.makeText(getApplicationContext(), "归属地为：" +
				// attribution, Toast.LENGTH_LONG).show();
				showAttribution(attribution);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话状态
				
				break;
			}
		}

		/**
		 * 显示归属地信息
		 * 
		 * @param attribution
		 */
		private void showAttribution(String attribution) {
			WindowManager.LayoutParams params = new LayoutParams();
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.format = PixelFormat.TRANSLUCENT;
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			params.setTitle("Toast");
			params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

			tv = new TextView(AttributionService.this);
			tv.setText("归属地为：" + attribution);
			windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
			windowManager.addView(tv, params);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册系统电话管理服务的监听器
		manager.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}

}
