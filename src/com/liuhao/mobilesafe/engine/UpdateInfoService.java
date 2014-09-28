package com.liuhao.mobilesafe.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

import com.liuhao.mobilesafe.domain.UpdateInfo;

public class UpdateInfoService {

	private Context context; //应用程序环境的上下文信息

	public UpdateInfoService(Context context) {
		this.context = context;
	}

	// 将与网络通信的过程封装在ServiceInBackGround的doInBackground方法中
	private class ServiceInBackGround extends AsyncTask<Integer, Void, UpdateInfo>{

		@Override
		protected UpdateInfo doInBackground(Integer... params) {
			String path = context.getResources().getString(params[0]); //根据urlId获取资源文件中对应的内容
			UpdateInfo info = new UpdateInfo();
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(20000);
				conn.setRequestMethod("GET");
				
				InputStream is = conn.getInputStream(); //得到url对应的文件流，应该是xml文件流，需要对其进行解析
				
				info = UpdateInfoParser.getUpdateInfo(is); //对xml文件流进行解析，获取到更新信息实体
			} catch (Exception e) {
				e.printStackTrace();
			}
			return info;
		}
		
		@Override
		protected void onPostExecute(UpdateInfo result) {
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * @param urlId
	 *            服务器资源路径对应的id
	 * @return 更新信息
	 * @throws Exception
	 */
	public UpdateInfo getUpdateInfo(int urlId) throws Exception {
//		new serviceInBackGround().execute(urlId).get();
		return new ServiceInBackGround().execute(urlId).get();
	}

}
