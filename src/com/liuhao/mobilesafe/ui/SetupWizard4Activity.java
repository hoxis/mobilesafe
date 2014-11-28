package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetupWizard4Activity extends Activity implements OnClickListener {

	private Button bt_prev;
	private Button bt_setup_finish;
	private CheckBox cb_isprotecting;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard4);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		bt_prev = (Button) this.findViewById(R.id.bt_previous);
		bt_setup_finish = (Button) this.findViewById(R.id.bt_setup_finish);
		cb_isprotecting = (CheckBox) this.findViewById(R.id.cb_isprotecting);

		bt_prev.setOnClickListener(this);
		bt_setup_finish.setOnClickListener(this);
		
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

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_previous:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent3 = new Intent(this, SetupWizard3Activity.class);
			startActivity(intent3);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;
		case R.id.bt_setup_finish:
			finishSetup();
			
			if(cb_isprotecting.isChecked()){
				finish();
			}else{
				AlertDialog.Builder builder = new Builder(SetupWizard4Activity.this);
				builder.setTitle("提醒");
				builder.setMessage("强烈建议开启手机防盗，是否完成设置");
				
				builder.setPositiveButton("不用了", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						finishSetup();
					}
				});
				builder.setNegativeButton("好的", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				builder.create().show();
			}
			break;
		}
	}

	private void finishSetup() {
		// 设置一个标识，标记用户已经完成过设置向导
		Editor editor = sp.edit();
		editor.putBoolean("isAlreadySetup", true);
		editor.commit();
	}

}
