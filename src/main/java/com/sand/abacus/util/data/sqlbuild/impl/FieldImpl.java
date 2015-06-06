/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午3:43:40
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import java.util.List;

import com.sand.abacus.util.data.sqlbuild.Field;
import com.sand.abacus.util.data.sqlbuild.Filter;
import com.sand.abacus.util.data.sqlbuild.Order;
import com.sand.abacus.util.data.sqlbuild.Order.Type;
import com.sand.abacus.util.data.sqlbuild.Setter;

/**
 *
 * @ClassName ：FieldImpl
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 下午3:43:40
 * @version 1.0.0
 *
 */
public class FieldImpl<T> implements Field<T>{

	private final String tableTame;
	private final String fieldName;
	
	private final Class<T> javaType;
	
	private String operator;
	
	private T operValue;
	
	protected FieldImpl(String tableTame, String fieldName, Class<T> javaType){
		
		if(null == tableTame || null == tableTame || null== javaType){
			throw new IllegalArgumentException("FieldImpl参数都不能为null");
		}
		
		this.tableTame = tableTame;
		this.fieldName = fieldName;
		this.javaType = javaType;
		
	}
	
	protected FieldImpl(String tableTame, String fieldName, Class<T> javaType, String operator, T operValue){
		
		if(null == tableTame || null == tableTame || null== javaType){
			throw new IllegalArgumentException("FieldImpl参数都不能为null");
		}
		
		this.tableTame = tableTame;
		this.fieldName = fieldName;
		this.javaType = javaType;
		
		this.operator = operator;
		this.operValue = operValue;
		
	}
	

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#getJavaType()
	 */
	@Override
	public Class<T> getJavaType() {
		return javaType;
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#getFullName()
	 */
	@Override
	public String getFullName() {
		return tableTame + "." + fieldName;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#getAsName()
	 */
	@Override
	public String getAsName() {
		String name = tableTame + "_" + fieldName;
		//oracle 列名称长度不能大于30 FIXME 汉字未处理
		if(name.length() > 30){
			return name.substring(name.length() - 30);
		}
		return name;
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#getName()
	 */
	@Override
	public String getName() {
		return fieldName;
	}
	

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#getTableName()
	 */
	@Override
	public String getTableName() {
		return tableTame;
	}



	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#eq(java.lang.Object)
	 */
	@Override
	public Filter<T> eq(T value) {
		return new FilterImpl<T>(this, "=", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#eq(com.sand.abacus.util.data.sqlbuild.Field)
	 */
	@Override
	public Filter<T> eq(Field<T> field) {
		return new FilterImpl<T>(this, "=", field);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#ne(java.lang.Object)
	 */
	@Override
	public Filter<T> ne(T value) {
		return new FilterImpl<T>(this, "<>", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#gt(java.lang.Object)
	 */
	@Override
	public Filter<T> gt(T value) {
		return new FilterImpl<T>(this, ">", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#lt(java.lang.Object)
	 */
	@Override
	public Filter<T> lt(T value) {
		return new FilterImpl<T>(this, "<", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#gte(java.lang.Object)
	 */
	@Override
	public Filter<T> gte(T value) {
		return new FilterImpl<T>(this, ">=", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#lte(java.lang.Object)
	 */
	@Override
	public Filter<T> lte(T value) {
		return new FilterImpl<T>(this, "<=", value);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#lk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> lk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T)("%" + value + "%"));
	}
	
	private void checkLike(T value){
		if(value == null){
			throw new IllegalArgumentException("like 不能匹配null");
		}
		if(!(value instanceof String)){
			throw new IllegalArgumentException("like 查询必须是字符串类型");
		}
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#llk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> llk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T)(value + "%"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sand.abacus.util.data.sqlbuild.Field#in(List<java.lang.Object>)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> in(List<T> list) {
		return new FilterImpl<T>(this, "in", (T[])list.toArray(), com.sand.abacus.util.data.sqlbuild.Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#in(java.lang.Object[])
	 */
	@Override
	public Filter<T> in(T[] values) {
		return new FilterImpl<T>(this, "in", values, com.sand.abacus.util.data.sqlbuild.Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nin(java.lang.Object[])
	 */
	@Override
	public Filter<T> nin(T[] values) {
		return new FilterImpl<T>(this, "not in", values, com.sand.abacus.util.data.sqlbuild.Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#rlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> rlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T)("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> nlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)("%" + value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nllk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> nllk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)(value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nrlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Filter<T> nrlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#st(java.lang.Object)
	 */
	@Override
	public Setter<T> st(T value) {
		return new SetterImpl<T>(this, value);
	}

	@Override
	public Setter<T> st(Field<T> field) {
		return new SetterImpl<T>(this, field);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#bta(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter<T> bta(T value1, T value2) {
		@SuppressWarnings("unchecked")
		T[] values = (T[]) new Object[]{value1, value2};
		
		return new FilterImpl<T>(
				this, " between ? and ? ", 
				values, 
				com.sand.abacus.util.data.sqlbuild.Filter.Type.TWIN);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#asc()
	 */
	@Override
	public Order asc() {
		return new OrderImpl(this, Type.asc);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#desc()
	 */
	@Override
	public Order desc() {
		return new OrderImpl(this, Type.desc);
	}
	

	@Override
	public Field<T> plus(T value) {
		return new FieldImpl<T>(tableTame, fieldName, javaType, "+", value);
	}


	@Override
	public Field<T> subtract(T value) {
		return new FieldImpl<T>(tableTame, fieldName, javaType, "-", value);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullName();
	}

	@Override
	public String getOperator() {
		return this.operator;
	}

	@Override
	public T getOperValue() {
		return this.operValue;
	}

	@Override
	public boolean hasOperator() {
		return this.operator != null && !"".equals(this.operator);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof FieldImpl){
			return getAsName().equals(((FieldImpl<T>)obj).getAsName());
		}
		return super.equals(obj);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getAsName().hashCode();
	}
}
