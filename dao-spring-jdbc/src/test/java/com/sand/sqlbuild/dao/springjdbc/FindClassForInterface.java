package com.sand.sqlbuild.dao.springjdbc;

import org.junit.Test;

/**
 * @author : sun.mt
 * @create : 16-6-29 下午1:44
 * @since : ${VERSION}
 */
public class FindClassForInterface {


	@Test
	public void test_find_class_for_interface(){


		FindClassForInterface.class.getClassLoader();

	}
}
