# activiti6 流程引擎的配置

环境:
- maven配置同项目**01-activiti**
- resources下添加my-activiti-cfg.xml, 内容如下
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

---

# 1.数据引擎配置对象的5种用法:ProcessEngineConfigDemo.java

### (1)读到默认的配置文件
```java
public void test1() {
	// 默认加载:activiti.cfg.xml文件, bean为processEngineConfiguration
	ProcessEngineConfiguration config = ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResourceDefault();

	System.out.println("test1-引擎名称: " + config.getProcessEngineName());
	System.out.println();
}
```

```
test1-引擎名称: default
```

### (2)读取自定义的配置文件
```java
public void test2() {
	ProcessEngineConfiguration config = ProcessEngineConfiguration
			.createProcessEngineConfigurationFromResource("my-activiti-cfg.xml", "myProcessEngineConfiguration");

	System.out.println("test2-引擎名称: " + config.getProcessEngineName());
	System.out.println();
}
```

```
test2-引擎名称: default
```

### (3)读取输入流的配置
```java
public void test3() {
	try {
		ClassLoader classLoader = ProcessEngineConfigDemo.class.getClassLoader();
		InputStream fis = classLoader.getResourceAsStream("my-activiti-cfg.xml");
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromInputStream(fis, "myProcessEngineConfiguration");
		System.out.println("test3-引擎名称: " + config.getProcessEngineName());
		System.out.println();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
```

```
test3-引擎名称: default
```

### (4)使用StandaloneInMemProcessEngineConfiguration
```java
public void test4() {
	// 不需要加载配置文件,使用默认值
	ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
	
	// 默认用的是h2内存数据库
	System.out.println("test4-dbschemaUpdate: " + config.getDatabaseSchemaUpdate());
	System.out.println("test4-jdbcUrl: " + config.getJdbcUrl());
	System.out.println("test4-jdbcDriver: " + config.getJdbcDriver());
	System.out.println();
}
```

```
test4-dbschemaUpdate: create-drop
test4-jdbcUrl: jdbc:h2:mem:activiti
test4-jdbcDriver: org.h2.Driver
```

### (5)使用StandaloneProcessEngineConfiguration
```java
public void test5() {
	// 不需要加载配置文件,使用默认值
	// StandaloneInMemProcessEngineConfiguration类的父类是StandaloneProcessEngineConfiguration
	ProcessEngineConfiguration config = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
	
	// 默认用的是h2内存数据库
	System.out.println("test5-dbschemaUpdate: " + config.getDatabaseSchemaUpdate());
	System.out.println("test5-jdbcUrl: " + config.getJdbcUrl());
	System.out.println("test5-jdbcDriver: " + config.getJdbcDriver());
	System.out.println();
}
```

```
test5-dbschemaUpdate: false
test5-jdbcUrl: jdbc:h2:tcp://localhost/~/activiti
test5-jdbcDriver: org.h2.Driver
```