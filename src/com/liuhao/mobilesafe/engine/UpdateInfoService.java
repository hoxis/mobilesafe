package com.liuhao.mobilesafe.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.liuhao.mobilesafe.domain.UpdateInfo;

public class UpdateInfoService {

	private Context context; //应用程序环境的上下文信息

	public UpdateInfoService(Context context) {
		this.context = context;
	}

	/**
	 * @param urlId
	 *            服务器资源路径对应的id
	 * @return 更新信息
	 * @throws Exception
	 */
	public UpdateInfo getUpdateInfo(int urlId) throws Exception {
		String path = context.getResources().getString(urlId); //根据urlId获取资源文件中对应的内容
		URL url = new URL(path);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(2000);
		conn.setRequestMethod("GET");
		
		InputStream is = conn.getInputStream(); //得到url对应的文件流，应该是xml文件流，需要对其进行解析
		
		return UpdateInfoParser.getUpdateInfo(is); //对xml文件流进行解析，获取到更新信息实体
	}

}
