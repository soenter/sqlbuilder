package com.sand.sqlbuild.builder;


import java.util.List;

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
	
	Builder select (Field<?>... fields);
	
	Builder distinct ();

	Builder fields (Field<?>... fields);
	
	//Builder all();

	/*------------------------------------插入（insert）------------------------------------*/

	Builder insert (Class<? extends Table> clazz, Field<?>... fields);
	
	Builder values (Object... values);

	Builder insert (Class<? extends Table> clazz, Setter<?>... setters);

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

	Builder where (Filter<?> filter);

	Builder where (FilterBuilder filterBuilder);
	
	Builder and ();
	
	Builder and (Filter<?> filter);
	
	Builder and (FilterBuilder filterBuilder);
	
	Builder or (Filter<?> filter);
	
	Builder or (FilterBuilder filterBuilder);
	
	Builder orderBy (Order... orders);
	
	Builder groupBy (Field<?>... fields);

	/**
	 * Mysql 语法
	 * 
	 * @param limit
	 * @return
	 */
	Builder limit (int limit);
	
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
	Builder args (Field<?>... fields);
	
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
}
