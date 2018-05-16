package com.zhou;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.CommandInterceptor;

public class MyProcessEngineConfiguration extends ProcessEngineConfigurationImpl {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public CommandInterceptor createTransactionInterceptor() {
		return null;
	}
}
