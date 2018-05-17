package com.zhou;

import java.util.Map;

import org.activiti.engine.*;
import org.junit.Test;

/**
 * 创建流程引擎对象
 * 
 * @author zhouweixin
 *
 */
public class Main {
	/**
	 * 默认方法创建
	 */
	@Test
	public void test1() {
		// 默认加载resource下的activiti.cfg.xml配置文件
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println("test1-引擎名称: " + processEngine.getName());
	}

	/**
	 * 实现的效果同test1, 是test1内部执行的细节
	 */
	@Test
	public void test2() {
		// 下面是test1的执行细节, 当执行init函数时, 所有流程引擎对象都会由ProcessEngines的Map来管理
		ProcessEngines.init();
		Map<String, ProcessEngine> processEngines = ProcessEngines.getProcessEngines();
		
		if(processEngines.containsKey("default")) {
			ProcessEngine processEngine = processEngines.get("default");
			System.out.println("test2-引擎名称: " + processEngine.getName());
		}
	}
	
	/**
	 * 通过配置创建
	 */
	@Test
	public void test3() {
		// 加载配置文件
		ProcessEngineConfiguration config = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("my.cfg.xml", "myConfig");
		
		// 修改名称
		config.setProcessEngineName("myProcessEngine");
		ProcessEngine processEngine = config.buildProcessEngine();
		System.out.println("test3-流程引擎\n"+ProcessEngines.getProcessEngines());
		processEngine.close();
	}
	
	/**
	 * 流程引擎的注册和注销
	 */
	@Test
	public void test4() {
		// 加载的同时会注册流程引擎
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		System.out.println("test4-加载后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
		System.out.println(ProcessEngines.getProcessEngines());
		
		// 注销
		ProcessEngines.unregister(processEngine);
		System.out.println("test4-注销后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
		System.out.println(ProcessEngines.getProcessEngines());
		processEngine.close();
		
		// 设置名称重新创建
		ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		processEngineConfiguration.setProcessEngineName("newName");
		processEngine = processEngineConfiguration.buildProcessEngine();
		
		// 注册		
		ProcessEngines.registerProcessEngine(processEngine);
		System.out.println("test4-注册后-流程引擎对象数量 : " + ProcessEngines.getProcessEngines().size());
		System.out.println(ProcessEngines.getProcessEngines());
		processEngine.close();
	}
}
