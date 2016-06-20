package com.sand.sqlbuild.builder.impl;

import com.sand.sqlbuild.builder.*;

/**
 * @author : sun.mt
 * @create : 16-5-11 下午3:24
 * @since : 0.2
 */
public class JointerImpl implements Jointer {

	private Jointer prev;

	private Jointer next;

	private String operator;

	private Object partner;

	JointerImpl (Jointer prev, String operator, Object partner) {
		this.prev = prev;
		this.operator = operator;
		this.partner = partner;
	}

	public Jointer getPrev () {
		return prev;
	}

	public Jointer getNext () {
		return next;
	}

	public String getOperator () {
		return operator;
	}

	public Object getPartner () {
		return partner;
	}

	public boolean hasNext () {
		return next != null;
	}

	public boolean hasPrev () {
		return prev != null;
	}

	public Jointer plus (Object partner) {
		return join("+", partner);
	}

	public Jointer subtract (Object partner) {
		return join("-", partner);
	}

	public Jointer multiply (Object partner) {
		return join("*", partner);
	}

	public Jointer divide (Object partner) {
		return join("/", partner);
	}

	public Jointer join (String operator, Object partner) {
		return joinObject(operator, partner);
	}

	private Jointer joinObject(String operator, Object partner){
		if(partner instanceof Jointer){
			return FieldUtils.joinJointer(this, operator, (Jointer)partner);
		}
		return next = new JointerImpl(this, operator, partner);
	}

	public Jointer join (String operator) {
		return joinObject(operator, null);
	}

	public Jointer plus (Jointer partner) {
		return join("+", partner);
	}

	public Jointer subtract (Jointer partner) {
		return join("-", partner);
	}

	public Jointer multiply (Jointer partner) {
		return join("*", partner);
	}

	public Jointer divide (Jointer partner) {
		return join("/", partner);
	}

	public Jointer join (String operator, Jointer partner) {
		//加上双括号 "()"
		return joinObject(operator, ds(partner));
	}

	public Jointer ls () {
		return join("(");
	}

	public Jointer rs () {
		return join(")");
	}

	public Jointer ls (Jointer jointer) {
		return joinObject("(", jointer);
	}

	public Jointer rs (Jointer jointer) {
		return joinObject(")", jointer);
	}

	public Jointer ls (Field<?> field) {
		return joinObject("(", field);
	}

	public Jointer rs (Field<?> field) {
		return joinObject(")", field);
	}

	public Jointer ds (Jointer jointer) {
		return ls(jointer).rs();
	}

	public boolean hasOperator () {
		return operator != null;
	}

	public Jointer as (Field<?> as) {
		return next = new JointerImpl(this, " as ", as);
	}

	public Jointer as (String as, Class<?> clazz) {
		return as(FieldFactory.create(as, clazz));
	}

	public Jointer eq (Object partner) {
		return join("=", partner);
	}

	public Jointer ne (Object partner) {
		return join("<>", partner);
	}

	public Jointer gt (Object partner) {
		return join(">", partner);
	}

	public Jointer lt (Object partner) {
		return join("<", partner);
	}

	public Jointer gte (Object partner) {
		return join(">=", partner);
	}

	public Jointer lte (Object partner) {
		return join("<", partner);
	}

	public Jointer lk (CharSequence partner) {
		return next = new JointerImpl(this, " like ", "%" + partner + "%");
	}

	public Jointer llk (CharSequence partner) {
		return next = new JointerImpl(this, " like ", partner + "%");
	}

	public Jointer rlk (CharSequence partner) {
		return next = new JointerImpl(this, " like ", "%" + partner);
	}

	public Jointer nlk (CharSequence partner) {
		return next = new JointerImpl(this, " not like ", "%" + partner + "%");
	}

	public Jointer nllk (CharSequence partner) {
		return next = new JointerImpl(this, " not like ", partner + "%");
	}

	public Jointer nrlk (CharSequence partner) {
		return next = new JointerImpl(this, " not like ", "%" + partner);
	}

	public Jointer st (Object partner) {
		return eq(partner);
	}

	public Jointer bta (Object partner1, Object partner2) {
		return join(" between ", partner1).join(" and ", partner2);
	}

	public Jointer plus () {
		return plus(null);
	}

	public Jointer subtract () {
		return subtract(null);
	}

	public Jointer multiply () {
		return multiply(null);
	}

	public Jointer divide () {
		return divide(null);
	}

	public Jointer and (Filter<?> filter) {
		return join(" and ", filter);
	}

	public Jointer and () {
		return join(" and ", null);
	}

	public Jointer or (Filter<?> filter) {
		return join(" or ", filter);
	}

	public Jointer or () {
		return join(" or ", null);
	}

	/*****************************************************************************/

	public void setPrev (Jointer prev) {
		this.prev = prev;
	}

	public void setOperator (String operator) {
		this.operator = operator;
	}

	public Jointer setNext(Jointer next){
		return this.next = next;
	}

}
