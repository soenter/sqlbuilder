/**
 * Copyright : http://www.sandpay.com.cn/ , 2007-2015
 * Project : abacus-util-data-sqlbuild
 * $Id$
 * $Revision$
 * Last Changed by sun.mt at 2015年6月2日 下午3:36:07
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * sun.mt         2015年6月2日        Initailized
 */
package com.sand.abacus.util.data.sqlbuild;

/**
 *
 * @ClassName ：PagingBuilder
 * @Description : 
 * @author : sun.mt
 * @Date : 2015年6月2日 下午3:36:07
 * @version 1.0.0
 *
 */
public interface PagingBuilder {
	
	public PagingBuilder select(Field<?>... fields);
	
	public PagingBuilder from(Class<? extends Table> clazz, Field<?> pk);
	
	public PagingBuilder innerJoin(Class<? extends Table> clazz);
	
	public PagingBuilder leftJoin(Class<? extends Table> clazz);
	
	public PagingBuilder rightJoin(Class<? extends Table> clazz);
	
	public PagingBuilder on(Filter<?> filter);
	
	public PagingBuilder where(Filter<?>... filters);
	
	public PagingBuilder where(FilterBuilder filterBuilder);
	
	public PagingBuilder and(Filter<?> filter);
	
	public PagingBuilder or(Filter<?> filter);
	
	public PagingBuilder orderBy(Order... orders);
	
	public BuildResult buildPagingCount();
	
	public BuildResult buildPagingList(int pageStart, int pageEnd, int pageLimit);
}
