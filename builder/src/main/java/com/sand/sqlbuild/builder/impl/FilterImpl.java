package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Filter;

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
	
	private Type type;
	
	FilterImpl(Field<T> field, String operator, T value) {
		super(field, value);
		this.operator = operator;
		this.type = Type.ONE;
	}
	
	FilterImpl(Field<T> field, String operator, Field<T> value) {
		super(field, value);
		this.operator = operator;
		this.type = Type.ONE;
	}
	
	FilterImpl(Field<T> field, String operator, T[] values, Type type) {
		super(field, (T)null);
		this.operator = operator;
		this.values = values;
		this.type = type;
	}


	FilterImpl(Field<T> field, String operator) {
		super(field, (T)null);
		this.operator = operator;
		this.type = Type.ONE;
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
	 * @see com.sand.abacus.util.data.sqlbuild.Filter#getType()
	 */
	public Type getType() {
		return type;
	}

	@Override
	public boolean isEmptyValue () {
		return super.isEmptyValue() && (values == null || values.length == 0);
	}
}
