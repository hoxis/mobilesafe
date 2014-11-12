package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class LostProtectedActivity extends Activity {

	private static final String TAG = "LostProtectedActivity";
	private SharedPreferences sp;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		// 判读用户是否已经设置了密码
		if (isPwdSetup()) {
			Log.i(TAG, "设置了密码，弹出输入密码的对话框");
		} else {
			Log.i(TAG, "未设置密码，弹出设置密码对话框");
			showFirstEntryDialog();
		}
	}

	/**
	 * 第一次进入程序时弹出的设置密码的对话框 
	 * 使用自定义对话框样式
	 */
	private void showFirstEntryDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(R.layout.first_entry_dialog);// 设置要显示的内容
		dialog.show();
	}

	/**
	 * 检查sharedpreference中是否有密码的设置
	 * 
	 * @return
	 */
	private boolean isPwdSetup() {
		String password = sp.getString("password", null);
		if (password == null) {
			return false;
		} else {
			if ("".equals(password)) {
				return false;
			} else {
				return true;
			}
		}
	}

}
