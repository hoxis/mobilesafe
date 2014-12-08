package com.liuhao.mobilesafe.receiver;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.engine.GpsInfoProvider;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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
			// 远程锁屏，并设置锁屏密码
			else if("#*lockscreen*#".equals(content.trim())){
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.resetPassword("123", 0);// 重置解锁密码
				manager.lockNow();// 立即锁屏
				abortBroadcast();
			} 
			// 远程擦除手机数据
			else if("#*wipedata*#".equals(content.trim())){
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.wipeData(0);
				abortBroadcast();
			} 
			// 利用MediaPlayer播放预设的音频文件
			else if("#*alarm*#".equals(content.trim())){
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.start();
				abortBroadcast();
			} 
		}
	}

}
