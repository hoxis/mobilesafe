package com.liuhao.mobilesafe.receiver;

import com.liuhao.mobilesafe.engine.GpsInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// 获取短信内容，判断是否是 #*location*#
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
			String content = sms.getMessageBody();
			Log.i(TAG, "短信内容：" + content);
			String sender = sms.getOriginatingAddress();
			if("#*location*#".equals(content.trim())){
				// 停止广播
				abortBroadcast();
				GpsInfoProvider provider = GpsInfoProvider.getInstance(context);
				String location = provider.getLocation();
				SmsManager smsManager = SmsManager.getDefault();
				if(!"".equals(location)){
					smsManager.sendTextMessage(sender, null, location, null, null);
				}
			}
		}
	}

}
