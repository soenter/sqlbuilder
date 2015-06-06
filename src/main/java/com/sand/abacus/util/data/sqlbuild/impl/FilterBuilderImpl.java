/**
 * Copyright : http://www.sandpay.com.cn/ , 2003-2014
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年5月8日 下午12:45:06
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年5月8日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild.impl;

import java.util.ArrayList;
import java.util.List;

import com.sand.abacus.util.data.sqlbuild.Filter;
import com.sand.abacus.util.data.sqlbuild.FilterBuildResult;
import com.sand.abacus.util.data.sqlbuild.FilterBuilder;

/**
 *
 * @ClassName ：FilterBuilderImpl
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年5月8日 下午12:45:06
 * @version 1.0.0
 *
 */
public class FilterBuilderImpl implements FilterBuilder {
	
	private StringBuilder builder;
	
	private List<Object> params;
	
	FilterBuilderImpl(){
		init();
	}
	
	private void init(){
		builder = new StringBuilder();
		params = new ArrayList<Object>();
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#ls()
	 */
	@Override
	public FilterBuilder ls() {
		builder.append("(");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#rs()
	 */
	@Override
	public FilterBuilder rs() {
		builder.append(")");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#lrs(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	@Override
	public FilterBuilder lrs(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append("(").append(fbr.getSql()).append(")");
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#add(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	@Override
	public FilterBuilder add(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#add(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	@Override
	public FilterBuilder add(Filter<?> filter) {
		builder.append(filter.getField().getFullName()).append(" ").append(filter.getOperator()).append(" ? ");
		params.add(filter.getValue());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#and()
	 */
	@Override
	public FilterBuilder and() {
		builder.append(" and ");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#or()
	 */
	@Override
	public FilterBuilder or() {
		builder.append(" or ");
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#rownum(int)
	 */
	@Override
	public FilterBuilder rownum(int rownum) {
		builder.append("rownum <= ").append(rownum);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#limit(int)
	 */
	@Override
	public FilterBuilder limit(int limit) {
		builder.append("limit = ").append(limit);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#build()
	 */
	@Override
	public FilterBuildResult build() {
		FilterBuildResult fbr = new FilterBuildResultImpl(builder, params);
		init();
		return fbr;
	}
	
}
