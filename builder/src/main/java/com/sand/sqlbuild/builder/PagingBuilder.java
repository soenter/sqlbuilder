package com.sand.sqlbuild.builder;

/**
 *
 * @author : sun.mt
 * @date : 2015年6月2日 下午3:36:07
 * @since 1.0.0
 *
 */
public interface PagingBuilder extends BaseBuilder<PagingBuilder> {
	

	/**
	 * @param clazz
	 * @param pk
	 * @return
	 */
	PagingBuilder from (Class<? extends Table> clazz, Field<?> pk);

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
