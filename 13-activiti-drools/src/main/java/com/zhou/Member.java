package com.zhou;

import java.io.Serializable;

/**
 * @Author: zhouweixin
 * @Description: 用户信息
 * @Date: Created in 下午2:53:22 2018年5月29日
 */
public class Member implements Serializable{
	private static final long serialVersionUID = -5516535573308741979L;
	// 身份
	private String identity;
	// 消费金额
	private double acount;
	// 折扣
	private double discount = 1;
	// 打折后金额
	private double afterAcount;	

	public Member(String identity, double acount) {
		super();
		this.identity = identity;
		this.acount = acount;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public double getAcount() {
		return acount;
	}

	public void setAcount(double acount) {
		this.acount = acount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
		// 计算折扣后的金额
		this.afterAcount = this.discount * this.acount;
	}

	public double getAfterAcount() {
		return afterAcount;
	}

	public void setAfterAcount(double afterAcount) {
		this.afterAcount = afterAcount;
	}

	@Override
	public String toString() {
		return "Member [身份=" + identity + ", 消费金额=" + acount + ", 折扣=" + discount + ", 实付金额=" + afterAcount + "]";
	}
}
