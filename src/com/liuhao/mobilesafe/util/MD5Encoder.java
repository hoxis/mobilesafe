package com.liuhao.mobilesafe.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {

	public static String encode(String pwd) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(pwd.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : bytes) {
				String str = Integer.toHexString(0xff & b);// byte是八位字节存储的，转化为16进制数，直接与11111111相与
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("不存在加密算法");
		}
	}
}
