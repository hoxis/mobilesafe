package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AtoolsActivity extends Activity implements OnClickListener {

	private TextView tv_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);

		tv_query = (TextView) this.findViewById(R.id.tv_atools_query);

		tv_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_query:
			Intent intent = new Intent(this, QueryNumberActivity.class);
			startActivity(intent);
			break;

		}
	}
}
