/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 下午3:40:24
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

/**
 *
 * @ClassName ：Order
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 下午3:40:24
 * @version 1.0.0
 *
 */
public interface Order {

	public Field<?> getField();
	
	public Type getType();

	public enum Type{
		/**正序*/
		asc,
		/**倒序*/
		desc
	}
}
