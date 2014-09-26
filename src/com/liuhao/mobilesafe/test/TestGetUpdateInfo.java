package com.liuhao.mobilesafe.test;

import junit.framework.Assert;

import com.liuhao.mobilesafe.R;
import com.liuhao.mobilesafe.domain.UpdateInfo;
import com.liuhao.mobilesafe.engine.UpdateInfoService;

import android.test.AndroidTestCase;

public class TestGetUpdateInfo extends AndroidTestCase {

	public void testGetInfo() throws Exception{
		UpdateInfoService service = new UpdateInfoService(getContext());
		UpdateInfo info = service.getUpdateInfo(R.string.updateurl);
		
		Assert.assertEquals("2.0", info.getVersion());
	}
	
}
