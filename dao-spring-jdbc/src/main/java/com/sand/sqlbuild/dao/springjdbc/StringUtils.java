package com.sand.sqlbuild.dao.springjdbc;

/**
 * @author : sun.mt
 * @create : 16-10-28 下午1:44
 * @since : ${VERSION}
 */
class StringUtils {

	public static boolean isNotBlank(String str){
		return str != null && !"".equals(str.trim());
	}


	public static boolean isBlank(String str){
		return str == null || "".equals(str.trim());
	}
}
