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
	
	Type getType ();
	
	enum Type{
		/**单值:正常比较（默认）*/
		ONE_VALUE,
		/**双值:between ? and ?*/
		TWIN_VALUES,
		/**多值（两个以上）:in*/
		MULTI_VALUES,
		/**没有值*/
		NO_VALUE
	}
}
