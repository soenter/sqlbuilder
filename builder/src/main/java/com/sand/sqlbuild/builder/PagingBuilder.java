package com.sand.sqlbuild.builder;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午3:36:07
 * @since 1.0.0
 *
 */
public interface PagingBuilder {
	
	PagingBuilder select (Field<?>... fields);
	
	PagingBuilder from (Class<? extends Table> clazz, Field<?> pk);
	
	PagingBuilder innerJoin (Class<? extends Table> clazz);
	
	PagingBuilder leftJoin (Class<? extends Table> clazz);
	
	PagingBuilder rightJoin (Class<? extends Table> clazz);
	
	PagingBuilder on (Filter<?> filter);
	
	PagingBuilder where (Filter<?>... filters);
	
	PagingBuilder where (FilterBuilder filterBuilder);
	
	PagingBuilder and (Filter<?> filter);
	
	PagingBuilder or (Filter<?> filter);
	
	PagingBuilder orderBy (Order... orders);
	
	BuildResult buildPagingCount ();
	
	BuildResult buildPagingList (int pageStart, int pageEnd, int pageLimit);
}
