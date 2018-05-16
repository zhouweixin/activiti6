package com.zhou;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.activiti.engine.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DatabaseConfigDemo {

	/**
	 * DBCP数据源-配置文件
	 */
	@Test
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

	/**
	 * DBCP数据源-代码
	 */
	@Test
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
}
