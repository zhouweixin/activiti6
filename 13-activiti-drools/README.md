# 工作流引擎activiti整合规则引擎drools
---

消费打折实例

## 1.创建一个规则文件

```drl
//created on: 2018-5-29
package com.zhou

rule "DiscountRuleGold"

    when
        $m : Member(identity == "gold")
    then
    	System.out.println("黄金会员打7折");
        $m.setDiscount(0.7);

end

rule "DiscountRuleSilver"

    when
        $m : Member(identity == "silver")
    then
    	System.out.println("白银会员打8折");
        $m.setDiscount(0.8);

end
```

对应的会员类为: Member.java

```java
package com.zhou;

import java.io.Serializable;

/**
 * @Author: zhouweixin
 * @Description: 用户信息
 * @Date: Created in 下午2:53:22 2018年5月29日
 */
public class Member implements Serializable{
	private static final long serialVersionUID = -5516535573308741979L;
	// 身份
	private String identity;
	// 消费金额
	private double acount;
	// 折扣
	private double discount = 1;
	// 打折后金额
	private double afterAcount;	

	public Member(String identity, double acount) {
		super();
		this.identity = identity;
		this.acount = acount;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public double getAcount() {
		return acount;
	}

	public void setAcount(double acount) {
		this.acount = acount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
		// 计算折扣后的金额
		this.afterAcount = this.discount * this.acount;
	}

	public double getAfterAcount() {
		return afterAcount;
	}

	public void setAfterAcount(double afterAcount) {
		this.afterAcount = afterAcount;
	}

	@Override
	public String toString() {
		return "Member [身份=" + identity + ", 消费金额=" + acount + ", 折扣=" + discount + ", 实付金额=" + afterAcount + "]";
	}
}
```


## 2.创建流程文件

```xml
<process id="discount" name="discount" isExecutable="true">
	<startEvent id="startevent1" name="Start"></startEvent>
	<userTask id="usertask1" name="录入销售信息"></userTask>

	<!-- 指定输入输出参数 -->
	<businessRuleTask id="businessruletask1"
		name="进行优惠打折" activiti:ruleVariablesInput="${member1}, ${member2}"
		activiti:resultVariable="members"></businessRuleTask>

	<!-- 指定输出委托 -->
	<serviceTask id="servicetask1" name="输出结果"
		activiti:class="com.zhou.PrintDelegate"></serviceTask>
	<endEvent id="endevent1" name="End"></endEvent>

	<sequenceFlow id="flow1" sourceRef="startevent1"
		targetRef="usertask1"></sequenceFlow>
	<sequenceFlow id="flow2" sourceRef="usertask1"
		targetRef="businessruletask1"></sequenceFlow>
	<sequenceFlow id="flow3" sourceRef="businessruletask1"
		targetRef="servicetask1"></sequenceFlow>
	<sequenceFlow id="flow4" sourceRef="servicetask1"
		targetRef="endevent1"></sequenceFlow>
</process>
```

对应的委托类: PrintDelegate.java

```java
public class PrintDelegate implements JavaDelegate {
	public void execute(DelegateExecution execution) {
		System.out.println(execution.getVariable("members"));
	}
}
```

## 3.在activiti.cfg.xml里配置rule部署

```xml
<!-- 流程引擎配置的bean -->
<bean id="processEngineConfiguration"
	class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
	<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/actdb" />
	<property name="jdbcDriver" value="com.mysql.jdbc.Driver" />
	<property name="jdbcUsername" value="root" />
	<property name="jdbcPassword" value="root" />
	<property name="databaseSchemaUpdate" value="true" />
	
	<!-- 添加规则部署的配置 -->
	<property name="customPostDeployers">
		<list>
			<bean class="org.activiti.engine.impl.rules.RulesDeployer"/>
		</list>
	</property>
</bean>
```

## 4.测试类MemberTest.java

```java
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
```

## 5.输出结果

```
黄金会员打7折
白银会员打8折
[Member [身份=silver, 消费金额=300.0, 折扣=0.8, 实付金额=240.0], Member [身份=gold, 消费金额=300.0, 折扣=0.7, 实付金额=210.0]]
```
