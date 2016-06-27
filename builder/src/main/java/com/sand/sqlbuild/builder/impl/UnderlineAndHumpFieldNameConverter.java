package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.FieldNameConverter;
import com.sand.sqlbuild.builder.FieldUtils;

/**
 * @author : sun.mt
 * @create : 16-6-25 上午12:27
 * @since : ${VERSION}
 */
public class UnderlineAndHumpFieldNameConverter implements FieldNameConverter {

	public String convert (String name) {
		return FieldUtils.underlineToHumpStyle(name);
	}
}
