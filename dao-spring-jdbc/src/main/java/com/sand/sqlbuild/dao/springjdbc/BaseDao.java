package com.sand.sqlbuild.dao.springjdbc;

import com.sand.sqlbuild.builder.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author : sun.mt
 * @create : 2015/9/22 17:48
 * @since : 0.1.1
 */
public interface BaseDao {

	/**
	 * 获取spring jdbc template
	 * @return
	 */
	JdbcTemplate getJdbcTemplate();

	/**
	 * 插入
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @return 受影响数
	 */
	int insert(BuildResult buildResult);

	/**
	 * 插入
	 *
	 * @param po po对象不能为空
	 * @return 受影响数
	 */
	int insert(AbstractPo po);

	/**
	 * 修改
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @return 受影响数
	 */
	int update(BuildResult buildResult);

	/**
	 * 批量更新
	 * @param result Builder 结果
	 * @param params 参数集合
	 * @param <R>
	 * @return
	 */
	<R extends AbstractPo> int[] batchUpdate(BuildResult result, List<R> params);

	/**
	 * 批量更新
	 * @param result
	 * @param poGetter
	 * @param <R>
	 * @return
	 */
	<R extends AbstractPo> int[] batchUpdate(BuildResult result, BatchPoGetter poGetter);

	/**
	 * 删除
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @return 受影响数
	 */
	int delete(BuildResult buildResult);

	/**
	 * 查询单条，并映射成PO对象
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @param po 集成AbstractPo的Java对象
	 * @return
	 */
	<R extends AbstractPo> R queryForPo (BuildResult buildResult, R po);

	/**
	 * 查询单条记录的第一个列，并映射成Java对象
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @param clazz 对象class
	 * @return
	 */
	<R> R queryFirstColumnForObject (BuildResult buildResult, Class<R> clazz);

	/**
	 * 查询多条，并映射成List Java对象集合
	 *
	 * @param buildResult Builder构建后的结果对象
	 * @param clazz
	 * @return
	 */
	<R extends AbstractPo> List<R> queryForPoList (BuildResult buildResult, Class<R> clazz);


	/**
	 * 插入
	 *
	 * @param setters 字段+值设置器组数
	 * @param clazz
	 * @return 受影响数
	 */
	<R extends AbstractPo> int insert(Setter<?>[] setters, Class<R> clazz);

	/**
	 * 更新
	 *
	 * @param setters 字段+值设置器组数
	 * @param filters 字段+值过滤器组数
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return 受影响数
	 */
	<R extends AbstractPo> int update(Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz);

	/**
	 * 更新或插入(使用 oracle merge into 语法实现 upsert)
	 * @see <code>upsert<code/> method
	 * @param setters 字段+值设置器组数
	 * @param filters 字段+值过滤器组数
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return 受影响数
	 */
	@Deprecated
	<R extends AbstractPo> int upsertOracle(Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz);

	<R extends AbstractPo> int upsert(Setter<?>[] setters, Filter<?>[] filters, Class<R> clazz);

	/**
	 * 更新
	 *
	 * @param setters 字段+值设置器组数
	 * @param filterBuilder 字段+值过滤器的构建对象
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return 受影响数
	 */
	<R extends AbstractPo> int update(Setter<?>[] setters, FilterBuilder filterBuilder, Class<R> clazz);

	/**
	 * 删除
	 *
	 * @param filters 字段+值过滤器组数
	 * @param clazz  表结构的Java对象映射类型（Class）
	 * @return 受影响数
	 */
	<R extends AbstractPo> int delete(Filter<?>[] filters, Class<R> clazz);

	/**
	 * 删除
	 *
	 * @param filterBuilder 字段+值过滤器的构建对象
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return 受影响数
	 */
	<R extends AbstractPo> int delete(FilterBuilder filterBuilder, Class<R> clazz);

	/**
	 * 查询单条，并映射成Java对象
	 *
	 * @param fields 查询字段数组
	 * @param filters 字段+值过滤器组数
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return Java对象
	 */
	<R extends AbstractPo> R queryForPo (Field<?>[] fields, Filter<?>[] filters, Class<R> clazz);

	/**
	 * 查询多条，并映射成List Java对象集合
	 *
	 * @param fields 查询字段数组
	 * @param filterBuilder 字段+值过滤器的构建对象
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return List Java对象集合
	 */
	<R extends AbstractPo> R queryForPo (Field<?>[] fields, FilterBuilder filterBuilder, Class<R> clazz);


	/**
	 * 查询多条，并映射成List Java对象集合
	 *
	 * @param fields 查询字段数组
	 * @param filters 字段+值过滤器组数
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return List Java对象集合
	 */
	<R extends AbstractPo> List<R> queryForPoList (Field<?>[] fields, Filter<?>[] filters, Class<R> clazz);

	/**
	 * 查询多条，并映射成List Java对象集合
	 *
	 * @param fields 查询字段数组
	 * @param filterBuilder 字段+值过滤器的构建对象
	 * @param clazz 表结构的Java对象映射类型（Class）
	 * @return List Java对象集合
	 */
	<R extends AbstractPo> List<R> queryForPoList (Field<?>[] fields, FilterBuilder filterBuilder, Class<R> clazz);


	/**
	 * 查询分页总数
	 * @param builder
	 * @return
	 */
	long queryForPagingCount (PagingBuilder builder);

	/**
	 * 分页查询多条，并映射成List Java对象集合
	 * @param builder 分页查询Builder
	 * @param clazz Po类
	 * @param pagingStart 开始页数
	 * @param pagingEnd 结束页数
	 * @param pagingLimit 每页限制
	 * @param <R>
	 * @return
	 */
	<R extends AbstractPo> List<R> queryForPagingPoList (PagingBuilder builder, Class<R> clazz, int pagingStart, int pagingEnd, int pagingLimit);


}
