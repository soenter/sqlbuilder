package com.sand.sqlbuild.builder.impl;


import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Filter;
import com.sand.sqlbuild.builder.Order;
import com.sand.sqlbuild.builder.Setter;

import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午3:43:40
 * @since 1.0.0
 *
 */
public class FieldImpl<T> implements Field<T> {

	private final String tableTame;
	private final String fieldName;
	
	private final Class<T> javaType;
	
	private String operator;
	
	private T operValue;
	
	protected FieldImpl(String tableTame, String fieldName, Class<T> javaType){
		
		if(null == tableTame || null == fieldName || null== javaType){
			throw new IllegalArgumentException("FieldImpl参数都不能为null");
		}
		
		this.tableTame = tableTame;
		this.fieldName = fieldName;
		this.javaType = javaType;
		
	}
	
	protected FieldImpl(String tableTame, String fieldName, Class<T> javaType, String operator, T operValue){
		
		if(null == tableTame || null == fieldName || null== javaType){
			throw new IllegalArgumentException("FieldImpl参数都不能为null");
		}
		
		this.tableTame = tableTame;
		this.fieldName = fieldName;
		this.javaType = javaType;
		
		this.operator = operator;
		this.operValue = operValue;
		
	}
	

	public Class<T> getJavaType() {
		return javaType;
	}


	public String getFullName() {
		return tableTame + "." + fieldName;
	}

	public String getAsName() {
		String name = tableTame + "_" + fieldName;
		//oracle 列名称长度不能大于30 FIXME 汉字未处理
		if(name.length() > 30){
			return name.substring(name.length() - 30);
		}
		return name;
	}


	public String getName() {
		return fieldName;
	}
	

	public String getTableName() {
		return tableTame;
	}



	public Filter<T> eq(T value) {
		return new FilterImpl<T>(this, "=", value);
	}

	public Filter<T> eq(Field<T> field) {
		return new FilterImpl<T>(this, "=", field);
	}

	public Filter<T> ne(T value) {
		return new FilterImpl<T>(this, "<>", value);
	}

	public Filter<T> gt(T value) {
		return new FilterImpl<T>(this, ">", value);
	}

	public Filter<T> lt(T value) {
		return new FilterImpl<T>(this, "<", value);
	}

	public Filter<T> gte(T value) {
		return new FilterImpl<T>(this, ">=", value);
	}

	public Filter<T> lte(T value) {
		return new FilterImpl<T>(this, "<=", value);
	}

	@SuppressWarnings("unchecked")
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
	public Filter<T> in(List<T> list) {
		return new FilterImpl<T>(this, "in", (T[])list.toArray(), Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#in(java.lang.Object[])
	 */
	public Filter<T> in(T[] values) {
		return new FilterImpl<T>(this, "in", values, Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nin(java.lang.Object[])
	 */
	public Filter<T> nin(T[] values) {
		return new FilterImpl<T>(this, "not in", values, Filter.Type.MULTI);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#rlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> rlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T)("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)("%" + value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nllk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nllk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)(value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nrlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nrlk(T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T)("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#st(java.lang.Object)
	 */
	public Setter<T> st(T value) {
		return new SetterImpl<T>(this, value);
	}

	public Setter<T> st(Field<T> field) {
		return new SetterImpl<T>(this, field);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#bta(java.lang.Object, java.lang.Object)
	 */
	public Filter<T> bta(T value1, T value2) {
		@SuppressWarnings("unchecked")
		T[] values = (T[]) new Object[]{value1, value2};
		
		return new FilterImpl<T>(
				this, " between ? and ? ", 
				values, 
				Filter.Type.TWIN);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#asc()
	 */
	public Order asc() {
		return new OrderImpl(this, Order.Type.asc);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#desc()
	 */
	public Order desc() {
		return new OrderImpl(this, Order.Type.desc);
	}
	

	public Field<T> plus(T value) {
		return new FieldImpl<T>(tableTame, fieldName, javaType, "+", value);
	}


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

	public String getOperator() {
		return this.operator;
	}

	public T getOperValue() {
		return this.operValue;
	}

	public boolean hasOperator() {
		return this.operator != null && !"".equals(this.operator);
	}

	public Field<T> join (Field<?> field, String with) {
		return null;
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
