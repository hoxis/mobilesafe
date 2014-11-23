package com.liuhao.mobilesafe.test;

import java.util.List;

import com.liuhao.mobilesafe.domain.ContactInfo;
import com.liuhao.mobilesafe.engine.ContactInfoService;

import android.test.AndroidTestCase;

public class TestGetContactInfo extends AndroidTestCase {

	public void testGetContacts() {
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> infos = service.getContactInfos();
		for(ContactInfo info : infos){
			System.out.print(info.getName() + "---");
			System.out.print(info.getPhone());
			System.out.println();
		}
	}
}
