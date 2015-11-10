package com.sand.sqlbuild.dao.springjdbc;

/**
 * @author : sun.mt
 * @create : 2015/11/10 17:01
 * @since : 0.1.3
 */
public interface BatchPoGetter {

	<R extends AbstractPo> R getPo(int index);

	int getBatchSize();
}
