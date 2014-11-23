package com.liuhao.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.liuhao.mobilesafe.domain.ContactInfo;

public class ContactInfoService {

	private Context context;

	public ContactInfoService(Context context) {
		this.context = context;
	}

	public List<ContactInfo> getContactInfos() {

		ContentResolver resolver = context.getContentResolver();

		// 1.获取联系人的id
		// 2.根据联系人的id获取联系人姓名
		// 3.根据联系人id 数据的type，获取对应的数据（电话，email等）
		
		List<ContactInfo> infos = new ArrayList<ContactInfo>();
		ContactInfo info;
		
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		
		Cursor cursor = resolver.query(uri, null, null, null, null);
		while (cursor.moveToNext()) {
			info = new ContactInfo();
			
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			if(name==null) continue;
//			System.out.println(name);
			info.setName(name);
			
			Cursor dataCursor = resolver.query(dataUri, null, "raw_contact_id=?", new String[]{id}, null);
			while(dataCursor.moveToNext()){
				String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
				String phone = dataCursor. getString(dataCursor.getColumnIndex("data1"));
				if(phone==null || type==null) continue;
				
				// 若是电话号码才添加
				if("vnd.android.cursor.item/phone_v2".equals(type)){
					info.setPhone(phone);
				}
				
//				System.out.println(phone);
//				System.out.println(type);
//				System.out.println("-------");
			}
			infos.add(info);
			info = null;
			dataCursor.close();
//			System.out.println("####################3");
		}
		cursor.close();
		return infos;
	}

}
