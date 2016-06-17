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

	/**
	 * @param clazz
	 * @param pk
	 * @return
	 */
	PagingBuilder from (Class<? extends Table> clazz, Field<?> pk);

	PagingBuilder from (Class<? extends Table> clazz);
	
	PagingBuilder innerJoin (Class<? extends Table> clazz);
	
	PagingBuilder leftJoin (Class<? extends Table> clazz);
	
	PagingBuilder rightJoin (Class<? extends Table> clazz);
	
	PagingBuilder on (Filter<?> filter);
	
	PagingBuilder where (Filter<?>... filters);
	
	PagingBuilder where (FilterBuilder filterBuilder);
	
	PagingBuilder and (Filter<?> filter);
	
	PagingBuilder or (Filter<?> filter);
	
	PagingBuilder orderBy (Order... orders);

	PagingBuilder groupBy (Field<?>... fields);

	/**
	 * Mysql 语法
	 *
	 * @param limit
	 * @return
	 */
	PagingBuilder limit (int limit);

	/**
	 * Oracle 语法
	 *
	 * @param rownum
	 * @return
	 */
	PagingBuilder rownum (int rownum);

	/**
	 * 左边括号"("
	 *
	 * <br>ls = left sign
	 * @return
	 */
	PagingBuilder ls ();

	/**
	 * 右边括号")"
	 *
	 * <br>rs = right sign
	 *
	 * @return
	 */
	PagingBuilder rs ();

	PagingBuilder rs(Filter<?> filter);

	PagingBuilder ls(Filter<?> filter);

	PagingBuilder func(String funName);

	/**
	 * @return
	 */
	BuildResult buildPagingCount ();

	/**
	 * 默认Oracle数据库分页语法
	 * @return
	 */
	BuildResult buildPagingList (int pageStart, int pageEnd, int pageLimit);

	/**
	 * MySql分页语法
	 * @param pageStart
	 * @param pageEnd
	 * @param pageLimit
	 * @return
	 */
	BuildResult buildMySqlPagingList (int pageStart, int pageEnd, int pageLimit);
}
