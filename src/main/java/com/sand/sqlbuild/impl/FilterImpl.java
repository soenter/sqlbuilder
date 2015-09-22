/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午5:05:12
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.sqlbuild.impl;

import com.sand.sqlbuild.Field;
import com.sand.sqlbuild.Filter;

/**
 *
 * @ClassName ：FilterImpl
 * @Description : 
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

}