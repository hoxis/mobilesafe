package com.liuhao.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.liuhao.mobilesafe.db.dao.AttributionDao;

public class NumberAttributionService {

	/**
	 * 在数据库中查询输入号码对应的归属地
	 * @param number 待查询号码
	 * @return 归属地
	 */
	public static String getAttribution(String number) {
		String attribution = number;
		String pattern = "^1[3458]\\d{9}$"; // 手机号码
		if(number.matches(pattern)){
			SQLiteDatabase db = AttributionDao.getAttributionDb(Environment.getExternalStorageDirectory().getPath() + "/address.db");
			if(db.isOpen()){
				Cursor cursor = db.rawQuery("select city from info i where i.mobileprefix = ?", new String[]{number.substring(0, 7)});
				if(cursor.moveToNext()){
					attribution = cursor.getString(0);
				}
				cursor.close();
			}
			db.close();
		}
		else{
			int len = number.length();
			switch (len) {
			case 4:
				attribution = "模拟器";
				break;
			case 7:
				attribution = "本地号码";
				break;
			case 8:
				attribution = "本地号码";
				break;
			case 10: // 3位区号+7位号码
				// select city from info i where i.area="0511" limit 1
				SQLiteDatabase db = AttributionDao.getAttributionDb(Environment.getExternalStorageDirectory().getPath() + "/address.db");
				if(db.isOpen()){
					Cursor cursor = db.rawQuery("select city from info i where i.area=? limit 1", new String[]{number.substring(0, 3)});
					if(cursor.moveToNext()){
						attribution = cursor.getString(0);
					}
					cursor.close();
				}
				db.close();
			case 11: // 4位区号 + 7位电话号码 或者 3位区号 + 8位手机号码
				// select city from info i where i.area="0511" limit 1
				SQLiteDatabase db2 = AttributionDao.getAttributionDb(Environment.getExternalStorageDirectory().getPath() + "/address.db");
				if(db2.isOpen()){
					Cursor cursor = db2.rawQuery("select city from info i where i.area=? limit 1", new String[]{number.substring(0, 3)});
					if(cursor.moveToNext()){
						attribution = cursor.getString(0);
					}
					cursor = db2.rawQuery("select city from info i where i.area=? limit 1", new String[]{number.substring(0, 4)});
					if(cursor.moveToNext()){
						attribution = cursor.getString(0);
					}
					cursor.close();
				}
				db2.close();
			case 12: // 4位区号 + 8位电话号码 
				// select city from info i where i.area="0511" limit 1
				SQLiteDatabase db3 = AttributionDao.getAttributionDb(Environment.getExternalStorageDirectory().getPath() + "/address.db");
				if(db3.isOpen()){
					Cursor cursor = db3.rawQuery("select city from info i where i.area=? limit 1", new String[]{number.substring(0, 4)});
					if(cursor.moveToNext()){
						attribution = cursor.getString(0);
					}
					cursor.close();
				}
				db3.close();
			}
		}
		
		
		return attribution;
	}

}
