/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月7日 上午9:52:10
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月7日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import java.util.List;

import com.sand.abacus.util.data.sqlbuild.BuildResult;
import com.sand.abacus.util.data.sqlbuild.Field;

/**
 *
 * @ClassName ：BuildResultImpl
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月7日 上午9:52:10
 * @version 1.0.0
 *
 */
public class BuildResultImpl implements BuildResult {

	private final StringBuilder builder;
	
	private final List<Object> params;
	
	private List<Field<?>> selectFields;
	

	public BuildResultImpl(StringBuilder builder, List<Object> params, List<Field<?>> selectFields) {
		super();
		this.builder = builder;
		this.params = params;
		this.selectFields = selectFields;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.BuildResult#getSql()
	 */
	@Override
	public String getSql() {
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.BuildResult#getParameters()
	 */
	@Override
	public List<Object> getParameters() {
		return params;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.BuildResult#getSelectFields()
	 */
	@Override
	public List<Field<?>> getSelectFields() {
		if(selectFields == null || selectFields.size() == 0){
			throw new IllegalArgumentException("查询字段不能为空");
		}
		return selectFields;
	}

}
