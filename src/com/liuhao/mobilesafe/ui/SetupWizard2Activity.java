package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class SetupWizard2Activity extends Activity implements OnClickListener {

	private Button bt_bind;
	private Button bt_next;
	private Button bt_prev;
	private CheckBox cb_bind;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.setup_wizard2);
		bt_bind = (Button) this.findViewById(R.id.bt_bind);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_prev = (Button) this.findViewById(R.id.bt_previous);
		cb_bind = (CheckBox) this.findViewById(R.id.cb_bind);

		bt_bind.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_prev.setOnClickListener(this);
		// 首先初始化chexkbox的状态
		String sim = sp.getString("sim", null);
		if(sim != null){
			cb_bind.setText("已绑定SIM卡");
			cb_bind.setChecked(true);
		}else{
			cb_bind.setText("未绑定SIM卡");
			cb_bind.setChecked(false);
		}
		
		cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					setSimInfo();
					cb_bind.setText("已绑定SIM卡");
				}else{
					cb_bind.setText("未绑定SIM卡");
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_bind:
			setSimInfo();
			cb_bind.setText("已绑定SIM卡");
			cb_bind.setChecked(true);
			break;
		case R.id.bt_next:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent3 = new Intent(this, SetupWizard3Activity.class);
			startActivity(intent3);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.bt_previous:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent1 = new Intent(this, SetupWizard1Activity.class);
			startActivity(intent1);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
	}

	/**
	 * 绑定SIM串号
	 * 
	 */
	private void setSimInfo() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String simSerialNumber = manager.getSimSerialNumber();
		Editor edit = sp.edit();
		edit.putString("sim", simSerialNumber);
		edit.commit();
		Toast.makeText(getApplicationContext(), "SIM卡已绑定", Toast.LENGTH_SHORT).show();
	}
}
