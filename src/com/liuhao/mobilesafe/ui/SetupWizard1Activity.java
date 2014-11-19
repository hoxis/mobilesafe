package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetupWizard1Activity extends Activity implements OnClickListener {

	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard1);
		
		button = (Button) this.findViewById(R.id.bt_next);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_next:
			finish();// 用户点击“后退”时不会再看到这个界面
			Intent intent = new Intent(this, SetupWizard2Activity.class);
			startActivity(intent);
			// 设置Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
	}

}
