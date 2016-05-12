package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午5:05:12
 * @since 1.0.0
 *
 */
public class FilterImpl<T> extends SetterImpl<T> implements Filter<T> {

	
	private String operator;
	
	private T[] values;
	

	FilterImpl(Field<T> field, String operator, T value) {
		super(field, value);
		this.operator = operator;
	}
	
	FilterImpl(Field<T> field, String operator, Fieldable fieldable) {
		super(field, fieldable);
		this.operator = operator;
	}
	
	FilterImpl(Field<T> field, String operator, T[] values, ValueType valueType) {
		super(field, (T)null);
		this.operator = operator;
		this.values = values;
		this.valueType = valueType;
	}


	FilterImpl(Field<T> field, String operator, ValueType valueType) {
		super(field, (T)null);
		this.operator = operator;
		this.valueType = valueType;
	}


	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Filter#getOperator()
	 */
	public String getOperator() {
		return operator;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Filter#getValues()
	 */
	public T[] getValues() {
		return values;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Filter#getValueType()
	 */
	public ValueType getValueType () {
		return valueType;
	}

	@Override
	public boolean isEmptyValue () {
		return valueType == ValueType.EMPTY_VALUE;
	}
}
