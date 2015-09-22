package com.sand.abacus.util.data.sqlbuild;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 下午3:40:24
 * @since 1.0.0
 *
 */
public interface Order {

	Field<?> getField();
	
	Type getType();

	enum Type{
		/**正序*/
		asc,
		/**倒序*/
		desc
	}
}
