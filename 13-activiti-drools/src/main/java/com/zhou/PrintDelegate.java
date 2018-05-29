package com.zhou;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 下午3:17:37 2018年5月29日
 */
public class PrintDelegate implements JavaDelegate {
	public void execute(DelegateExecution execution) {
		System.out.println(execution.getVariable("members"));
	}
}
