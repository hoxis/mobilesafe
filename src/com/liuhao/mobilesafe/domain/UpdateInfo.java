package com.liuhao.mobilesafe.domain;

/**
 * @author liuhao
 * 升级信息
 */
public class UpdateInfo {

	String version;
	String description;
	String apkurl;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApkurl() {
		return apkurl;
	}

	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}

}
