package com.sand.sqlbuild.builder;

/**
 * @author : sun.mt
 * @create : 16-8-2 下午4:34
 * @since : ${VERSION}
 */
@SuppressWarnings("unchecked")
public interface BaseBuilder  <T extends BaseBuilder>{

	/*------------------------------------查询（select）------------------------------------*/

	T select ();

	T select (Fieldable... fields);

	T distinct ();

	T distinct (Fieldable... fields);

	T fields (Fieldable... fields);

	//T all();

	/*------------------------------------插入（insert）------------------------------------*/

	T insert (Class<? extends Table> clazz, Field<?>... fields);

	T insert(Table table, Field<?>... fields);

	T values (Object... values);

	T insert (Class<? extends Table> clazz, Setter<?>... setters);

	T insert (Table table, Setter<?>... setters);

	T upsertOracle (Class<? extends Table> clazz, Setter<?>[] setters, Filter<?>[] filters);

	/*------------------------------------更新（update）------------------------------------*/

	T update (Class<? extends Table> clazz);

	@SuppressWarnings("unchecked")
	T set (Setter<?>... setters);

	/*------------------------------------删除（update）------------------------------------*/

	T delete ();

	/*------------------------------------表连接（join）------------------------------------*/

	/**
	 * 内连接（inner join）
	 *
	 * @param clazz 实现<code>Table</code>接口的Class
	 * @return builder
	 */
	T join (Class<? extends Table> clazz);

	/**
	 * 内连接（inner join）
	 *
	 * @param clazz 实现<code>Table</code>接口的Class
	 * @return builder
	 */
	T innerJoin (Class<? extends Table> clazz);
	T leftJoin (Class<? extends Table> clazz);
	T rightJoin (Class<? extends Table> clazz);
	T on (Filter<?> filter);

	/*------------------------------------通用（common）------------------------------------*/

	T from (Class<? extends Table> clazz);

	/**
	 * 多表查询（select * from table_1, table_2）
	 *
	 * @param first 实现<code>Table</code>接口的Class
	 * @param clazzs 实现<code>Table</code>接口的Class
	 * @return builder
	 */
	T from (Class<? extends Table> first, Class<? extends Table>... clazzs);

	/**
	 *
	 * @param clazzs 实现<code>Table</code>接口的Class
	 * @return builder
	 */
	T from (Class<? extends Table>[] clazzs);

	T where ();

	@SuppressWarnings("unchecked")
	T where (Filter<?> filter);

	T where (Jointer jointer);

	@Deprecated
	T where (FilterBuilder filterT);

	T and ();

	T and (Filter<?> filter);

	T and (Jointer jointer);

	@Deprecated
	T and (FilterBuilder filterT);

	T or (Filter<?> filter);

	T or (Jointer jointer);

	@Deprecated
	T or (FilterBuilder filterT);

	T orderBy (Order... orders);

	T groupBy (Fieldable... fields);

	/**
	 * Mysql 语法
	 *
	 * @param limit
	 * @return
	 */
	T limit (int limit);

	/**
	 * Mysql 语法
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	T limit (int limitStart, int limitEnd);

	/**
	 * SQL Server 语法
	 *
	 * @param top
	 * @return
	 */
	T top (int top);

	/**
	 * Oracle 语法
	 *
	 * @param rownum
	 * @return
	 */
	T rownum (int rownum);


	/**
	 * 函数
	 *
	 * @param funName
	 * @return
	 */
	T func (String funName);

	//T func (String funName, Field<?>... args);

	/**
	 * 左边括号"("
	 *
	 * <br>ls = left sign
	 * @return
	 */
	T ls ();

	/**
	 * 函数参数
	 *
	 * @param fields
	 * @return
	 */
	T args (Fieldable... fields);

	/**
	 * 右边括号")"
	 *
	 * <br>rs = right sign
	 *
	 * @return
	 */
	T rs ();

	/**
	 * 点 “.”
	 *
	 * @return
	 */
	T dot ();

	/**
	 * 逗号 “,”
	 *
	 * @return
	 */
	T comma ();

	/**
	 * 别名
	 *
	 * @param field
	 * @return
	 */
	T as (Field<?> field);


	T as (String aliasName, Class<?> javaType);

	T as (Field<?> field, Class<?> javaType);

	T forUpdate ();

	/**
	 * wait 子句指定等待其他用户释放锁的秒数，防止无限期的等待
	 *
	 * <br>为了避免与Object.wait重名后边加一个s后缀
	 *
	 * @param second
	 * @return
	 */
	T waits (int second);

	T union ();

	T unionAll ();

	/**
	 * 构建返回结果并重新初始化builder
	 *
	 * @return
	 */
	BuildResult build ();


	/**
	 * 从新初始化
	 */
	T reinit ();

	T or();

	T rs(Filter<?> filter);

	T rs(Jointer jointer);

	T ls(Filter<?> filter);

	T ls(Jointer jointer);

	T cases();

	T cases(Field<?> field);

	T cases(Jointer jointer);

	T when();

	T when(Field<?> field);

	T when(Object value);

	T when(Filter<?> filter);

	T when(Jointer jointer);

	T then();

	T then(Field<?> field);

	T then(Object value);

	T then(Jointer jointer);

	T elses();

	T elses(Field<?> field);

	T elses(Object value);

	T elses(Jointer jointer);

	T end();

	T count(Field<?> field);

	/**
	 * COUNT(DISTINCT) 适用于 ORACLE 和 Microsoft SQL Server，但是无法用于 Microsoft Access。
	 * @param field
	 * @return
	 */
	T countDistinct(Field<?> field);

	T sum(Fieldable field);

	T max(Fieldable field);

	T min(Fieldable field);

	T avg(Fieldable field);

	T len(Fieldable field);

	/**
	 * MID 函数用于从文本字段中提取字符。
	 * @param field
	 * @param start 开始位置
	 * @param length 长度
	 * @return
	 */
	T mid(Fieldable field, int start, int length);

	/**
	 * UCASE 函数把字段的值转换为大写。
	 * @param field
	 * @return
	 */
	T ucase(Fieldable field);

	/**
	 * LCASE 函数把字段的值转换为小写。
	 * @param field
	 * @return
	 */
	T lcase(Fieldable field);

	/**
	 * FIRST() 函数返回指定的字段中第一个记录的值。
	 * @param field
	 * @return
	 */
	T first(Fieldable field);

	/**
	 * LAST() 函数返回指定的字段中最后一个记录的值。
	 * @param field
	 * @return
	 */
	T last(Fieldable field);

	/**
	 * ROUND 函数用于把数值字段舍入为指定的小数位数。
	 * @param field 必需。要舍入的字段。
	 * @param decimals 必需。规定要返回的小数位数。
	 * @return
	 */
	T round(Fieldable field, int decimals);

	/**
	 * FORMAT 函数用于对字段的显示进行格式化。
	 * @param field
	 * @param format
	 * @return
	 */
	T format(Fieldable field, String format);

}
