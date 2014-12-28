package com.liuhao.mobilesafe.ui;

import java.io.File;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.engine.DownloadFileTask;
import com.liuhao.mobilesafe.service.AttributionService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AtoolsActivity extends Activity implements OnClickListener {

	protected static final int SUCCESS = 11;
	protected static final int ERROR = 10;
	private TextView tv_query;
	private ProgressDialog pd;
	private TextView tv_atools_attribution;
	private CheckBox cb_atools_attribution;
	private Intent serviceIntent;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				Toast.makeText(getApplicationContext(), "下载数据库成功", Toast.LENGTH_LONG).show();
				break;
			case ERROR:
				Toast.makeText(getApplicationContext(), "下载数据库失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);

		tv_atools_attribution = (TextView) this.findViewById(R.id.tv_atools_attribution);
		cb_atools_attribution = (CheckBox) this.findViewById(R.id.cb_atools_attribution);
		serviceIntent = new Intent(this, AttributionService.class);
		cb_atools_attribution.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					startService(serviceIntent);
					tv_atools_attribution.setTextColor(Color.WHITE);
					tv_atools_attribution.setText("显示来电归属地服务已开启");
				} else{
					stopService(serviceIntent);
					tv_atools_attribution.setTextColor(Color.RED);
					tv_atools_attribution.setText("显示来电归属地服务未开启");
				}
			}
		});
		
		tv_query = (TextView) this.findViewById(R.id.tv_atools_query);
		tv_query.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_atools_query:
			
			// 判断归属地数据库是否存在
			if(isDBExist()){
				Intent intent = new Intent(this, QueryNumberActivity.class);
				startActivity(intent);
			}
			else{
				// 提示用户下载数据库
				pd = new ProgressDialog(this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 默认情况下不显示进度，这个设置用于显示进度
				pd.setMessage("正在下载数据库...");
				pd.setCancelable(false);
				pd.setCanceledOnTouchOutside(false);
				pd.show();
				// 下载数据库
				new Thread(){
					public void run() {
						String path = getResources().getString(R.string.dbadressurl);
						String filepath = Environment.getExternalStorageDirectory().getPath() + "/address.db";
						try {
							DownloadFileTask.getFile(path, filepath, pd);
							pd.dismiss();
							Message msg = new Message();
							msg.what = SUCCESS;
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							pd.dismiss();
							Message msg = new Message();
							msg.what = ERROR;
							handler.sendMessage(msg);
						}
					};
				}.start();
			}
			break;

		}
	}

	/**
	 * 判断归属地数据库是否存在
	 * 
	 * @return
	 */
	public boolean isDBExist() {
		File file = new File(Environment.getExternalStorageDirectory().getPath() + "/address.db");
		return file.exists();
	}
}
