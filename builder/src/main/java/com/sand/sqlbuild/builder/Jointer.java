package com.sand.sqlbuild.builder;

/**
 * 双向链表结构
 * @author : sun.mt
 * @create : 16-5-11 下午2:41
 * @since : 0.0.2
 */
public interface Jointer extends Fieldable {


	Jointer getPrev();

	Jointer getNext();

	String getOperator();

	Object getPartner ();

	boolean hasNext();

	boolean hasPrev();

	boolean hasOperator();

	/**
	 * 加
	 * @param partner
	 * @return
	 */
	Jointer plus (Object partner);
	Jointer plus (Jointer partner);
	Jointer plus ();

	/**
	 * 减
	 * @param partner
	 * @return
	 */
	Jointer subtract (Object partner);
	Jointer subtract (Jointer partner);
	Jointer subtract ();

	/**
	 * 乘
	 * @param partner
	 * @return
	 */
	Jointer multiply (Object partner);
	Jointer multiply (Jointer partner);
	Jointer multiply ();

	/**
	 * 除
	 * @param partner
	 * @return
	 */
	Jointer divide (Object partner);
	Jointer divide (Jointer partner);
	Jointer divide ();

	/**
	 * 连接
	 * @param operator
	 * @param partner
	 * @return
	 */
	Jointer join(String operator, Object partner);
	Jointer join(String operator, Jointer partner);

	Jointer join(String operator);


	/**
	 *
	 * 左括号“)”
	 * @return
	 */
	Jointer ls();

	/**
	 *
	 * 右括号“)”
	 * @return
	 */
	Jointer rs();

	/**
	 *
	 * 左括号“)”
	 * @param jointer
	 * @return
	 */
	Jointer ls(Jointer jointer);
	Jointer ls(Field<?> field);

	/**
	 * 右括号“)”
	 * @param jointer
	 * @return
	 */
	Jointer rs(Jointer jointer);
	Jointer rs(Field<?> field);

	/**
	 * 双括号“()”
	 * @param fieldable
	 * @return
	 */
	Jointer ds(Jointer fieldable);

	Jointer as(Field<?> as);

	Jointer as(String as, Class<?> clazz);

	/**
	 * 等于（=）
	 *
	 * @param partner
	 * @return
	 */
	Jointer eq (Object partner);

	/**
	 * 不等于（!=, <>）
	 *
	 * @param partner
	 * @return
	 */
	Jointer ne (Object partner);

	/**
	 * 大于（>）
	 *
	 * @param partner
	 * @return
	 */
	Jointer gt (Object partner);

	/**
	 * 小于（<）
	 *
	 * @param partner
	 * @return
	 */
	Jointer lt (Object partner);


	/**
	 * 大于等于（>=）
	 *
	 * @param partner
	 * @return
	 */
	Jointer gte (Object partner);

	/**
	 * 小于等于（<=）
	 *
	 * @param partner
	 * @return
	 */
	Jointer lte (Object partner);


	/**
	 * 类似模糊查询（like '%string%'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer lk (CharSequence partner);

	/**
	 * 左类似（like 'string%'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer llk (CharSequence partner);

	/**
	 * 右类似（like '%string'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer rlk (CharSequence partner);

	/**
	 * 非类似模糊查询（not like '%string%'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer nlk (CharSequence partner);

	/**
	 * 非左类似（not like 'string%'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer nllk (CharSequence partner);

	/**
	 * 非右类似（not like '%string'）
	 *
	 * @param partner
	 * @return
	 */
	Jointer nrlk (CharSequence partner);


	/**
	 * 设置值 （field = value）
	 *
	 * @param partner
	 * @return
	 */
	Jointer st (Object partner);

	/**
	 * 在...之间（between ? and ？）
	 *
	 * @param partner2
	 * @return
	 */
	Jointer bta (Object partner1, Object partner2);


}
