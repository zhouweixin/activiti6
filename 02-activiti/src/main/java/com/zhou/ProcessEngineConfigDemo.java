package com.zhou;

import org.junit.Test;

import java.io.InputStream;

import org.activiti.engine.*;

/**
 * 流程引擎配置对象
 * 
 * @author zhouweixin
 *
 */
public class ProcessEngineConfigDemo {
	/**
	 * 读取默认的配置文件
	 */
	@Test
	public void test1() {
		// 默认加载:activiti.cfg.xml文件, bean为processEngineConfiguration
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResourceDefault();

		System.out.println("test1-引擎名称: " + config.getProcessEngineName());
		System.out.println();
	}

	/**
	 * 读取自定义的配置文件和bean名
	 */
	@Test
	public void test2() {
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("my-activiti-cfg.xml", "myProcessEngineConfiguration");

		System.out.println("test2-引擎名称: " + config.getProcessEngineName());
		System.out.println();
	}

	/**
	 * 通过输入流读取
	 */
	@Test
	public void test3() {
		try {
			ClassLoader classLoader = ProcessEngineConfigDemo.class.getClassLoader();
			InputStream fis = classLoader.getResourceAsStream("my-activiti-cfg.xml");
			ProcessEngineConfiguration config = ProcessEngineConfiguration
					.createProcessEngineConfigurationFromInputStream(fis, "myProcessEngineConfiguration");
			System.out.println("test3-引擎名称: " + config.getProcessEngineName());
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过流程配置实例类
	 */
	@Test
	public void test4() {
		// 不需要加载配置文件,使用默认值
		ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		
		// 默认用的是h2内存数据库
		System.out.println("test4-dbschemaUpdate: " + config.getDatabaseSchemaUpdate());
		System.out.println("test4-jdbcUrl: " + config.getJdbcUrl());
		System.out.println("test4-jdbcDriver: " + config.getJdbcDriver());
		System.out.println();
	}
	
	/**
	 * 通过流程配置实例类
	 */
	@Test
	public void test5() {
		// 不需要加载配置文件,使用默认值
		// StandaloneInMemProcessEngineConfiguration类的父类是StandaloneProcessEngineConfiguration
		ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		
		// 默认用的是h2内存数据库
		System.out.println("test5-dbschemaUpdate: " + config.getDatabaseSchemaUpdate());
		System.out.println("test5-jdbcUrl: " + config.getJdbcUrl());
		System.out.println("test5-jdbcDriver: " + config.getJdbcDriver());
		System.out.println();
	}
}
