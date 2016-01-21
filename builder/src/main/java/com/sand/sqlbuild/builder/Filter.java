package com.sand.sqlbuild.builder;


/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午1:11:56
 * @since 1.0.0
 *
 */
public interface Filter <T> extends Setter<T>{
	
	
	String getOperator ();
	

	T[] getValues ();

	ValueType getValueType ();
}
