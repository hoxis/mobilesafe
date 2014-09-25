package com.liuhao.mobilesafe.ui;

import com.liuhao.mobilesafe.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private TextView tv_splash_version;
	private LinearLayout ll_splash_main;

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
