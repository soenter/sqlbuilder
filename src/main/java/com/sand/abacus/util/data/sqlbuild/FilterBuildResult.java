/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 下午12:52:14
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

import java.util.List;

/**
 *
 * @ClassName ：FilterBuildResult
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 下午12:52:14
 * @version 1.0.0
 *
 */
public interface FilterBuildResult {

	public String getSql();
	
	public List<Object> getParameters();
}
