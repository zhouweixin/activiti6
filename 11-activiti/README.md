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