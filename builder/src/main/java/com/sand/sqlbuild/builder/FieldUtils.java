package com.sand.sqlbuild.builder;

/**
 *
 * @author : sun.mt@sand.com.cn
 * @date : 2015/6/8 14:34
 * @since 1.0.0
 *
 */
public class FieldUtils {

	/**
	 * 根据别名获取全名
	 * @param aliasName
	 * @return
	 */
	public static String getFullNameByAliasName(String aliasName){
		return Table.innerAsTableName + "." + aliasName;
	}
}
