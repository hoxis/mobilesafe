package com.liuhao.mobilesafe.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.liuhao.mobilesafe.domain.UpdateInfo;

public class UpdateInfoParser {

	/**
	 * @param is xml格式的文件输入流
	 * @return 解析好的UpdateInfo
	 */
	public static UpdateInfo getUpdateInfo(InputStream is) throws Exception{
		XmlPullParser parser = Xml.newPullParser();
		UpdateInfo info = new UpdateInfo();
		
		// 初始化parser解析器，设置准备对哪个输入流进行解析
		// 这个方法会对parser进行重置，同时会将事件类型(event type)定位到文档初始位置(START_DOCUMENT)
		parser.setInput(is, "utf-8"); 
		
		int type = parser.getEventType(); //Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)

		while(type != XmlPullParser.END_DOCUMENT){
			switch (type) {
			// 对其中的标签类型进行处理
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					info.setVersion(version);
				}
				else if("description".equals(parser.getName())){
					String description = parser.nextText();
					info.setDescription(description);
				}
				else if("apkurl".equals(parser.getName())){
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
				break;
			}
			
			type = parser.next();
		}
		return info;
	}
	
}
