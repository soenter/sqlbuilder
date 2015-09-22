package com.sand.sqlbuild.impl;

import com.sand.sqlbuild.Field;
import com.sand.sqlbuild.Table;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 上午9:05:15
 * @since 1.0.0
 *
 */
public class FieldFactory {

	private FieldFactory(){}
	
	public static <T> Field<T> create(String tableTame, String fieldName, Class<T> javaType){
		return new FieldImpl<T>(tableTame, fieldName, javaType);
	}

	public static <T> Field<T> create(String fieldName, Class<T> javaType){
		return new FieldImpl<T>(Table.innerAsTableName, fieldName, javaType);
	}
	
	public static <T> Field<T> as(Field<?> field, Class<T> javaType){
		return new FieldImpl<T>(field.getTableName(), field.getName(), javaType);
	}

}
