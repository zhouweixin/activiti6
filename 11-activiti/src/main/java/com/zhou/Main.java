package com.zhou;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

/**
 * @Author: zhouweixin
 * @Description: 流程控制
 * @Date: Created in 下午9:26:56 2018年5月28日
 */
public class Main {
	/**
	 * 通过流程定义的id启动流程:startProcessInstanceById
	 */
	@Test
	public void test1() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		
		// 部署流程
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
		// 查询流程定义对象
		ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
		
		// 定义参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "张三");
		map.put("age", 18);
		
		// 通过流程定义的id启动流程实例:param1和param2为业务主键
		runtimeService.startProcessInstanceById(processDef.getId());// 流程定义的id
		runtimeService.startProcessInstanceById(processDef.getId(), "param1");
		runtimeService.startProcessInstanceById(processDef.getId(), map);
		runtimeService.startProcessInstanceById(processDef.getId(), "param2", map);
		
		// 查询流程实例的个数
		long count = runtimeService.createProcessInstanceQuery().count();
		System.out.println("test1: 流程实例的个数"+count);
	}
	
	/**
	 * 通过流程文件的节点id启动流程:startProcessInstanceByKey
	 */
	@Test
	public void test2() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		
		// 部署流程
		repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
		
		// 定义参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "张三");
		map.put("age", 18);
		
		// 通过流程定义的id启动流程实例:param1和param2为业务主键
		runtimeService.startProcessInstanceByKey("myProcess");// 流程文件里的id
		runtimeService.startProcessInstanceByKey("myProcess", "param1");
		runtimeService.startProcessInstanceByKey("myProcess", map);
		runtimeService.startProcessInstanceByKey("myProcess", "param2", map);
		
		// 查询流程实例的个数
		long count = runtimeService.createProcessInstanceQuery().count();
		System.out.println("test2: 流程实例的个数"+count);
	}
	
	/**
	 * 通过消息事件启动流程:startProcessInstanceByKey
	 */
	@Test
	public void test3() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		
		// 部署流程
		repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
		
		// 定义参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "张三");
		map.put("age", 18);
		
		// 通过流程定义的id启动流程实例:param1和param2为业务主键
		runtimeService.startProcessInstanceByMessage("startMsgName");// 消息的name
		runtimeService.startProcessInstanceByMessage("startMsgName", "param1");
		runtimeService.startProcessInstanceByMessage("startMsgName", map);
		runtimeService.startProcessInstanceByMessage("startMsgName", "param2", map);
		
		// 查询流程实例的个数
		long count = runtimeService.createProcessInstanceQuery().count();
		System.out.println("test3: 流程实例的个数"+count);
	}
}
