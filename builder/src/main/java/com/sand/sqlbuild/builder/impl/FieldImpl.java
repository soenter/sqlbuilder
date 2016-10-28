package com.sand.sqlbuild.builder.impl;


import com.sand.sqlbuild.builder.*;

import java.util.List;

/**
 * @author : sun.mt
 * @date : 2015年5月6日 下午3:43:40
 * @since 1.0.0
 */
public class FieldImpl<T> implements Field<T> {

	private final String tableTame;
	private final String fieldName;

	private final Class<T> javaType;

	private String convertToJavaFieldName;

	private String asName;

	protected FieldImpl (String tableTame, String fieldName, Class<T> javaType, FieldNameConverter fieldNameConverter) {

		if (null == tableTame || null == fieldName || null == javaType) {
			throw new IllegalArgumentException("FieldImpl参数都不能为null");
		}

		this.tableTame = tableTame;
		this.fieldName = fieldName;
		this.javaType = javaType;
		convertToJavaFieldName = fieldNameConverter.convert(fieldName);

		this.asName = FieldUtils.createAsName(tableTame, fieldName);
	}


	public Class<T> getJavaType () {
		return javaType;
	}

	public String getFullName () {
		return tableTame + "." + fieldName;
	}

	public String getAsName () {
		return asName;
	}

	public String getName () {
		return fieldName;
	}

	public String getTableName () {
		return tableTame;
	}

	public String getJavaFieldName () {
		return convertToJavaFieldName;
	}

	public String getReflactGetterName () {
		return "get" + convertToJavaFieldName.substring(0 ,1).toUpperCase() + convertToJavaFieldName.substring(1);
	}

	public String getReflactSetterName () {
		return "set" + convertToJavaFieldName.substring(0 ,1).toUpperCase() + convertToJavaFieldName.substring(1);
	}


	public Filter<T> eq (T value) {
		return new FilterImpl<T>(this, "=", value);
	}

	public Filter<?> eq (Fieldable field) {
		return new FilterImpl<T>(this, " = ", field);
	}

	public Filter<T> ne (T value) {
		return new FilterImpl<T>(this, "<>", value);
	}

	public Filter<?> ne (Fieldable field) {
		return new FilterImpl<T>(this, " <> ", field);
	}

	public Filter<T> gt (T value) {
		return new FilterImpl<T>(this, ">", value);
	}

	public Filter<?> gt (Fieldable field) {
		return new FilterImpl<T>(this, " > ", field);
	}

	public Filter<T> lt (T value) {
		return new FilterImpl<T>(this, "<", value);
	}

	public Filter<?> lt (Fieldable field) {
		return new FilterImpl<T>(this, " < ", field);
	}


	public Filter<T> gte (T value) {
		return new FilterImpl<T>(this, ">=", value);
	}

	public Filter<?> gte (Fieldable field) {
		return new FilterImpl<T>(this, " >= ", field);
	}

	public Filter<T> lte (T value) {
		return new FilterImpl<T>(this, "<=", value);
	}

	public Filter<?> lte (Fieldable field) {
		return new FilterImpl<T>(this, " <= ", field);
	}

	@SuppressWarnings("unchecked")
	public Filter<T> lk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T) ("%" + value + "%"));
	}

	private void checkLike (T value) {
		if (value == null) {
			throw new IllegalArgumentException("like 不能匹配null");
		}
		if (!(value instanceof String)) {
			throw new IllegalArgumentException("like 查询必须是字符串类型");
		}
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#llk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> llk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T) (value + "%"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sand.abacus.util.data.sqlbuild.Field#in(List<java.lang.Object>)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> in (List<T> list) {
		return new FilterImpl<T>(this, "in", (T[]) list.toArray(), ValueType.MULTI_VALUES);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#in(java.lang.Object[])
	 */
	public Filter<T> in (T[] values) {
		return new FilterImpl<T>(this, "in", values, ValueType.MULTI_VALUES);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nin(java.lang.Object[])
	 */
	public Filter<T> nin (T[] values) {
		return new FilterImpl<T>(this, "not in", values, ValueType.MULTI_VALUES);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#rlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> rlk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "like ", (T) ("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nlk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T) ("%" + value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nllk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nllk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T) (value + "%"));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#nrlk(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Filter<T> nrlk (T value) {
		checkLike(value);
		return new FilterImpl<T>(this, "not like ", (T) ("%" + value));
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#st(java.lang.Object)
	 */
	public Setter<T> st (T value) {
		return new SetterImpl<T>(this, value);
	}

	public Setter<T> st (Fieldable field) {
		return new SetterImpl<T>(this, field);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#bta(java.lang.Object, java.lang.Object)
	 */
	public Filter<T> bta (T value1, T value2) {
		@SuppressWarnings("unchecked")
		T[] values = (T[]) new Object[]{value1, value2};

		return new FilterImpl<T>(
				this, " between ? and ? ",
				values,
				ValueType.TWIN_VALUES);
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#asc()
	 */
	public Order asc () {
		return new OrderImpl(this, Order.Type.asc);
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Field#desc()
	 */
	public Order desc () {
		return new OrderImpl(this, Order.Type.desc);
	}


	public Jointer plus (Object value) {
		return join(" + ", value);
	}

	public Jointer subtract (Object value) {
		return join(" - ", value);
	}

	public Jointer multiply (Object value) {
		return join(" * ", value);
	}

	public Jointer divide (Object value) {
		return join(" / ", value);
	}

	public Jointer plus (Jointer value) {
		return join(" + ", value);
	}

	public Jointer subtract (Jointer value) {
		return join(" - ", value);
	}

	public Jointer multiply (Jointer value) {
		return join(" * ", value);
	}

	public Jointer divide (Jointer value) {
		return join(" / ", value);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString () {
		return getFullName();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof FieldImpl) {
			return getAsName().equals(((FieldImpl<T>) obj).getAsName());
		}
		return super.equals(obj);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode () {
		return getAsName().hashCode();
	}

	public Filter<?> epv () {
		return new FilterImpl(this, "=", ValueType.EMPTY_VALUE);
	}

	public Filter<?> epv (String operator) {
		if (operator == null || "".equals((operator = operator.trim()))) {
			throw new IllegalArgumentException("epv 方法的 operator 暂时不能为 null 或空");
		} else if ("like".equalsIgnoreCase(operator)) {
			throw new IllegalArgumentException("epv 方法的 operator 暂时不支持 like 语法");
		} else if ("in".equalsIgnoreCase(operator)) {
			throw new IllegalArgumentException("epv 方法的 operator 暂时不支持 in 语法");
		}

		return new FilterImpl(this, operator, ValueType.EMPTY_VALUE);
	}

	public Filter<?> isNull () {
		return new FilterImpl(this, " is null ", ValueType.NO_VALUE);
	}

	public Filter<?> isNotNull () {
		return new FilterImpl(this, " is not null ", ValueType.NO_VALUE);
	}

	public Jointer join (String operator, Object value) {

		JointerImpl fieldJointer = new JointerImpl(null, null, this);
		if(value instanceof Jointer){
			return FieldUtils.joinJointer(fieldJointer, operator, (Jointer)value);
		}
		return fieldJointer.setNext(new JointerImpl(fieldJointer, operator, value));
	}

	public Jointer join (String operator) {
		return join(operator, (Object) null);
	}

	public Jointer join (String operator, Jointer value) {
		//加上双括号 "()"
		return join(operator).ds(value);
	}

	public Fieldable as (String alias, Class<?> asJavaType) {
		Field<?> asField = FieldFactory.create(alias, javaType);
		return join(" as ", asField);
	}

	public Fieldable as (Field<T> alias) {
		return join(" as ", alias);
	}

	public Jointer plus () {
		return plus((Object) null);
	}

	public Jointer subtract () {
		return subtract((Object) null);
	}

	public Jointer multiply () {
		return multiply((Object) null);
	}

	public Jointer divide () {
		return divide((Object) null);
	}


}
