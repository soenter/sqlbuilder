/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-dao-base
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月6日 下午12:54:52
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月6日        Initailized
 */
package com.sand.sqlbuild;

/**
 *
 * @ClassName ：ITable
 * @Description : 
 * @author : sun.mt
 * @date : 2015年5月6日 下午12:54:52
 * @since 1.0.0
 *
 */
public interface Table {

	/**内部表别名*/
	String innerAsTableName = "inner_as_table_name_9999";

	String getName();


}
