/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月7日 下午2:59:05
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月7日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import com.sand.abacus.util.data.sqlbuild.Builder;
import com.sand.abacus.util.data.sqlbuild.FilterBuilder;

/**
 *
 * @ClassName ：BuilderFactory
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月7日 下午2:59:05
 * @version 1.0.0
 *
 */
public class BuilderFactory {

	private BuilderFactory(){}
	
	public static Builder create(){
		return new BuilderImpl();
	}
	
	public static FilterBuilder createFilterBuilder(){
		return new FilterBuilderImpl();
	}
	
}
