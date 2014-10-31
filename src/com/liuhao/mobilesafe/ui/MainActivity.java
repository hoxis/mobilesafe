package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.adapter.MainUIAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private GridView gv_main;
	private MainUIAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set the activity content from a layout resource. The resource will be inflated, adding all top-level views to the activity
		setContentView(R.layout.mainscreen);
		gv_main = (GridView) this.findViewById(R.id.gv_main);
		adapter = new MainUIAdapter(this);
		gv_main.setAdapter(adapter);
		gv_main.setOnItemClickListener(this);
		
	}

	/**
	 * 当gridview的条目被点击的时候对应的回调
	 * parent ： gridView
	 * view : 当前被点击条目的 LinearLayout
	 * position : 点击条目对应的位置
	 * id : 代表的行号
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "点击的位置" + position);
		switch(position){
		case 0 :
			Log.i(TAG, "进入手机防盗");
			break;
		}
	}

}
