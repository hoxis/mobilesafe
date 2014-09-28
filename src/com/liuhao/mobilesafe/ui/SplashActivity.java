package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.domain.UpdateInfo;
import com.liuhao.mobilesafe.engine.UpdateInfoService;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.splash);
        
        tv_splash_version = (TextView) this.findViewById(R.id.tv_splash_version);
        ll_splash_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
        
        String versiontext = getVersion();
        tv_splash_version.setText(versiontext);
        
        if(isNeedUpdate(versiontext)){
        	Log.i(TAG, "弹出升级对话框");
        	showUpdateDialog();
        }
        
        /* AlphaAnimation类：透明度变化动画类
         * AlphaAnimation类是Android系统中的透明度变化动画类，用于控制View对象的透明度变化，该类继承于Animation类。
         * AlphaAnimation类中的很多方法都与Animation类一致，该类中最常用的方法便是AlphaAnimation构造方法。
         * 
         * public AlphaAnimation (float fromAlpha, float toAlpha)
			参数说明
				fromAlpha：开始时刻的透明度，取值范围0~1。
				toAlpha：结束时刻的透明度，取值范围0~1。
         */
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(2000); //Animation类的方法，设置持续时间 
        ll_splash_main.startAnimation(aa); //设置动画 
        
        //完成窗体的全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showUpdateDialog() {
    	//弹出一个消息框
    	AlertDialog.Builder builder = new Builder(this);
    	builder.setIcon(R.drawable.icon5); //设置消息框的标题图标
    	builder.setTitle("升级提醒"); //设置消息框的标题
    	builder.setMessage(info.getDescription()); //设置要显示的内容
    	builder.setCancelable(false); //让用户不能按后退键取消
    	builder.setPositiveButton("确定", new OnClickListener() { //设置用户选择确定时的按键操作
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载pak文件:" + info.getApkurl());
			}
		});
    	
    	builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "用户取消升级，进入程序主界面");
			}
		});
    	
    	builder.create().show();
    	
	}

	/**
     * 
     * @param versiontext 当前客户端的版本信息
     * @return 是否需要更新
     */
    private boolean isNeedUpdate(String versiontext) {
    	UpdateInfoService service = new UpdateInfoService(this);
    	try {
			info = service.getUpdateInfo(R.string.updateurl);
			String version = info.getVersion();
			if(versiontext.equals(version)){
				Log.i(TAG, "版本号相同，无需升级，进入到主界面");
				return false;
			}
			else{
				Log.i(TAG, "版本号不同，需要升级");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			/**
			 * Toast使用场景
			 * 1、需要提示用户，但又不需要用户点击“确定”或者“取消”按钮。
			 * 2、不影响现有Activity运行的简单提示。
			 */
			Toast.makeText(this, "获取更新信息异常", 2).show();//弹出文本，并保持2秒
			Log.i(TAG, "获取更新信息异常，进入到主界面");
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
     * @return
     */
    private String getVersion(){
    	// 获取一个PackageManager的实例，从而可以获取全局包信息
    	PackageManager manager = getPackageManager();

    	try {
    		// Retrieve overall information about an application package that is installed on the system.
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			// The version name of this package, as specified by the <manifest> tag's versionName attribute.
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
    	
    }
    
}
