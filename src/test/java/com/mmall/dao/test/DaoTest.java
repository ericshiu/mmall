package com.mmall.dao.test;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.test.TestBase;

/**
 * Created by geely on mmall.
 */
public class DaoTest extends TestBase {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void testDao() {
		User a = new User();
		a.setPassword("111");
		a.setUsername("aaaaageely");
		a.setAnswer("答案");
		a.setQuestion("問題");
		a.setRole(0);
		a.setCreateTime(new Date());
		a.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		int c = userMapper.insert(a);
		System.out.println(c + "  aaaaaaaaaaaaaa");
	}

}
