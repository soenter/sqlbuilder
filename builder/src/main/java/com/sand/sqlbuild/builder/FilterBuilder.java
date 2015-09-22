package com.sand.sqlbuild.builder;

/**
 *
 * @author : sun.mt
 * @date : 2015年5月8日 上午11:21:59
 * @since 1.0.0
 *
 */
public interface FilterBuilder {

	/**
	 * 左边括号"("
	 * 
	 * <br>ls = left sign
	 * @return
	 */
	FilterBuilder ls ();
	
	/**
	 * 右边括号")"
	 * 
	 * <br>rs = left sign
	 * 
	 * @return
	 */
	FilterBuilder rs ();
	
	/**
	 * 左右边括号 "("  ")"
	 * 
	 * <br>lrs = left right sign
	 * 
	 * @return
	 */
	FilterBuilder lrs (FilterBuilder filterBuilder);
	
	/**
	 * 分组里边嵌套分组
	 * 
	 * @param filterBuilder 嵌套分组
	 * @return
	 */
	FilterBuilder add (FilterBuilder filterBuilder);

	/**
	 * 分组里边套条件
	 * 
	 * @param filter 条件
	 * @return
	 */
	FilterBuilder add (Filter<?> filter);
	
	/**
	 * 逻辑连接“且”
	 * 
	 * @return
	 */
	FilterBuilder and ();
	
	/**
	 * 逻辑连接“或”
	 * 
	 * @return
	 */
	FilterBuilder or ();
	
	/**
	 * 限制查询记录数, Mysql 语法
	 * 
	 * @param limit
	 * @return
	 */
	FilterBuilder limit (int limit);
	
	/**
	 * 限制查询记录数, Oracle 语法
	 * 
	 * @param rownum
	 * @return
	 */
	FilterBuilder rownum (int rownum);
	

	/**
	 * 构建返回结果并重新初始化builder
	 * 
	 * @return
	 */
	FilterBuildResult build ();
}
