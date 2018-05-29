package com.zhou;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 下午3:15:14 2018年5月29日
 */
public class MemberTest {
	public static void main(String[] agrs) {
		// 加载引擎
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		// 存储服务
		RepositoryService repositoryService = engine.getRepositoryService();
		// 运行时服务
		RuntimeService runtimeService = engine.getRuntimeService();
		// 任务服务
		TaskService taskService = engine.getTaskService();

		// 部署流程和规则
		repositoryService.createDeployment().addClasspathResource("discount.bpmn").addClasspathResource("discount.drl")
				.deploy();

		// 启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("discount");

		// 查询任务
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();

		// 创建参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("member1", new Member("gold", 300));
		map.put("member2", new Member("silver", 300));
		// 用户完成任务
		taskService.complete(task.getId(), map);
	}
}
