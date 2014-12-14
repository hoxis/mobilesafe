package com.liuhao.mobilesafe.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class AttributionDao {
	
	/**
	 * 打开已有数据库
	 * @param path 数据库的路径
	 * @return 数据库对象
	 */
	public static SQLiteDatabase getAttributionDb(String path){
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
}
