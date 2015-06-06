/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-dao-base
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午1:11:56
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;


/**
 *
 * @ClassName ：IFilter
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月6日 下午1:11:56
 * @version 1.0.0
 *
 */
public interface Filter <T> extends Setter<T>{
	
	
	public String getOperator();
	

	public T[] getValues();
	
	public Type getType();
	
	public enum Type{
		/**单值:正常比较（默认）*/
		ONE,
		/**双值:between ? and ?*/
		TWIN,
		/**多值（两个以上）:in*/
		MULTI
	}
}
