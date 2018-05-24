package com.zhou;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class Main {

	/**
	 * 保存任务
	 */
	@Test
	public void test1() {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();

		// 不指定id: 保存后会自动生成id
		Task task1 = taskService.newTask();
		task1.setName("task1");
		taskService.saveTask(task1);

		// 指定id
		Task task2 = taskService.newTask("1001");
		task2.setName("task2");
		taskService.saveTask(task2);

		System.out.println("test1: ");
		System.out.println(String.format("task1.id = %s, task1.name = %s", task1.getId(), task1.getName()));
		System.out.println(String.format("task2.id = %s, task2.name = %s", task2.getId(), task2.getName()));
	}

	/**
	 * 任务权限:任务候选人
	 */
	@Test
	public void test2() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 任务服务
		TaskService taskService = engine.getTaskService();

		// 创建任务1
		Task task1 = taskService.newTask();
		task1.setName("task1");
		taskService.saveTask(task1);

		// 创建任务2
		Task task2 = taskService.newTask();
		task2.setName("task2");
		taskService.saveTask(task2);

		// 身份服务
		IdentityService identityService = engine.getIdentityService();

		// 创建用户1
		User user1 = identityService.newUser("");
		user1.setId(null);
		user1.setLastName("user1");
		identityService.saveUser(user1);

		// 创建用户2
		User user2 = identityService.newUser("");
		user2.setId(null);
		user2.setLastName("user2");
		identityService.saveUser(user2);

		// 创建用户3
		User user3 = identityService.newUser("");
		user3.setId(null);
		user3.setLastName("user3");
		identityService.saveUser(user3);

		// 添加候选用户
		taskService.addCandidateUser(task1.getId(), user1.getId());
		taskService.addCandidateUser(task1.getId(), user2.getId());
		taskService.addCandidateUser(task2.getId(), user2.getId());
		taskService.addCandidateUser(task2.getId(), user3.getId());

		// 查询用户的任务
		List<Task> user1tasks = taskService.createTaskQuery().taskCandidateUser(user1.getId()).list();
		List<Task> user2tasks = taskService.createTaskQuery().taskCandidateUser(user2.getId()).list();
		List<Task> user3tasks = taskService.createTaskQuery().taskCandidateUser(user3.getId()).list();

		for (Task task : user1tasks) {
			System.out.println(String.format("user1-%s", task.getName()));
		}
		for (Task task : user2tasks) {
			System.out.println(String.format("user2-%s", task.getName()));
		}
		for (Task task : user3tasks) {
			System.out.println(String.format("user3-%s", task.getName()));
		}
	}

	/**
	 * 任务权限: 任务持有人
	 */
	@Test
	public void test3() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 任务服务
		TaskService taskService = engine.getTaskService();

		// 创建任务1
		Task task1 = taskService.newTask();
		task1.setName("task1");
		taskService.saveTask(task1);

		// 创建任务2
		Task task2 = taskService.newTask();
		task2.setName("task2");
		taskService.saveTask(task2);

		// 创建任务3
		Task task3 = taskService.newTask();
		task3.setName("task3");
		taskService.saveTask(task3);

		// 身份服务
		IdentityService identityService = engine.getIdentityService();

		// 创建用户1
		User user1 = identityService.newUser("");
		user1.setId(null);
		user1.setLastName("user1");
		identityService.saveUser(user1);

		// 创建用户2
		User user2 = identityService.newUser("");
		user2.setId(null);
		user2.setLastName("user2");
		identityService.saveUser(user2);

		// 设置任务代理人: 同时只能有一个任务持有人
		taskService.setOwner(task1.getId(), user1.getId());
		taskService.setOwner(task2.getId(), user1.getId());
		taskService.setOwner(task2.getId(), user2.getId());// 这个会把上个持有人覆盖
		taskService.setOwner(task3.getId(), user2.getId());

		// 查询用户代理的任务
		List<Task> user1tasks = taskService.createTaskQuery().taskOwner(user1.getId()).list();
		List<Task> user2tasks = taskService.createTaskQuery().taskOwner(user2.getId()).list();

		for (Task task : user1tasks) {
			System.out.println(String.format("user1-%s", task.getName()));
		}
		for (Task task : user2tasks) {
			System.out.println(String.format("user2-%s", task.getName()));
		}
	}

	/**
	 * 任务权限: 任务持有人
	 */
	@Test
	public void test4() {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 任务服务
		TaskService taskService = engine.getTaskService();

		// 创建任务1
		Task task1 = taskService.newTask();
		task1.setName("task1");
		taskService.saveTask(task1);

		// 创建任务2
		Task task2 = taskService.newTask();
		task2.setName("task2");
		taskService.saveTask(task2);

		// 创建任务3
		Task task3 = taskService.newTask();
		task3.setName("task3");
		taskService.saveTask(task3);

		// 身份服务
		IdentityService identityService = engine.getIdentityService();

		// 创建用户1
		User user1 = identityService.newUser("");
		user1.setId(null);
		user1.setLastName("user1");
		identityService.saveUser(user1);

		// 创建用户2
		User user2 = identityService.newUser("");
		user2.setId(null);
		user2.setLastName("user2");
		identityService.saveUser(user2);

		// 设置任务代理人: 同时只能有一个任务代理人
		taskService.setAssignee(task1.getId(), user1.getId());
		taskService.setAssignee(task2.getId(), user1.getId());
		taskService.setAssignee(task2.getId(), user2.getId());// 这个会把上个代理人覆盖
		taskService.setAssignee(task3.getId(), user2.getId());

		// 查询用户代理的任务
		List<Task> user1tasks = taskService.createTaskQuery().taskAssignee(user1.getId()).list();
		List<Task> user2tasks = taskService.createTaskQuery().taskAssignee(user2.getId()).list();

		for (Task task : user1tasks) {
			System.out.println(String.format("user1-%s", task.getName()));
		}
		for (Task task : user2tasks) {
			System.out.println(String.format("user2-%s", task.getName()));
		}
	}
}
