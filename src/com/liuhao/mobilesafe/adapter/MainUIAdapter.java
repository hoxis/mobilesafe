package com.liuhao.mobilesafe.adapter;

import com.liuhao.mobilesafe.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainUIAdapter extends BaseAdapter {
	
	private static final String TAG = "MainUIAdapter";
	private Context context;//用于接收传递过来的Context对象
	private LayoutInflater inflater;
	private static ImageView iv_icon;
	private static TextView tv_name;
	private SharedPreferences sp;
	
	public MainUIAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	// 数据源，子条目的文本内容
	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "任务管理", "上网管理",
			"手机杀毒", "系统优化", "高级工具", "设置中心" };

	// 数据源，子条目的对应图片
	private static int[] icons = { R.drawable.widget05, R.drawable.widget02,
			R.drawable.widget01, R.drawable.widget07, R.drawable.widget05,
			R.drawable.widget04, R.drawable.widget06, R.drawable.widget03,
			R.drawable.widget08 };

	/**
	 * How many items are in the data set represented by this Adapter.
	 * 在此适配器中所代表的数据集中的条目数
	 */
	@Override
	public int getCount() {
		return names.length;
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 * 获取数据集中与指定索引对应的数据项
	 */
	@Override
	public Object getItem(int position) {
		return position;
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 * 取在列表中与指定索引对应的行id
	 */
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
		
		// 得到布局文件对应的View对象
		// inflate()方法一般接收两个参数，第一个参数就是要加载的布局id，
		// 第二个参数是指给该布局的外部再嵌套一层父布局，如果不需要就直接传null。
		View view = inflater.inflate(R.layout.mainscreen_item, null);
		iv_icon = (ImageView) view.findViewById(R.id.iv_main_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_main_name);
		
		iv_icon.setImageResource(icons[position]);
		tv_name.setText(names[position]);
		if(position == 0){
			// 从SharedPreferences获取条目的文本内容
			String name = sp.getString("lost_name", null);
			if(!"".equals(name) && null != name){
				tv_name.setText(name);
			}
		}
		
		return view;
	}

}
