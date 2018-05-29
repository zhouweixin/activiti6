# 流程控制
---

执行流

接口：Execution
子接口：ExecutionEntity
实现类：ExecutionEntityImpl

- id: 主键
- revision： 版本号
- businessKey： 流程实例的业务主键
- parentId：父执行流的ID
- processDefinitionId：流程定义的ID
- superExecutionId：父执行流的ID
- activityId：当前执行流的动作
- isActive：活跃
- isConcurrent：并行
- isScope：执行流的范围
- isEventScope：事件范围
- suspensionState：流程中断状态，1活跃；2中断

## 1.启动流程的三种主要方法

### (1) 通过流程定义的id启动流程:startProcessInstanceById

```java
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
```

结果

```
test1: 流程实例的个数4
```

### (2) 通过流程文件的节点id启动流程:startProcessInstanceByKey

```java
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
```

结果

```
test2: 流程实例的个数8
```

### (3) 通过消息事件启动流程:startProcessInstanceByKey

流程文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:activiti="http://activiti.org/bpmn"
	xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
	xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
	xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
	typeLanguage="http://www.w3.org/2001/XMLSchema"
	expressionLanguage="http://www.w3.org/1999/XPath"
	targetNamespace="http://www.activiti.org/test">
	
	<!-- 定义消息 -->
	<message id="startMsgId" name="startMsgName"></message>
	
	<process id="myProcess" name="My process" isExecutable="true">
		<startEvent id="startevent1" name="Start">
			<!-- 引用消息的id -->
			<messageEventDefinition messageRef="startMsgId"></messageEventDefinition>
		</startEvent>
		<endEvent id="endevent1" name="End"></endEvent>
		<userTask id="usertask1" name="task1"></userTask>
		<userTask id="usertask2" name="task2"></userTask>
		<sequenceFlow id="flow1" sourceRef="startevent1"
			targetRef="usertask1"></sequenceFlow>
		<sequenceFlow id="flow2" sourceRef="usertask1"
			targetRef="usertask2"></sequenceFlow>
		<sequenceFlow id="flow3" sourceRef="usertask2"
			targetRef="endevent1"></sequenceFlow>
	</process>
	<bpmndi:BPMNDiagram id="BPMNDiagram_myProcess">
		<bpmndi:BPMNPlane bpmnElement="myProcess"
			id="BPMNPlane_myProcess">
			<bpmndi:BPMNShape bpmnElement="startevent1"
				id="BPMNShape_startevent1">
				<omgdc:Bounds height="35.0" width="35.0" x="30.0"
					y="90.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="endevent1"
				id="BPMNShape_endevent1">
				<omgdc:Bounds height="35.0" width="35.0" x="440.0"
					y="90.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="usertask1"
				id="BPMNShape_usertask1">
				<omgdc:Bounds height="55.0" width="105.0" x="130.0"
					y="80.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="usertask2"
				id="BPMNShape_usertask2">
				<omgdc:Bounds height="55.0" width="105.0" x="280.0"
					y="80.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNEdge bpmnElement="flow1"
				id="BPMNEdge_flow1">
				<omgdi:waypoint x="65.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="130.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
			<bpmndi:BPMNEdge bpmnElement="flow2"
				id="BPMNEdge_flow2">
				<omgdi:waypoint x="235.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="280.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
			<bpmndi:BPMNEdge bpmnElement="flow3"
				id="BPMNEdge_flow3">
				<omgdi:waypoint x="385.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="440.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
		</bpmndi:BPMNPlane>
	</bpmndi:BPMNDiagram>
</definitions>
```

程序代码

```java
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
```

结果

```
test3: 流程实例的个数12
```

### (4) 流程参数的设置与查询

需要区分的概念: 流程, 执行流
需要区分的方法: setVariable, getVariable, setVariableLocal, getVariableLocal

- 一个流程包含一个或多个执行流

- setVariable设置的参数, 在流程未结束前都可用getVariable方法查到, 但不可用getVariableLocal方法查到

- setVariableLocal设置的参数, 只有本执行流可以查到, 但是getVariable和getVariableLocal方法都可查到

```java
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
		System.out.println(task.getName() + "--查到的参数: days = " + runtimeService.getVariable(endExecution.getId(), "days"));
		System.out.println(task.getName() + "--查到的参数: days = " + runtimeService.getVariableLocal(endExecution.getId(), "days"));
		System.out.println(task.getName() + "--查到的参数: m-audit = " + runtimeService.getVariable(endExecution.getId(), "m-audit"));
		System.out.println(task.getName() + "--查到的参数: m-audit = " + runtimeService.getVariableLocal(endExecution.getId(), "m-audit"));
		System.out.println(task.getName() + "--查到的参数: manager-result = " + runtimeService.getVariable(endExecution.getId(), "manager-result"));
		System.out.println(task.getName() + "--查到的参数: manager-result = " + runtimeService.getVariableLocal(endExecution.getId(), "manager-result"));
		System.out.println(task.getName() + "--查到的参数: HR-audit = " + runtimeService.getVariable(endExecution.getId(), "HR-audit"));
		System.out.println(task.getName() + "--查到的参数: HR-audit = " + runtimeService.getVariableLocal(endExecution.getId(), "HR-audit"));
		System.out.println(task.getName() + "--查到的参数: HR-result = " + runtimeService.getVariable(endExecution.getId(), "HR-result"));
		System.out.println(task.getName() + "--查到的参数: HR-result = " + runtimeService.getVariableLocal(endExecution.getId(), "HR-result"));
	}
}
```

结果

```
Manager Audit--查到参数: days = 7
Manager Audit--添加参数: m-audit = 不同意
Manager Audit--添加参数: m-audit = 不同意
Manager Audit--添加参数: manager-result = 不同意
Manager Audit--添加参数: manager-result = null
Manager Audit-- 查到的参数: days = 7
Manager Audit-- 查到的参数: days = null
Manager Audit-- 查到的参数: m-audit = null
Manager Audit-- 查到的参数: m-audit = null
Manager Audit-- 查到的参数: manager-result = 不同意
Manager Audit-- 查到的参数: manager-result = null
Manager Audit-- 查到的参数: HR-audit = null
Manager Audit-- 查到的参数: HR-audit = null
Manager Audit-- 查到的参数: HR-result = null
Manager Audit-- 查到的参数: HR-result = null
HR Audit--查到参数: days = 7
HR Audit--添加参数: HR-audit = 同意
HR Audit--添加参数: HR-audit = 同意
HR Audit--添加参数: HR-result = 同意
HR Audit--添加参数: HR-result = null
HR Audit-- 查到的参数: days = 7
HR Audit-- 查到的参数: days = null
HR Audit-- 查到的参数: m-audit = null
HR Audit-- 查到的参数: m-audit = null
HR Audit-- 查到的参数: manager-result = 不同意
HR Audit-- 查到的参数: manager-result = null
HR Audit-- 查到的参数: HR-audit = 同意
HR Audit-- 查到的参数: HR-audit = 同意
HR Audit-- 查到的参数: HR-result = 同意
HR Audit-- 查到的参数: HR-result = null
```

### (5) 流程操作

- 启动流程
- 发送信号
- 中断流程
- 激活流程





