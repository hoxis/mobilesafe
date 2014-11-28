package com.liuhao.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "重启完毕");
		// 判断手机是否处于保护状态
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		boolean isProtecting = sp.getBoolean("isProtecting", false);
		if(isProtecting){
			TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = manager.getSimSerialNumber();
			String realSim = sp.getString("sim", "");
			if(!currentSim.equals(realSim)){// SIM卡串号不同
				Log.i(TAG, "发送报警短信");
				// 发送报警短信给安全号码
				SmsManager smsManager = SmsManager.getDefault();
				String safenumber = sp.getString("safenumber", "");
				smsManager.sendTextMessage(safenumber, null, "手机绑定SIM卡发生变化，手机可能被盗！", null, null);
			}
		}
	}

}
