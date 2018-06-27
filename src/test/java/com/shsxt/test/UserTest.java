package com.shsxt.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.shsxt.dao.UserDao;
import com.shsxt.po.User;
import com.shsxt.util.MD5Util;

/**
 * 单元测试类
 * @author Administrator
 *
 */
public class UserTest {

	// 不能有父类 ； @Test 不能为 静态方法 不能存在参数 返回值为void
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testUserDao () {
		System.out.println("单元测试类....");
		UserDao userDao = new UserDao();
		User user = userDao.findUserByUnameAndUpwd("shsxt", MD5Util.encode(MD5Util.encode("123456")));
		System.out.println(user.getUname());
	}

}
