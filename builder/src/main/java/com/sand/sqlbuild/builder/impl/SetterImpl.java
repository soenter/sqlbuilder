/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午7:06:26
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

/**
 *
 * @ClassName ：SetterImpl
 * @Description : 
 * @author : sun.mt
 * @date : 2015年5月6日 下午7:06:26
 * @since 1.0.0
 *
 */
public class SetterImpl <T> implements Setter<T> {


	private Field<T> field;
	
	private T value;
	
	private Fieldable fieldValue;

	protected ValueType valueType;
	
	public SetterImpl(Field<T> field, T value){
		this.field = field;
		this.value = value;
		valueType = ValueType.ONE_VALUE;
	}
	
	public SetterImpl(Field<T> field, Fieldable value){
		this.field = field;
		this.fieldValue = value;
		valueType = ValueType.FIELD_VALUE;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Setter#getField()
	 */
	public Field<T> getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Setter#getValue()
	 */
	public T getValue() {
		return value;
	}

	public Fieldable getFieldable () {
		return fieldValue;
	}

	public boolean isFieldValue() {
		return valueType == ValueType.FIELD_VALUE;
	}

	public boolean isEmptyValue () {
		return valueType == ValueType.NO_VALUE;
	}

}
