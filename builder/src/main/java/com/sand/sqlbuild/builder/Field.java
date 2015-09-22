package com.sand.sqlbuild.builder;

import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午12:53:07
 * @since 1.0.0
 *
 */
public interface Field<T>{
	
	Class<T> getJavaType ();
	
	String getFullName ();
	
	String getAsName ();
	
	String getName ();
	
	String getTableName ();

	/**
	 * 在一个范围内 (in)
	 *
	 * @param list
	 * @return
	 */
	Filter<T> in (List<T> list);

	/**
	 * 在一个范围内 (in)
	 *
	 * @param list
	 * @return
	 */
	Filter<T> in (@SuppressWarnings("unchecked") T... values);

	/**
	 * 不在一个范围内 (not in)
	 * 
	 * @param values
	 * @return
	 */
	Filter<T> nin (T[] values);
	
	/**
	 * 等于（=）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> eq (T value);
	
	/**
	 * 等于（=）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> eq (Field<T> field);
	
	/**
	 * 不等于（!=, <>）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> ne (T value);

	/**
	 * 大于（>）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> gt (T value);

	/**
	 * 小于（<）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> lt (T value);

	/**
	 * 大于等于（>=）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> gte (T value);

	/**
	 * 小于等于（<=）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> lte (T value);
	
	
	/**
	 * 类似模糊查询（like '%string%'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> lk (T value);

	/**
	 * 左类似（like 'string%'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> llk (T value);

	/**
	 * 右类似（like '%string'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> rlk (T value);
	
	/**
	 * 非类似模糊查询（not like '%string%'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> nlk (T value);

	/**
	 * 非左类似（not like 'string%'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> nllk (T value);

	/**
	 * 非右类似（not like '%string'）
	 * 
	 * @param value
	 * @return
	 */
	Filter<T> nrlk (T value);
	
	
	/**
	 * 设置值 （field = value）
	 * 
	 * @param value
	 * @return
	 */
	Setter<T> st (T value);
	
	
	/**
	 * 设置值 （field = value）
	 * 
	 * @param field
	 * @return
	 */
	Setter<T> st (Field<T> field);
	
	/**
	 * 在...之间（between ? and ？）
	 * 
	 * @param value2
	 * @return
	 */
	Filter<T> bta (T value1, T value2);

	/**
	 * 正序，默认
	 * 
	 * @return
	 */
	Order asc ();
	
	/**
	 * 倒序
	 * 
	 * @return
	 */
	Order desc ();

	
	/**
	 * 加
	 * this + value
	 * 
	 * @return
	 */
	Field<T> plus (T value);

	
	/**
	 * 减
	 * this - value
	 * 
	 * @return
	 */
	Field<T> subtract (T value);
	
	/**
	 * 获取操作符
	 * 
	 * @return
	 */
	String getOperator ();
	
	/**
	 * 获取操作值
	 * 
	 * @return
	 */
	T getOperValue ();
	
	/**
	 * 获取操作符
	 * 
	 * @return
	 */
	boolean hasOperator ();
}
