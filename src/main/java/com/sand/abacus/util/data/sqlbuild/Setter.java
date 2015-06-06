/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午7:01:35
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

/**
 * insert 或 update 操作设置值
 * @ClassName ：Setter
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 下午7:01:35
 * @version 1.0.0
 *
 */
public interface Setter <T>{

	public Field<T> getField();
	
	public T getValue();
	
	public Field<T> getFieldValue();
	
	public boolean isFieldValue();


}
