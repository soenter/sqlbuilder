package com.sand.abacus.util.data.sqlbuild;

/**
 * insert 或 update 操作设置值
 * @author : sun.mt
 * @date : 2015年5月6日 下午7:01:35
 * @since 1.0.0
 *
 */
public interface Setter <T>{

	Field<T> getField();
	
	T getValue();
	
	Field<T> getFieldValue();
	
	boolean isFieldValue();


}
