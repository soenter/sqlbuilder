/**
 * Copyright : http://www.sandpay.com.cn/ , 2007-2015
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年6月2日 下午5:20:21
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年6月2日        Initailized
 */
package com.sand.sqlbuild.impl;

import com.sand.sqlbuild.PagingBuilder;

/**
 *
 * @ClassName ：PagingBuilderFactory
 * @Description : 
 * @author : sun.mt
 * @date : 2015年6月2日 下午5:20:21
 * @since 1.0.0
 *
 */
public class PagingBuilderFactory {

	public static PagingBuilder create(){
		return new PagingBuilderImpl();
	}
}
