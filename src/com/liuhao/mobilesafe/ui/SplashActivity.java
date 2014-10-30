package com.liuhao.mobilesafe.ui;

import java.io.File;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.domain.UpdateInfo;
import com.liuhao.mobilesafe.engine.DownloadFileTask;
import com.liuhao.mobilesafe.engine.UpdateInfoService;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";
	private TextView tv_splash_version;
	private LinearLayout ll_splash_main;
	private UpdateInfo info;
	private ProgressDialog pd;// 进度条
	private String versiontext;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (isNeedUpdate(versiontext)) {
				Log.i(TAG, "弹出升级对话框");
				showUpdateDialog();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);
		
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 默认情况下不显示进度，这个设置用于显示进度
		pd.setMessage("正在下载...");// 设置进度条显示的内容

		tv_splash_version = (TextView) this.findViewById(R.id.tv_splash_version);
		ll_splash_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);

		versiontext = getVersion();
		
		// 让当前Activity延时两秒钟，再去检查更新
		new Thread(){
			public void run() {
				try {
					sleep(2000);
					handler.sendEmptyMessage(0);// 向主线程发送一条空消息
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

		tv_splash_version.setText(versiontext);
		
		/*
		 * AlphaAnimation类：透明度变化动画类
		 * AlphaAnimation类是Android系统中的透明度变化动画类，用于控制View对象的透明度变化，该类继承于Animation类。
		 * AlphaAnimation类中的很多方法都与Animation类一致，该类中最常用的方法便是AlphaAnimation构造方法。
		 * 
		 * public AlphaAnimation (float fromAlpha, float toAlpha) 参数说明
		 * fromAlpha：开始时刻的透明度，取值范围0~1。 toAlpha：结束时刻的透明度，取值范围0~1。
		 */
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000); // Animation类的方法，设置持续时间
		ll_splash_main.startAnimation(aa); // 设置动画

		// 完成窗体的全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	private void showUpdateDialog() {
		// 弹出一个消息框
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.icon5); // 设置消息框的标题图标
		builder.setTitle("升级提醒"); // 设置消息框的标题
		builder.setMessage(info.getDescription()); // 设置要显示的内容
		builder.setCancelable(false); // 让用户不能按后退键取消
		builder.setPositiveButton("确定", new OnClickListener() { // 设置用户选择确定时的按键操作

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "下载pak文件:" + info.getApkurl());
						
						// 判断sd卡是否可用
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							
							// 调用子线程进行下载
							DownloadFileThreadTask task = new DownloadFileThreadTask(
									info.getApkurl(), Environment.getExternalStorageDirectory().getPath() + "/aanew.apk");
							pd.show();// 显示下载进度条
							new Thread(task).start();// 启动子线程
						} else {
							Toast.makeText(getApplicationContext(), "sd卡不可用",
									Toast.LENGTH_LONG).show();
							loadMainUI();
						}
					}
				});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "用户取消升级，进入程序主界面");
				loadMainUI();
			}
		});

		builder.create().show();

	}

	// 子线程，用于下载文件，因为下载文件比较耗时
	private class DownloadFileThreadTask implements Runnable {

		private String path;// 服务器路径
		private String filepath;// 本地文件路径

		public DownloadFileThreadTask(String path, String filepath) {
			this.path = path;
			this.filepath = filepath;
		}

		@Override
		public void run() {
			try {
				File file = DownloadFileTask.getFile(path, filepath, pd);
				Log.i(TAG, "下载更新apk成功");
				pd.dismiss();
				install(file);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "下载文件失败", Toast.LENGTH_LONG).show();
				pd.dismiss();
				loadMainUI();
			}
		}

	}

	/**
	 * 
	 * @param versiontext
	 *            当前客户端的版本信息
	 * @return 是否需要更新
	 */
	private boolean isNeedUpdate(String versiontext) {
		UpdateInfoService service = new UpdateInfoService(this);
		try {
			info = service.getUpdateInfo(R.string.updateurl);
			if ("".equals(info.getVersion()) || info.getVersion() == null) {
				Toast.makeText(this, "联网获取版本信息异常", Toast.LENGTH_SHORT).show();
				Log.i(TAG, "联网获取版本信息异常，进入到主界面");
				loadMainUI();
				return false;
			}
			String version = info.getVersion();
			if (versiontext.equals(version)) {
				Log.i(TAG, "版本号相同，无需升级，进入到主界面");
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "版本号不同，需要升级");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * Toast使用场景 1、需要提示用户，但又不需要用户点击“确定”或者“取消”按钮。 2、不影响现有Activity运行的简单提示。
			 */
			Toast.makeText(this, "获取更新信息异常", Toast.LENGTH_SHORT).show();// 弹出文本，并保持2秒
			Log.i(TAG, "获取更新信息异常，进入到主界面");
			loadMainUI();
			return false;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	/**
	 * 获取当前程序的版本号
	 * 
	 * @return
	 */
	private String getVersion() {
		// 获取一个PackageManager的实例，从而可以获取全局包信息
		PackageManager manager = getPackageManager();

		try {
			// Retrieve overall information about an application package that is
			// installed on the system.
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			// The version name of this package, as specified by the <manifest>
			// tag's versionName attribute.
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}

	}

	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish(); // 将当前Activity从任务栈中移除，用户按后退键时便不会再到SplashActivity界面来
	}
	
	
	/**
	 * 安装apk文件
	 * @param file
	 */
	private void install(File file){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		
		finish();// 终结当前Activity
		startActivity(intent);// 激活新的Activity
	}

}
