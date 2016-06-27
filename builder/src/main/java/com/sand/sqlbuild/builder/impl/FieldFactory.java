package com.sand.sqlbuild.builder.impl;


import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.FieldNameConverter;
import com.sand.sqlbuild.builder.Table;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 上午9:05:15
 * @since 1.0.0
 *
 */
public class FieldFactory {

	private static final FieldNameConverter DEFAULT_CONVERTER = new UnderlineAndHumpFieldNameConverter();

	private FieldFactory(){}
	
	public static <T> Field<T> create(String tableTame, String fieldName, Class<T> javaType){
		return new FieldImpl<T>(tableTame, fieldName, javaType, DEFAULT_CONVERTER);
	}

	public static <T> Field<T> create(String fieldName, Class<T> javaType){
		return new FieldImpl<T>(Table.innerAsTableName, fieldName, javaType, DEFAULT_CONVERTER);
	}
	
	public static <T> Field<T> as(Field<?> field, Class<T> javaType){
		return new FieldImpl<T>(field.getTableName(), field.getName(), javaType, DEFAULT_CONVERTER);
	}

}
