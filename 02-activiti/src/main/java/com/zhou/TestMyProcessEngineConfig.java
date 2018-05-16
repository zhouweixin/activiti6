package com.zhou;

import org.activiti.engine.*;
import org.junit.Test;

/**
 * 测试自定义的引擎配置类
 * 
 * @author zhouweixin
 *
 */
public class TestMyProcessEngineConfig {
	@Test
	public void test1() {
		MyProcessEngineConfiguration config = (MyProcessEngineConfiguration)ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("my-process-cfg.xml", "myProcessEngineConfiguration");
		ProcessEngine processEngine = config.buildProcessEngine();
		System.out.println("测试名称: " + config.getName());
		System.out.println("引擎名称: " + processEngine.getName());
	}	
}
