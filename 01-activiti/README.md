# activiti6 实例一

### 1.创建maven空项目

### 2.pom.xml里添加依赖

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.zhou</groupId>
	<artifactId>01-activiti</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<dependencies>
		<!-- Activiti6 依赖 -->
		<dependency>
			<groupId>org.activiti</groupId>
			<artifactId>activiti-engine</artifactId>
			<version>6.0.0</version>
		</dependency>

		<!-- mysql 依赖 -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.46</version>
		</dependency>

		<!-- 日志 依赖 -->
		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-log4j12</artifactId>
			<version>1.7.25</version>
		</dependency>

		<dependency>
			<groupId>apache-log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.14</version>
		</dependency>

		<!-- 单元测试 依赖 -->
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>4.12</version>
		</dependency>
	</dependencies>
</project>
```

### 3.resource里添加activiti.cfg.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- 流程引擎配置的bean -->
	<bean id="processEngineConfiguration"
		class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
		<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/actdb" />
		<property name="jdbcDriver" value="com.mysql.jdbc.Driver" />
		<property name="jdbcUsername" value="root" />
		<property name="jdbcPassword" value="root" />
		<property name="databaseSchemaUpdate" value="true" />
	</bean>
</beans>
```

### 4.resource里添加log4j.properties
```properties
log4j.rootLogger=INFO, stdout

# Console Appender
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern= %d{hh:mm:ss,SSS} [%t] %-5p %c %x - %m%n

# Custom tweaks
log4j.logger.com.codahale.metrics=WARN
log4j.logger.com.ryantenney=WARN
log4j.logger.com.zaxxer=WARN
log4j.logger.org.apache=WARN
log4j.logger.org.hibernate=WARN
log4j.logger.org.hibernate.engine.internal=WARN
log4j.logger.org.hibernate.validator=WARN
log4j.logger.org.springframework=WARN
log4j.logger.org.springframework.web=WARN
log4j.logger.org.springframework.security=WARN
```

### 5.resource里添加MyProcess.bpmn
```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
	xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
	typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath"
	targetNamespace="http://www.activiti.org/test">
	<process id="myProcess" name="My process" isExecutable="true">
		<startEvent id="startevent1" name="Start"></startEvent>
		<endEvent id="endevent1" name="End"></endEvent>
		<userTask id="usertask1" name="task1"></userTask>
		<userTask id="usertask2" name="task2"></userTask>
		<sequenceFlow id="flow1" sourceRef="startevent1"
			targetRef="usertask1"></sequenceFlow>
		<sequenceFlow id="flow2" sourceRef="usertask1" targetRef="usertask2"></sequenceFlow>
		<sequenceFlow id="flow3" sourceRef="usertask2" targetRef="endevent1"></sequenceFlow>
	</process>
	<bpmndi:BPMNDiagram id="BPMNDiagram_myProcess">
		<bpmndi:BPMNPlane bpmnElement="myProcess" id="BPMNPlane_myProcess">
			<bpmndi:BPMNShape bpmnElement="startevent1"
				id="BPMNShape_startevent1">
				<omgdc:Bounds height="35.0" width="35.0" x="30.0" y="90.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="endevent1" id="BPMNShape_endevent1">
				<omgdc:Bounds height="35.0" width="35.0" x="460.0" y="90.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="usertask1" id="BPMNShape_usertask1">
				<omgdc:Bounds height="55.0" width="105.0" x="130.0" y="80.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNShape bpmnElement="usertask2" id="BPMNShape_usertask2">
				<omgdc:Bounds height="55.0" width="105.0" x="280.0" y="80.0"></omgdc:Bounds>
			</bpmndi:BPMNShape>
			<bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
				<omgdi:waypoint x="65.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="130.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
			<bpmndi:BPMNEdge bpmnElement="flow2" id="BPMNEdge_flow2">
				<omgdi:waypoint x="235.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="280.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
			<bpmndi:BPMNEdge bpmnElement="flow3" id="BPMNEdge_flow3">
				<omgdi:waypoint x="385.0" y="107.0"></omgdi:waypoint>
				<omgdi:waypoint x="460.0" y="107.0"></omgdi:waypoint>
			</bpmndi:BPMNEdge>
		</bpmndi:BPMNPlane>
	</bpmndi:BPMNDiagram>
</definitions>
```

### 6.main里添加com.zhou.Main.java

```java
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
```

### 7.test1()运行结果
```
测试结束
```
### 8.test2()运行结果
```
================测试开始==================
当前任务: task1
当前任务: task2
当前无任务!
================测试开始==================
```