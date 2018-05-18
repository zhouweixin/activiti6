package com.zhou;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.activiti.engine.*;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.apache.ibatis.io.Resources;
import org.junit.Test;

/**
 * 用户
 * 
 * @author zhouweixin
 *
 */
public class UserDemo {

	/**
	 * 添加
	 */
	@Test
	public void test1() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		User user = identityService.newUser("1");
		user.setFirstName("张");
		user.setLastName("三");
		user.setEmail("*********@163.com");
		user.setPassword("123456");
		identityService.saveUser(user);

		User u = identityService.createUserQuery().userId("1").singleResult();
		System.out.println(String.format("test1-{id = %s, firstName = %s, lastName = %s, email = %s, password = %s}",
				u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPassword()));
	}

	/**
	 * 验证用户密码
	 */
	@Test
	public void test2() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		User user = identityService.createUserQuery().userId("1").singleResult();
		System.out
				.println(String.format("test2-user {id = %s, firstName = %s, lastName = %s, email = %s, password = %s}",
						user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()));

		boolean b1 = identityService.checkPassword("1", "123456");
		boolean b2 = identityService.checkPassword("1", "asdfas");

		System.out.println("123456--验证结果: " + b1);
		System.out.println("asdfas--验证结果: " + b2);
	}

	/**
	 * 设置用户自定义信息和图片
	 */
	public void test3() {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		IdentityService identityService = processEngine.getIdentityService();

		User user = identityService.newUser("3");
		user.setFirstName("李");
		user.setLastName("四");
		user.setEmail("abc@123.com");
		identityService.saveUser(user);

		// 设置用户信息
		identityService.setUserInfo("2", "age", "18");

		try {
			// 设置用户图片
			FileInputStream fis = new FileInputStream(Resources.getResourceAsFile("ask-for-leave.png"));
			BufferedImage img = ImageIO.read(fis);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(img, "png", output);
			byte[] pics = output.toByteArray();
			Picture picture = new Picture(pics, "ask-for-image");
			identityService.setUserPicture("3", picture);
		} catch (Exception e) {

		}
		
		System.out.println("test3-测试结束!");
	}
	
	public static void main(String[] args) {
		new UserDemo().test3();
	}
}
