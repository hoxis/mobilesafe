package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetupWizard3Activity extends Activity implements OnClickListener {

	private Button bt_select_contact;
	private Button bt_next;
	private Button bt_prev;
	private EditText et_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard3);
		
		bt_select_contact = (Button) this.findViewById(R.id.bt_select_contact);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_prev = (Button) this.findViewById(R.id.bt_previous);
		et_number = (EditText) this.findViewById(R.id.et_setup3_phonenumber);
		
		bt_select_contact.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_prev.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_select_contact:
			Intent intent = new Intent(this, SelectContactActivity.class);
			// 激活一个带返回值的界面
			startActivityForResult(intent, 0);
			break;
		case R.id.bt_next:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent4 = new Intent(this, SetupWizard4Activity.class);
			startActivity(intent4);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.bt_previous:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent2 = new Intent(this, SetupWizard2Activity.class);
			startActivity(intent2);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data != null){
			String phone = data.getStringExtra("phone");
			et_number.setText(phone);
			et_number.setTextColor(getResources().getColor(R.color.textcolor));
		}
		
	}

}
