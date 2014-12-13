package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class QueryNumberActivity extends Activity {
	
	private TextView tv_query_number_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_number);
		
		tv_query_number_result = (TextView) this.findViewById(R.id.tv_query_number_result);
	}
	
	public void query(View v){
		
	}
}
