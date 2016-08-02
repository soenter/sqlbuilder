package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.Filter;
import com.sand.sqlbuild.builder.FilterBuildResult;
import com.sand.sqlbuild.builder.FilterBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 下午12:45:06
 * @since 1.0.0
 *
 */
@Deprecated
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
	public FilterBuilder ls() {
		builder.append("(");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#rs()
	 */
	public FilterBuilder rs() {
		builder.append(")");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#lrs(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	public FilterBuilder lrs(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append("(").append(fbr.getSql()).append(")");
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#add(com.sand.abacus.util.data.sqlbuild.FilterBuilder)
	 */
	public FilterBuilder add(FilterBuilder filterBuilder) {
		FilterBuildResult fbr = filterBuilder.build();
		builder.append(fbr.getSql());
		params.addAll(fbr.getParameters());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#add(com.sand.abacus.util.data.sqlbuild.Filter)
	 */
	public FilterBuilder add(Filter<?> filter) {
		builder.append(filter.getField().getFullName()).append(" ").append(filter.getOperator()).append(" ? ");
		params.add(filter.getValue());
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#and()
	 */
	public FilterBuilder and() {
		builder.append(" and ");
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#or()
	 */
	public FilterBuilder or() {
		builder.append(" or ");
		return this;
	}
	
	
	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#rownum(int)
	 */
	public FilterBuilder rownum(int rownum) {
		builder.append("rownum <= ").append(rownum);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#limit(int)
	 */
	public FilterBuilder limit(int limit) {
		builder.append("limit = ").append(limit);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.sand.abacus.util.data.sqlbuild.FilterBuilder#build()
	 */
	public FilterBuildResult build() {
		FilterBuildResult fbr = new FilterBuildResultImpl(builder, params);
		init();
		return fbr;
	}
	
}
