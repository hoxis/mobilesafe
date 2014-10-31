package com.liuhao.mobilesafe.adapter;

import com.liuhao.mobilesafe.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainUIAdapter extends BaseAdapter {
	
	private static final String TAG = "MainUIAdapter";
	private Context context;
	private LayoutInflater inflater;
	private static ImageView iv_icon;
	private static TextView tv_name;
	
	public MainUIAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "任务管理", "上网管理",
			"手机杀毒", "系统优化", "高级工具", "设置中心" };

	private static int[] icons = { R.drawable.widget05, R.drawable.widget02,
			R.drawable.widget01, R.drawable.widget07, R.drawable.widget05,
			R.drawable.widget04, R.drawable.widget06, R.drawable.widget03,
			R.drawable.widget08 };

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//getView()方法被调用了多少次？
		//9次？
		//GridView 控件bug
		// 使用静态变量引用来减少内存中申请的引用的个数
		Log.i(TAG, "getView" + position);
		View view = inflater.inflate(R.layout.mainscreen_item, null);
		iv_icon = (ImageView) view.findViewById(R.id.iv_main_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_main_name);
		
		iv_icon.setImageResource(icons[position]);
		tv_name.setText(names[position]);
		
		return view;
	}

}
