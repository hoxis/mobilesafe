package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set the activity content from a layout resource. The resource will be inflated, adding all top-level views to the activity
		setContentView(R.layout.main);
	}
	
}
