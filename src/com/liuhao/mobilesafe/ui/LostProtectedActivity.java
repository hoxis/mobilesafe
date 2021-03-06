package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.util.MD5Encoder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LostProtectedActivity extends Activity implements OnClickListener {

	private static final String TAG = "LostProtectedActivity";
	private SharedPreferences sp;
	private Dialog dialog;
	private EditText et_pwd;
	private EditText et_pwd_confirm;
	private TextView tv_lost_protected_number;
	private TextView tv_reEntry_setup_wizard;
	private CheckBox cb_isprotecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		// 判读用户是否已经设置了密码
		if (isPwdSetup()) {
			Log.i(TAG, "设置了密码，弹出输入密码的对话框");
			showNormalEntryDialog();
		} else {
			Log.i(TAG, "未设置密码，弹出设置密码对话框");
			showFirstEntryDialog();
		}
	}

	/**
	 * 正常登录的对话框
	 * 
	 */
	private void showNormalEntryDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		View view = View.inflate(this, R.layout.normal_entry_dialog, null);
		et_pwd = (EditText) view.findViewById(R.id.et_normal_entry_pwd);
		Button bt_confirm = (Button) view.findViewById(R.id.bt_normal_dialog_confirm);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_normal_dialog_cancel);
		
		// 设置按钮对应的点击事件
		bt_confirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);// 设置dialog不可以点击其他地方时消失
//		dialog.setCancelable(false);// 设置dialog不可以点返回键时消失
		dialog.show();
	}

	/**
	 * 第一次进入程序时弹出的设置密码的对话框 
	 * 使用自定义对话框样式
	 */
	private void showFirstEntryDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
//		dialog.setContentView(R.layout.first_entry_dialog);// 设置要显示的内容
		View view = View.inflate(this, R.layout.first_entry_dialog, null);
		et_pwd = (EditText) view.findViewById(R.id.et_first_entry_pwd);
		et_pwd_confirm = (EditText) view.findViewById(R.id.et_first_entry_pwd_confirm);
		Button bt_confirm = (Button) view.findViewById(R.id.bt_first_dialog_confirm);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_first_dialog_cancel);
		
		// 设置按钮对应的点击事件
		bt_confirm.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);// 设置dialog不可以点击其他地方时消失
//		dialog.setCancelable(false);// 设置dialog不可以点返回键时消失
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

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		// 点击取消
		case R.id.bt_first_dialog_cancel:
			dialog.dismiss();
			break;
		case R.id.bt_first_dialog_confirm:
			String pwd = et_pwd.getText().toString().trim();
			String pwd_confirm = et_pwd_confirm.getText().toString().trim();
			
			// 输入的密码中包含空值
			if("".equals(pwd) || "".equals(pwd_confirm)){
				Toast.makeText(getApplicationContext(), "输入不能为空！", Toast.LENGTH_LONG).show();
				return;
			}else{
				if(pwd.equals(pwd_confirm)){
					Editor editor = sp.edit();
					editor.putString("password", MD5Encoder.encode(pwd));
					editor.commit();
				}
				// 两次输入不一致
				else{
					Toast.makeText(getApplicationContext(), "两次输入密码不相同！", Toast.LENGTH_LONG).show();
					et_pwd_confirm.selectAll();// 用户输入错误后，对文本进行全选，方便用户进行删除重新输入
					return;
				}
			}
			dialog.dismiss();
			// 进入手机防盗设置界面
			finish();
			Intent intent = new Intent(getApplicationContext(), SetupWizard1Activity.class);
			startActivity(intent);
			break;
		case R.id.bt_normal_dialog_cancel:
			dialog.dismiss();
			break;
		case R.id.bt_normal_dialog_confirm:
			String input_pwd = et_pwd.getText().toString();
			if("".equals(input_pwd)){
				Toast.makeText(getApplicationContext(), "输入不能为空！", Toast.LENGTH_LONG).show();
				return;
			}else{
				String password = sp.getString("password", "");
				if(!password.equals(MD5Encoder.encode(input_pwd))){
					Toast.makeText(getApplicationContext(), "输入密码不正确，请重新输入！", Toast.LENGTH_LONG).show();
					et_pwd.selectAll();// 用户输入错误后，对文本进行全选，方便用户进行删除重新输入
					return;
				}
			}
			if(isSetup()){
				Log.i(TAG, "加载手机防盗主界面");
				setContentView(R.layout.lost_protected);
				
				tv_lost_protected_number = (TextView) this.findViewById(R.id.tv_lost_protected_number);
				tv_reEntry_setup_wizard = (TextView) this.findViewById(R.id.tv_reEntry_setup_wizard);
				cb_isprotecting = (CheckBox) this.findViewById(R.id.cb_isprotecting);
				
				// 初始化这些控件
				String number = sp.getString("safenumber", null);
				tv_lost_protected_number.setText("安全手机号码为：" + number);
				
				// 重新进入设置向导
				tv_reEntry_setup_wizard.setOnClickListener(this);
				
				// 初始化CheckBox的状态
				boolean isProtecting = sp.getBoolean("isProtecting", false);
				if(isProtecting){
					cb_isprotecting.setText("手机防盗保护中");
					cb_isprotecting.setChecked(true);
				}
				
				cb_isprotecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked){
							cb_isprotecting.setText("手机防盗保护中");
							Editor editor = sp.edit();
							editor.putBoolean("isProtecting", true);
							editor.commit();
						}else{
							cb_isprotecting.setText("没有开启防盗保护");
							Editor editor = sp.edit();
							editor.putBoolean("isProtecting", false);
							editor.commit();
						}
					}
				});
				
				
			}else{
				Log.i(TAG, "激活设置向导界面");
				finish();
				Intent intent1 = new Intent(getApplicationContext(), SetupWizard1Activity.class);
				startActivity(intent1);
			}
			dialog.dismiss();
			break;
		case R.id.tv_reEntry_setup_wizard:
			finish();
			Intent intent11 = new Intent(LostProtectedActivity.this, SetupWizard1Activity.class);
			startActivity(intent11);
			break;
		}
		
	}

	/**
	 * 判断用户是否进行过设置向导
	 * @return
	 */
	private boolean isSetup(){
		return sp.getBoolean("isAlreadySetup", false);
	}
}
