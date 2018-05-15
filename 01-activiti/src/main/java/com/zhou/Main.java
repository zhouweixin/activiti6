package com.zhou;

import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class Main {

	// 部署流程:存储到表"act_ge_bytearray"
	@Test
	public void test1() {
		// 加载默认引擎(加载默认配置文件activiti.cfg.xml)
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 存储服务
		RepositoryService repositoryService = processEngine.getRepositoryService();

		// 存储服务部署流程
		repositoryService.createDeployment().addClasspathResource("MyProcess.bpmn").deploy();

		if (processEngine != null) {
			processEngine.close();
		}

		System.out.println("测试结束");
	}

	// 启动并运行流程
	@Test
	public void test2() {
		System.out.println("\n================测试开始==================\n");
		
		// 加载默认引擎(加载默认配置文件activiti.cfg.xml)
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

		// 运行时服务
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// MyProcess.bpmn -> process -> id
		runtimeService.startProcessInstanceByKey("myProcess");

		// 任务服务
		TaskService taskService = processEngine.getTaskService();

		// 查询当前任务
		Task task1 = taskService.createTaskQuery().singleResult();
		if (task1 != null) {
			// 打印当前任务名称
			System.out.println("当前任务: " + task1.getName());
			// 完成当前任务
			taskService.complete(task1.getId());
		}

		// 查询当前任务
		Task task2 = taskService.createTaskQuery().singleResult();
		if (task2 != null) {
			// 打印当前任务名称
			System.out.println("当前任务: " + task2.getName());
			// 完成当前任务
			taskService.complete(task2.getId());
		}

		// 查询当前任务
		Task task3 = taskService.createTaskQuery().singleResult();
		if (task3 != null) {
			// 打印当前任务名称
			System.out.println("当前任务: " + task3.getName());
			// 完成当前任务
			taskService.complete(task3.getId());
		}else {
			System.out.println("当前无任务!");
		}

		if (processEngine != null) {
			processEngine.close();
		}


		System.out.println("\n================测试结束==================\n");
	}

}
