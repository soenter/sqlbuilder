/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 上午9:05:15
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import com.sand.abacus.util.data.sqlbuild.Field;

/**
 *
 * @ClassName ：FieldFactory
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 上午9:05:15
 * @version 1.0.0
 *
 */
public class FieldFactory {

	private FieldFactory(){}
	
	public static <T> Field<T> create(String tableTame, String fieldName, Class<T> javaType){
		return new FieldImpl<T>(tableTame, fieldName, javaType);
	}
	
	public static <T> Field<T> as(Field<?> field, Class<T> javaType){
		return new FieldImpl<T>(field.getTableName(), field.getName(), javaType);
	}

}
