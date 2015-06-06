/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 下午3:42:14
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import com.sand.abacus.util.data.sqlbuild.Field;
import com.sand.abacus.util.data.sqlbuild.Order;

/**
 *
 * @ClassName ：OrderImpl
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 下午3:42:14
 * @version 1.0.0
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
	@Override
	public Field<?> getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.Order#getType()
	 */
	@Override
	public Type getType() {
		return type;
	}

}
