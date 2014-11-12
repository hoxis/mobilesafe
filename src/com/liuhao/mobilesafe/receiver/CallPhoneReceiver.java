package com.liuhao.mobilesafe.receiver;

import com.liuhao.mobilesafe.ui.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CallPhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		if("20122012".equals(number)){
			Intent lostIntent = new Intent(context, LostProtectedActivity.class);
			// 指定要激活的Activity在自己的任务栈中运行
			lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(lostIntent);
			// 终止这个拨号
			// 不能通过abortBroadcast()终止
			setResultData(null);
		}
	}

}
