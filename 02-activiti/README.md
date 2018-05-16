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
	
	// 创建引擎
	ProcessEngine processEngine = config.buildProcessEngine();
	System.out.println("test1-引擎名称: " + config.getProcessEngineName());
	System.out.println("test1-引擎名称: " + processEngine.getName());
	
	System.out.println();
}
```

```
test1-引擎名称: default
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

**继承关系**
- ProcessEngineConfiguration
    - ProcessEngineConfigurationImpl
        - SpringProcessConfiguration
        - JtaProcessEngineConfiguration
        - StandaloneProcessEngineConfiguration
            - StandaloneInMemProcessEngineConfiguration

# 2.数据源配置:DatabaseConfigDemo.java
### (1)DBCP数据源-配置文件 

pom.xml里添加DBCP依赖

```xml
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-dbcp2</artifactId>
	<version>2.1.1</version>
</dependency>
```

测试代码

```java
public void test1() {
	try {
		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("my-activiti-dbcp-1.xml");
		DataSource dataSource = config.getDataSource();
		System.out.println("test1-数据源对象 = " + dataSource);
		DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
		System.out.println("test1-测试连接 = " + metaData);
		System.out.println("test1-数据源类 = " + dataSource.getClass().getName());
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
```

```
test1-数据源对象 = org.apache.commons.dbcp2.BasicDataSource@5c5eefef
test1-测试连接 = org.apache.commons.dbcp2.DelegatingDatabaseMetaData@6f45df59
test1-数据源类 = org.apache.commons.dbcp2.BasicDataSource
```

### (2)DBCP数据源-代码

```java
public void test2() {
	try {
		// 创建数据源,并设置相应属性
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/actdb");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		System.out.println("test2-测试连接 = " + dataSource.getConnection().getMetaData());

		ProcessEngineConfiguration config = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("my-activiti-dbcp-2.xml");

		config.setDataSource(dataSource);

		System.out.println("test2-数据源对象 = " + dataSource);
		System.out.println("test2-数据源类 = " + dataSource.getClass().getName());
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
```

```
test2-测试连接 = org.apache.commons.dbcp2.DelegatingDatabaseMetaData@10d68fcd
test2-数据源对象 = org.apache.commons.dbcp2.BasicDataSource@d21a74c
test2-数据源类 = org.apache.commons.dbcp2.BasicDataSource
```

### (3)其他配置

**databaseSchemaUpdate**
- false(默认)
- true
- create-drop
- drop-create(官网文档没有)
	
**databaseType**(不配置也没有问题)
- h2
- mysql
- oracle
- postgres
- mssql
- db2
- ...

配置代码

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- 流程引擎配置的bean -->
	<bean id="myProcessEngineConfiguration"
		class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
		<property name="jdbcUrl" value="jdbc:mysql://localhost:3306/actdb" />
		<property name="jdbcDriver" value="com.mysql.jdbc.Driver" />
		<property name="jdbcUsername" value="root" />
		<property name="jdbcPassword" value="root" />
		<property name="databaseSchemaUpdate" value="true" />
		<property name="databaseType" value="mysql" />
	</bean>
</beans>
```
