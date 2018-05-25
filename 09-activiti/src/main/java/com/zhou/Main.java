package com.zhou;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.DataObject;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 为任务添加属性变量
 * 
 * @author zhouweixin
 *
 */
public class Main {
	/**
	 * 为任务添加属性变量: 基本类型
	 */
	@Test
	public void test1() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();
		Task task = taskService.newTask();
		taskService.saveTask(task);

		taskService.setVariable(task.getId(), "var_boolean", false);
		taskService.setVariable(task.getId(), "var_date", new Date());
		taskService.setVariable(task.getId(), "var_double", 2.3);
		taskService.setVariable(task.getId(), "var_int", 10);
		taskService.setVariable(task.getId(), "var_long", 200L);
		taskService.setVariable(task.getId(), "var_null", null);
		taskService.setVariable(task.getId(), "var_short", (short) 1);
		taskService.setVariable(task.getId(), "var_String", "hello");

		System.out.println("var_boolean = " + taskService.getVariable(task.getId(), "var_boolean"));
		System.out.println("var_date = " + taskService.getVariable(task.getId(), "var_date"));
		System.out.println("var_double = " + taskService.getVariable(task.getId(), "var_double"));
		System.out.println("var_int = " + taskService.getVariable(task.getId(), "var_int"));
		System.out.println("var_long = " + taskService.getVariable(task.getId(), "var_long"));
		System.out.println("var_null = " + taskService.getVariable(task.getId(), "var_null"));
		System.out.println("var_short = " + taskService.getVariable(task.getId(), "var_short"));
		System.out.println("var_String = " + taskService.getVariable(task.getId(), "var_String"));
		System.out.println("test1测试结束");
	}

	/**
	 * 为任务添加属性变量: 自定义类型
	 */
	@Test
	public void test2() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();
		Task task = taskService.newTask();
		taskService.saveTask(task);

		TestObjectVariable testObjectVariable = new TestObjectVariable(1, "张三", 18);
		taskService.setVariable(task.getId(), "testObjectVariable", testObjectVariable);
		System.out.println("testObjectVariable = " + taskService.getVariable(task.getId(), "testObjectVariable"));

		System.out.println("test2测试结束");
	}

	/**
	 * 为任务添加属性变量: Map类型
	 */
	@Test
	public void test3() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();
		Task task = taskService.newTask();
		taskService.saveTask(task);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 2);
		map.put("name", "李四");
		map.put("age", 20);
		taskService.setVariable(task.getId(), "map", map);
		System.out.println("map = " + taskService.getVariable(task.getId(), "map"));

		System.out.println("test3测试结束");
	}

	/**
	 * 为任务添加属性变量: 数据对象(dataObject)
	 */
	@Test
	public void test4() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		TaskService taskService = engine.getTaskService();

		// 部署流程
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("data-object-process.bpmn")
				.deploy();
		// 流程定义
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.singleResult();

		// 启动流程
		ProcessInstance process = runtimeService.startProcessInstanceById(pd.getId());

		// 查询流程任务
		Task task = taskService.createTaskQuery().processInstanceId(process.getId()).singleResult();

		Map<String, DataObject> dataObjects = taskService.getDataObjects(task.getId());

		dataObjects.forEach((key, value) -> {
			System.out.println(String.format("%s-%s", key, value.getValue()));
		});
		System.out.println("test4测试结束");
	}
}

/**
 * 测试类
 * 
 * @author zhouweixin
 *
 */
class TestObjectVariable implements Serializable {
	private static final long serialVersionUID = -5273687758046001432L;

	private int id;
	private String name;
	private int age;

	public TestObjectVariable(int id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "TestObjectVariable [id=" + id + ", name=" + name + ", age=" + age + "]";
	}

}
