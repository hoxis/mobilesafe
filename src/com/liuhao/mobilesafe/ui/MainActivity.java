package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.adapter.MainUIAdapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private GridView gv_main;
	private MainUIAdapter adapter;
	// 用来持久化一些配置信息
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Set the activity content from a layout resource. The resource will be inflated, adding all top-level views to the activity
		setContentView(R.layout.mainscreen);
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);// 初始化
		
		gv_main = (GridView) this.findViewById(R.id.gv_main);
		adapter = new MainUIAdapter(this);
		gv_main.setAdapter(adapter);
		gv_main.setOnItemClickListener(this);
		
		gv_main.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
				// 长按只对第一个条目有效
				if(position == 0){
					Builder builder = new Builder(MainActivity.this);
					builder.setTitle("设置");
					builder.setMessage("请输入要更改的内容");
					
					final EditText et = new EditText(MainActivity.this);
					et.setHint("请输入内容，长度在1-4之间");
					builder.setView(et);
					
					builder.setPositiveButton("确定", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String name = et.getText().toString().trim();// 获取用户的输入值
							if("".equals(name)){
								Toast.makeText(getApplicationContext(), "输入为空无效哦~", Toast.LENGTH_LONG).show();
								return;
							}else if(name.length() > 4){
								Toast.makeText(getApplicationContext(), "输入超过4个字啦~", Toast.LENGTH_LONG).show();
								return;
							}else{
								Editor editor = sp.edit();// 用于修改一个SharedPreferences的值
								editor.putString("lost_name", name);
								// 完成数据的提交
								editor.commit();
								TextView tv = (TextView) view.findViewById(R.id.tv_main_name);
								tv.setText(name);// 更新条目的文本内容
							}
						}
					});
					
					builder.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					
					builder.create().show();
				}
				return false;
			}
			
		});
		
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
			Intent lostIntent = new Intent(MainActivity.this, LostProtectedActivity.class);
			startActivity(lostIntent);
			break;
		case 7 :
			Log.i(TAG, "进入高级工具");
			Intent atoolsIntent = new Intent(MainActivity.this, AtoolsActivity.class);
			startActivity(atoolsIntent);
			break;
		}
	}

}
