package com.zhou;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
		ProcessDefinition processDef = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.singleResult();

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
		System.out.println("test1: 流程实例的个数" + count);
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
		System.out.println("test2: 流程实例的个数" + count);
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
		System.out.println("test3: 流程实例的个数" + count);
	}

	/**
	 * 流程参数的设置与查询: 流程, 执行流
	 */
	@Test
	public void test4() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		// 任务服务
		TaskService taskService = engine.getTaskService();

		// 部署流程
		repositoryService.createDeployment().addClasspathResource("VacationAuditProcess.bpmn").deploy();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("days", 7);
		// 启动流程
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationProcessId", map);

		// 通过流程实例查询任务
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		for (Task task : tasks) {
			// 查询执行流
			Execution exe = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
			if ("Manager Audit".equals(task.getName())) {
				// 查询参数
				int days = (Integer) runtimeService.getVariable(exe.getId(), "days");
				System.out.println(task.getName() + "--查到参数: days = " + days);

				// 审核结果
				runtimeService.setVariableLocal(exe.getId(), "m-audit", "不同意");
				System.out.println(
						task.getName() + "--添加参数: m-audit = " + runtimeService.getVariable(exe.getId(), "m-audit"));
				System.out.println(task.getName() + "--添加参数: m-audit = "
						+ runtimeService.getVariableLocal(exe.getId(), "m-audit"));

				// 添加参数
				map = new HashMap<String, Object>();
				map.put("manager-result", "不同意");
				taskService.complete(task.getId(), map);
				System.out.println(task.getName() + "--添加参数: manager-result = "
						+ runtimeService.getVariable(exe.getId(), "manager-result"));
				System.out.println(task.getName() + "--添加参数: manager-result = "
						+ runtimeService.getVariableLocal(exe.getId(), "manager-result"));
			} else if ("HR Audit".equals(task.getName())) {
				// 查询参数
				int days = (Integer) runtimeService.getVariable(exe.getId(), "days");
				System.out.println(task.getName() + "--查到参数: days = " + days);

				// 审核结果
				runtimeService.setVariableLocal(exe.getId(), "HR-audit", "同意");
				System.out.println(
						task.getName() + "--添加参数: HR-audit = " + runtimeService.getVariable(exe.getId(), "HR-audit"));
				System.out.println(task.getName() + "--添加参数: HR-audit = "
						+ runtimeService.getVariableLocal(exe.getId(), "HR-audit"));

				// 添加参数
				map = new HashMap<String, Object>();
				map.put("HR-result", "同意");
				taskService.complete(task.getId(), map);
				System.out.println(
						task.getName() + "--添加参数: HR-result = " + runtimeService.getVariable(exe.getId(), "HR-result"));
				System.out.println(task.getName() + "--添加参数: HR-result = "
						+ runtimeService.getVariableLocal(exe.getId(), "HR-result"));
			}

			Task endTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			Execution endExecution = runtimeService.createExecutionQuery().executionId(endTask.getExecutionId())
					.singleResult();
			System.out.println(
					task.getName() + "--查到的参数: days = " + runtimeService.getVariable(endExecution.getId(), "days"));
			System.out.println(task.getName() + "--查到的参数: days = "
					+ runtimeService.getVariableLocal(endExecution.getId(), "days"));
			System.out.println(task.getName() + "--查到的参数: m-audit = "
					+ runtimeService.getVariable(endExecution.getId(), "m-audit"));
			System.out.println(task.getName() + "--查到的参数: m-audit = "
					+ runtimeService.getVariableLocal(endExecution.getId(), "m-audit"));
			System.out.println(task.getName() + "--查到的参数: manager-result = "
					+ runtimeService.getVariable(endExecution.getId(), "manager-result"));
			System.out.println(task.getName() + "--查到的参数: manager-result = "
					+ runtimeService.getVariableLocal(endExecution.getId(), "manager-result"));
			System.out.println(task.getName() + "--查到的参数: HR-audit = "
					+ runtimeService.getVariable(endExecution.getId(), "HR-audit"));
			System.out.println(task.getName() + "--查到的参数: HR-audit = "
					+ runtimeService.getVariableLocal(endExecution.getId(), "HR-audit"));
			System.out.println(task.getName() + "--查到的参数: HR-result = "
					+ runtimeService.getVariable(endExecution.getId(), "HR-result"));
			System.out.println(task.getName() + "--查到的参数: HR-result = "
					+ runtimeService.getVariableLocal(endExecution.getId(), "HR-result"));
		}
	}

	/**
	 * 流程操作: 触发事件--receive task
	 */
	@Test
	public void test5() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		// 任务服务
		TaskService taskService = engine.getTaskService();
		
		// 部署流程
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("triggerProcess.bpmn").deploy();
		// 查询流程定义
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.singleResult();
		// 创建流程实例
		runtimeService.startProcessInstanceById(pd.getId());

		Execution exe1 = runtimeService.createExecutionQuery().activityId("receivetask1").singleResult();
		if (exe1 != null) {
			System.out.println("当前任务: receivetask1");
			runtimeService.trigger(exe1.getId());
		}

		List<Task> list = taskService.createTaskQuery().list();

		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("当前任务: " + task.getName());
				taskService.complete(task.getId());
			}
		}
	}
}
