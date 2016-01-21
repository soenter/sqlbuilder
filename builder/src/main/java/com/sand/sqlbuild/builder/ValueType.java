package com.sand.sqlbuild.builder;

/**
 * @author : sun.mt
 * @create : 16-1-21 下午1:54
 * @since : 0.2.0
 */
public enum ValueType {

	/**单值:正常比较（默认）*/
	ONE_VALUE,
	/**双值:between ? and ?*/
	TWIN_VALUES,
	/**多值（两个以上）:in*/
	MULTI_VALUES,
	/**没有值: is null */
	NO_VALUE,
	/**空值: 批量 */
	EMPTY_VALUE,
	/**字段值*/
	FIELD_VALUE;

}
