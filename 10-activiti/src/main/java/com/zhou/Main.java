package com.zhou;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * @Author: zhouweixin
 * @Description: 任务附件管理
 * @Date: Created in 下午1:03:09 2018年5月25日
 */
public class Main {
	/**
	 * 任务附件的添加了查询
	 */
	@Test
	public void test1() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 任务服务
		TaskService taskService = engine.getTaskService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		
		// 部署流程
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();
		
		// 流程定义
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
		
		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(pd.getId());
		
		// 查询当前任务
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		
		// (1) 添加文本附件
		taskService.createAttachment("类型: 文本", task.getId(), processInstance.getId(), "名称1", "描述1", "内容1");
		// (2) 添加图片附件
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("MyProcess.png");
		taskService.createAttachment("类型: 图片", task.getId(), processInstance.getId(), "名称2", "描述2", is);
		
		System.out.println("test1测试结束");
	}

}
