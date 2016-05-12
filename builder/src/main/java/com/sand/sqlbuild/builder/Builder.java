package com.sand.sqlbuild.builder;


/**
 *
 * @author : sun.mt
 * @date : 2015年5月6日 下午12:28:52
 * @since 1.0.0
 *
 */
public interface Builder {
	
	/*------------------------------------查询（select）------------------------------------*/
	
	Builder select ();
	
	Builder select (Fieldable... fields);
	
	Builder distinct ();

	Builder fields (Fieldable... fields);
	
	//Builder all();

	/*------------------------------------插入（insert）------------------------------------*/

	Builder insert (Class<? extends Table> clazz, Field<?>... fields);

	Builder insert(Table table, Field<?>... fields);

	Builder values (Object... values);

	Builder insert (Class<? extends Table> clazz, Setter<?>... setters);

	Builder insert (Table table, Setter<?>... setters);

	Builder upsertOracle (Class<? extends Table> clazz, Setter<?>[] setters, Filter<?>[] filters);

	/*------------------------------------更新（update）------------------------------------*/

	Builder update (Class<? extends Table> clazz);

	Builder set (Setter<?>... setters);

	/*------------------------------------删除（update）------------------------------------*/

	Builder delete ();

	/*------------------------------------表连接（join）------------------------------------*/

	/**
	 * 内连接（inner join）
	 * 
	 * @param clazz
	 * @return
	 */
	Builder join (Class<? extends Table> clazz);
	
	/**
	 * 内连接（inner join）
	 * 
	 * @param clazz
	 * @return
	 */
	Builder innerJoin (Class<? extends Table> clazz);
	Builder leftJoin (Class<? extends Table> clazz);
	Builder rightJoin (Class<? extends Table> clazz);
	Builder on (Filter<?> filter);

	/*------------------------------------通用（common）------------------------------------*/
	
	Builder from (Class<? extends Table> clazz);

    /**
     * 多表查询（select * from table_1, table_2）
     *
     * @param first
     * @param clazzs
     * @return
     */
    Builder from (Class<? extends Table> first, Class<? extends Table>... clazzs);

    /**
     *
     * @param clazzs
     * @return
     */
    Builder from (Class<? extends Table>[] clazzs);

	Builder where ();

	Builder where (Filter<?> filter);

	Builder where (Jointer jointer);

	@Deprecated
	Builder where (FilterBuilder filterBuilder);
	
	Builder and ();
	
	Builder and (Filter<?> filter);

	Builder and (Jointer jointer);

	@Deprecated
	Builder and (FilterBuilder filterBuilder);
	
	Builder or (Filter<?> filter);

	Builder or (Jointer jointer);

	@Deprecated
	Builder or (FilterBuilder filterBuilder);
	
	Builder orderBy (Order... orders);
	
	Builder groupBy (Fieldable... fields);

	/**
	 * Mysql 语法
	 * 
	 * @param limit
	 * @return
	 */
	Builder limit (int limit);

	/**
	 * Mysql 语法
	 * @param limitStart
	 * @param limitEnd
	 * @return
	 */
	Builder limit (int limitStart, int limitEnd);
	
	/**
	 * SQL Server 语法
	 * 
	 * @param top
	 * @return
	 */
	Builder top (int top);
	
	/**
	 * Oracle 语法
	 * 
	 * @param rownum
	 * @return
	 */
	Builder rownum (int rownum);
	

	/**
	 * 函数
	 * 
	 * @param funName
	 * @return
	 */
	Builder func (String funName);

	//Builder func (String funName, Field<?>... args);

	/**
	 * 左边括号"("
	 * 
	 * <br>ls = left sign
	 * @return
	 */
	Builder ls ();
	
	/**
	 * 函数参数
	 * 
	 * @param fields
	 * @return
	 */
	Builder args (Fieldable... fields);
	
	/**
	 * 右边括号")"
	 * 
	 * <br>rs = right sign
	 * 
	 * @return
	 */
	Builder rs ();

	/**
	 * 点 “.”
	 *
	 * @return
	 */
	Builder dot ();
	
	/**
	 * 逗号 “,”
	 * 
	 * @return
	 */
	Builder comma ();
	
	/**
	 * 别名
	 * 
	 * @param field
	 * @return
	 */
	Builder as (Field<?> field);


    Builder as (String aliasName, Class<?> javaType);
	
	Builder as (Field<?> field, Class<?> javaType);

	Builder forUpdate ();
	
	/**
	 * wait 子句指定等待其他用户释放锁的秒数，防止无限期的等待
	 * 
	 * <br>为了避免与Object.wait重名后边加一个s后缀
	 * 
	 * @param second
	 * @return
	 */
	Builder waits (int second);
	
	Builder union ();

	Builder unionAll ();

	/**
	 * 构建返回结果并重新初始化builder
	 * 
	 * @return
	 */
	BuildResult build ();


	/**
	 * 从新初始化
	 */
	Builder reinit ();

	Builder or();

	Builder rs(Filter<?> filter);

	Builder rs(Jointer jointer);

	Builder ls(Filter<?> filter);

	Builder ls(Jointer jointer);

	Builder cases();

	Builder cases(Field<?> field);

	Builder cases(Jointer jointer);

	Builder when();

	Builder when(Field<?> field);

	Builder when(Object value);

	Builder when(Filter<?> filter);

	Builder when(Jointer jointer);

	Builder then();

	Builder then(Field<?> field);

	Builder then(Object value);

	Builder then(Jointer jointer);

	Builder elses();

	Builder elses(Field<?> field);

	Builder elses(Object value);

	Builder elses(Jointer jointer);

	Builder end();

	Builder count(Field<?> field);

	/**
	 * COUNT(DISTINCT) 适用于 ORACLE 和 Microsoft SQL Server，但是无法用于 Microsoft Access。
	 * @param field
	 * @return
	 */
	Builder countDistinct(Field<?> field);

	Builder sum(Fieldable field);

	Builder max(Fieldable field);

	Builder min(Fieldable field);

	Builder avg(Fieldable field);

	Builder len(Fieldable field);

	/**
	 * MID 函数用于从文本字段中提取字符。
	 * @param field
	 * @param start 开始位置
	 * @param length 长度
	 * @return
	 */
	Builder mid(Fieldable field, int start, int length);

	/**
	 * UCASE 函数把字段的值转换为大写。
	 * @param field
	 * @return
	 */
	Builder ucase(Fieldable field);

	/**
	 * LCASE 函数把字段的值转换为小写。
	 * @param field
	 * @return
	 */
	Builder lcase(Fieldable field);

	/**
	 * FIRST() 函数返回指定的字段中第一个记录的值。
	 * @param field
	 * @return
	 */
	Builder first(Fieldable field);

	/**
	 * LAST() 函数返回指定的字段中最后一个记录的值。
	 * @param field
	 * @return
	 */
	Builder last(Fieldable field);

	/**
	 * ROUND 函数用于把数值字段舍入为指定的小数位数。
	 * @param field 必需。要舍入的字段。
	 * @param decimals 必需。规定要返回的小数位数。
	 * @return
	 */
	Builder round(Fieldable field, int decimals);

	/**
	 * FORMAT 函数用于对字段的显示进行格式化。
	 * @param field
	 * @param format
	 * @return
	 */
	Builder format(Fieldable field, String format);

}
