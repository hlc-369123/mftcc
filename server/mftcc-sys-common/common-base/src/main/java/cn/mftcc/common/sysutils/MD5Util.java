/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.sysutils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具类
 * @author Administrator
 *
 */
public class MD5Util {
	/**
	 * 将msg字符串采用MD5算法处理，返回一个Sring结果
	 * @param msg	明文
	 * @return		密文
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String md5(String msg) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		//原始信息input
		byte[] input = msg.getBytes();
		//加密信息output
		byte[] output = md.digest(input);
		//将加密内容output转成String字符串
		//为解决乱码问题采用BASE64转换,需要引一个工具包commons codec
		String result = Base64Util.encoder(output);
		return result;
	}
}
