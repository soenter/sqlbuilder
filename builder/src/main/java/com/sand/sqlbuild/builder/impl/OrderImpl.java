package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.Field;
import com.sand.sqlbuild.builder.Order;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 下午3:42:14
 * @since 1.0.0
 *
 */
public class OrderImpl implements Order {
	
	private final Field<?> field;
	
	private final Type type;

	OrderImpl(Field<?> field, Type type) {
		super();
		this.field = field;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Order#getField()
	 */
	public Field<?> getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Order#getType()
	 */
	public Type getType() {
		return type;
	}

}
