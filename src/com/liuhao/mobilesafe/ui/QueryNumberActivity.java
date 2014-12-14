package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.engine.NumberAttributionService;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QueryNumberActivity extends Activity {
	
	private TextView tv_query_number_result;
	private EditText et_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_number);
		
		tv_query_number_result = (TextView) this.findViewById(R.id.tv_query_number_result);
		et_number = (EditText) this.findViewById(R.id.et_query_number);
	}
	
	public void query(View v){
		// 判断输入是否为空
		String number = et_number.getText().toString().trim();
		if(TextUtils.isEmpty(number)){
			Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			et_number.startAnimation(shake);
		}
		else{
			// 打开数据库 查询号码归属地
			String attribution = NumberAttributionService.getAttribution(number);
			tv_query_number_result.setText("归属地：" + attribution);
			tv_query_number_result.setTextIsSelectable(true);
		}
	}
}
