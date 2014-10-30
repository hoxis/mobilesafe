package com.liuhao.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

public class DownloadFileTask {

	/**
	 * @param path
	 *            服务器文件路径
	 * @param filepath
	 *            本地文件路径
	 * @param pd
	 * 			  进度条，用以显示下载进度
	 * @return 本地文件对象
	 * @throws Exception
	 */
	public static File getFile(String path, String filepath, ProgressDialog pd) throws Exception {

		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(5000);

		// 读取数据没有异常
		if (conn.getResponseCode() == 200) {
			int total = conn.getContentLength();// 获取内容的总长度
			pd.setMax(total);
			InputStream is = conn.getInputStream();// 获取文件输入流
			File file = new File(filepath);// 本地文件对象
			FileOutputStream fos = new FileOutputStream(file);//本地文件输出流
			byte[] buffer = new byte[1024];
			int length = 0;
			int process = 0;// 当前进度
			while ((length = is.read(buffer)) != -1) {
				fos.write(buffer, 0, length);
				process += length;
				pd.setProgress(process);// 设置当前进度
				Thread.sleep(50);
			}
			fos.flush();
			fos.close();
			is.close();

			return file;
		}

		return null;
	}

}
