/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 下午12:34:54
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

import org.junit.Test;

import com.sand.abacus.util.data.sqlbuild.impl.BuilderFactory;

/**
 *
 * @ClassName ：FilterGroupTest
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 下午12:34:54
 * @version 1.0.0
 *
 */
public class FilterGroupTest {

	@Test
	public void test_interface(){
		FilterBuilder fg = BuilderFactory.createFilterBuilder();
		
		fg.ls().rs().and().ls().rs().or().ls().rs();
		
		System.out.println(fg.build().getSql());
		
		FilterBuilder fg2 = BuilderFactory.createFilterBuilder();
		
		fg2.ls().add(fg).rs().or().lrs(fg);
		
		System.out.println(fg2.build().getSql());
	}
}
