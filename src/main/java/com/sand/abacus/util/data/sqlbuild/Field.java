/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-dao-base
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午12:53:07
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

import java.util.List;

/**
 *
 * @ClassName ：IField
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 下午12:53:07
 * @version 1.0.0
 *
 */
public interface Field<T>{
	
	public Class<T> getJavaType();
	
	public String getFullName();
	
	public String getAsName();
	
	public String getName();
	
	public String getTableName();

	/**
	 * 在一个范围内 (in)
	 *
	 * @param list
	 * @return
	 */
	public Filter<T> in(List<T> list);

	/**
	 * 在一个范围内 (in)
	 *
	 * @param list
	 * @return
	 */
	public Filter<T> in(@SuppressWarnings("unchecked") T... values);

	/**
	 * 不在一个范围内 (not in)
	 * 
	 * @param values
	 * @return
	 */
	public Filter<T> nin(T[] values);
	
	/**
	 * 等于（=）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> eq(T value);
	
	/**
	 * 等于（=）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> eq(Field<T> field);
	
	/**
	 * 不等于（!=, <>）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> ne(T value);

	/**
	 * 大于（>）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> gt(T value);

	/**
	 * 小于（<）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> lt(T value);

	/**
	 * 大于等于（>=）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> gte(T value);

	/**
	 * 小于等于（<=）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> lte(T value);
	
	
	/**
	 * 类似模糊查询（like '%string%'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> lk(T value);

	/**
	 * 左类似（like 'string%'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> llk(T value);

	/**
	 * 右类似（like '%string'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> rlk(T value);
	
	/**
	 * 非类似模糊查询（not like '%string%'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> nlk(T value);

	/**
	 * 非左类似（not like 'string%'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> nllk(T value);

	/**
	 * 非右类似（not like '%string'）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> nrlk(T value);
	
	
	/**
	 * 设置值 （field = value）
	 * 
	 * @param value
	 * @return
	 */
	public Setter<T> st(T value);
	
	
	/**
	 * 设置值 （field = value）
	 * 
	 * @param value
	 * @return
	 */
	public Setter<T> st(Field<T> field);
	
	/**
	 * 在...之间（between ? and ？）
	 * 
	 * @param value
	 * @return
	 */
	public Filter<T> bta(T value1, T value2);

	/**
	 * 正序，默认
	 * 
	 * @return
	 */
	public Order asc();
	
	/**
	 * 倒序
	 * 
	 * @return
	 */
	public Order desc();

	
	/**
	 * 加
	 * this + value
	 * 
	 * @return
	 */
	public Field<T> plus(T value);

	
	/**
	 * 减
	 * this - value
	 * 
	 * @return
	 */
	public Field<T> subtract(T value);
	
	/**
	 * 获取操作符
	 * 
	 * @return
	 */
	public String getOperator();
	
	/**
	 * 获取操作值
	 * 
	 * @return
	 */
	public T getOperValue();
	
	/**
	 * 获取操作符
	 * 
	 * @return
	 */
	public boolean hasOperator();
}
