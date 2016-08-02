package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午3:53:10
 * @since 1.0.0
 *
 */
public class PagingBuilderImpl extends BaseBuilderImpl<PagingBuilder> implements PagingBuilder {
	
	private PagingBuilderImpl selectBuilder;
	private PagingBuilderImpl fromBuilder;
	private PagingBuilderImpl whereBuilder;
	private PagingBuilderImpl orderBuilder;
	private PagingBuilderImpl groupbyBuilder;

	private PagingBuilderImpl mainBuilder;


	private BuildResult selectBr;
	private BuildResult fromBr;
	private BuildResult whereBr;
	private BuildResult orderBr;
	
	private Field<?> fromPk;
	
	private List<Object> params;

	private boolean isPartedBuilder = false;

	PagingBuilderImpl(boolean isRootBuilder){
		if(isRootBuilder){
			selectBuilder = new PagingBuilderImpl(false);
			fromBuilder = new PagingBuilderImpl(false);
			whereBuilder = new PagingBuilderImpl(false);
			orderBuilder = new PagingBuilderImpl(false);
			groupbyBuilder = new PagingBuilderImpl(false);
		}
	}

	public PagingBuilderImpl initBuilders(){
		selectBuilder.initParted(this);
		fromBuilder.initParted(this);
		whereBuilder.initParted(this);
		orderBuilder.initParted(this);
		groupbyBuilder.initParted(this);
		return this;
	}

	private PagingBuilderImpl initParted (PagingBuilderImpl pagingBuilder){
		selectBuilder = pagingBuilder.selectBuilder;
		fromBuilder = pagingBuilder.fromBuilder;
		whereBuilder = pagingBuilder.whereBuilder;
		orderBuilder = pagingBuilder.orderBuilder;
		groupbyBuilder = pagingBuilder.groupbyBuilder;

		mainBuilder = pagingBuilder;
		isPartedBuilder = true;
		return this;
	}

	@Override
	public PagingBuilder select () {
		if(isPartedBuilder && this.equals(selectBuilder)){
			return super.select();
		}
		return selectBuilder.select();
	}

	@Override
	public PagingBuilder select (Fieldable... fields) {
		if(isPartedBuilder && this.equals(selectBuilder)){
			return super.select(fields);
		}
		return selectBuilder.select(fields);
	}

	public PagingBuilder from(Class<? extends Table> clazz, Field<?> pk) {
		mainBuilder.fromPk = pk;
		return from(clazz);
	}

	@Override
	public PagingBuilder from(Class<? extends Table> clazz) {
		if(isPartedBuilder && this.equals(fromBuilder)){
			return super.from(clazz);
		}
		return fromBuilder.from(clazz);
	}

	@Override
	public PagingBuilder from (Class<? extends Table> first, Class<? extends Table>... clazzs) {
		if(isPartedBuilder && this.equals(fromBuilder)){
			return super.from(first, clazzs);
		}
		return fromBuilder.from(first, clazzs);
	}

	@Override
	public PagingBuilder where () {
		if(isPartedBuilder && this.equals(whereBuilder)){
			return super.where ();
		}
		return whereBuilder.where();
	}

	@Override
	public PagingBuilder where (Filter<?> filter) {
		if(isPartedBuilder && this.equals(whereBuilder)){
			return super.where (filter);
		}
		return whereBuilder.where(filter);
	}

	@Deprecated
	public PagingBuilder where(FilterBuilder filterBuilder) {
		if(isPartedBuilder && this.equals(whereBuilder)){
			return super.where (filterBuilder);
		}
		return whereBuilder.where(filterBuilder);
	}

	@Override
	public PagingBuilder where (Jointer jointer) {
		if(isPartedBuilder && this.equals(whereBuilder)){
			return super.where (jointer);
		}
		return whereBuilder.where(jointer);
	}

	@Override
	public PagingBuilder groupBy (Fieldable... fields) {
		if(isPartedBuilder && this.equals(groupbyBuilder)){
			return super.groupBy (fields);
		}
		return groupbyBuilder.groupBy(fields);
	}

	@Override
	public PagingBuilder orderBy(Order... orders) {
		if(isPartedBuilder && this.equals(orderBuilder)){
			return super.orderBy (orders);
		}
		return orderBuilder.orderBy(orders);
	}

	public BuildResult buildPagingCount() {
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		mainBuilder.params = new ArrayList<Object>();
		
		builder.append("select count(*) ");
		
		//from builder
		builder.append(mainBuilder.fromBr.getSql());

		//where builder
		builder.append(mainBuilder.whereBr.getSql());
		if(mainBuilder.whereBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.whereBr.getParameters());
		}

		return new BuildResultImpl(builder, mainBuilder.params, null, null);
	}

	private void checkPageArgs(int pageStart, int pageEnd, int pageLimit){
		if(pageStart < 1){
			throw new IllegalArgumentException("分页开始数不能小于1");
		}
		if(pageEnd <= pageStart){
			throw new IllegalArgumentException("分页结束数不能小于等于分页开始数");
		}
		if(pageLimit < 1){
			throw new IllegalArgumentException("分页限制数不能小于1");
		}
	}

	public BuildResult buildPagingList(int pageStart, int pageEnd, int pageLimit) {
		checkPageArgs(pageStart, pageEnd, pageLimit);
		pageStart = pageStart - 1;
		pageEnd = pageEnd - 1;
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		mainBuilder.params = new ArrayList<Object>();
		
		builder.append("with rownum_table as ( select row_number() over( ");
		
		//order builder
		builder.append(mainBuilder.orderBr.getSql());
		builder.append(") rownum_table_rn , ");
		
		if(mainBuilder.fromPk == null){
			throw new IllegalArgumentException("oracle 分页查询 from 方法中传入的pk字段不能为null");
		}
		
		builder.append(mainBuilder.fromPk.getFullName()).append(" ");
		
		//from builder
		builder.append(mainBuilder.fromBr.getSql()).append(" ");
		if(mainBuilder.fromBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.fromBr.getParameters());
		}

		//where builder
		builder.append(mainBuilder.whereBr.getSql()).append(" ");
		if(mainBuilder.whereBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.whereBr.getParameters());
		}
		
		builder.append(") ");
		
		//select builder
		builder.append(mainBuilder.selectBr.getSql()).append(" ");
		if(mainBuilder.selectBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.selectBr.getParameters());
		}
		
		//from
		builder.append(mainBuilder.fromBr.getSql()).append(" ");
		if(mainBuilder.fromBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.fromBr.getParameters());
		}
		
		//inner join rownum_table
		builder.append("inner join rownum_table on ").append(mainBuilder.fromPk.getFullName())
		.append(" = ").append("rownum_table.").append(mainBuilder.fromPk.getName())
		.append(" and rownum_table.rownum_table_rn between ? and ? ");
		mainBuilder.params.add(pageStart * pageLimit + 1);
		mainBuilder.params.add(pageEnd * pageLimit);
		
		//where
		builder.append(mainBuilder.whereBr.getSql()).append(" ");
		if(mainBuilder.whereBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.whereBr.getParameters());
		}
		
		//order 
		builder.append(mainBuilder.orderBr.getSql()).append(" ");

		return new BuildResultImpl(builder, mainBuilder.params, mainBuilder.selectBr.getSelectFields(), mainBuilder.selectBr.getEmptyValuesFields());
	}

	public BuildResult buildMySqlPagingList (int pageStart, int pageEnd, int pageLimit) {
		checkPageArgs(pageStart, pageEnd, pageLimit);
		pageStart = pageStart - 1;
		pageEnd = pageEnd - 1;
		//构建结果
		buildResult();
		StringBuilder builder = new StringBuilder();
		mainBuilder.params = new ArrayList<Object>();

		//select builder
		builder.append(mainBuilder.selectBr.getSql()).append(" ");
		if(mainBuilder.selectBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.selectBr.getParameters());
		}

		//from
		builder.append(mainBuilder.fromBr.getSql()).append(" ");
		if(mainBuilder.fromBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.fromBr.getParameters());
		}

		//where
		builder.append(mainBuilder.whereBr.getSql()).append(" ");
		if(mainBuilder.whereBr.getParameters() != null){
			mainBuilder.params.addAll(mainBuilder.whereBr.getParameters());
		}

		//order
		builder.append(mainBuilder.orderBr.getSql()).append(" ");

		// limit TODO 优化大数据量分页效率
		builder.append("limit ").append(pageStart * pageLimit).append(", ").append(pageLimit * (pageEnd - pageStart));
		return new BuildResultImpl(builder, mainBuilder.params, mainBuilder.selectBr.getSelectFields(), mainBuilder.selectBr.getEmptyValuesFields());
	}

	@Override
	public BuildResult build () {
		if(isPartedBuilder){
			return super.build ();
		}
		throw new UnsupportedOperationException("PagingBuilder 不支持 build() 方法");
	}

	private void buildResult(){
		if(mainBuilder.selectBr == null){
			mainBuilder.selectBr = selectBuilder.build();
		}
		if(mainBuilder.fromBr == null){
			mainBuilder.fromBr = fromBuilder.build();
		}
		if(mainBuilder.whereBr == null){
			mainBuilder.whereBr = whereBuilder.build();
		}
		if(mainBuilder.orderBr == null){
			mainBuilder.orderBr = orderBuilder.build();
		}
	}

}
