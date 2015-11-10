package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午7:06:26
 * @since 1.0.0
 *
 */
public class SetterImpl <T> implements Setter<T> {

	private StringBuilder sql = new StringBuilder();

	private List<Object> params = new ArrayList<Object>();

	private Field<T> field;
	
	private T value;
	
	private Field<T> fieldValue;
	
	public SetterImpl(Field<T> field, T value){
		this.field = field;
		this.value = value;

		sql.append(" ").append(field.getSql()).append(" = ?");
		params.addAll(field.getParameters());
		params.add(value);
	}
	
	public SetterImpl(Field<T> field, Field<T> value){
		this.field = field;
		this.fieldValue = value;

		sql.append(" ").append(field.getSql()).append(" = ").append(value.getSql());
		params.addAll(field.getParameters());
		params.addAll(value.getParameters());
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

	public Field<T> getFieldValue() {
		return fieldValue;
	}

	public boolean isFieldValue() {
		return fieldValue != null;
	}

	public String getSql () {
		return sql.toString();
	}

	public List<Object> getParameters () {
		return params;
	}
}
