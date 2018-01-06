/*******************************************************************************
 * Project Key : CPPII
 * Create on 2018年1月5日 下午4:48:35
 * Copyright (c) 2017.炬火數位有限公司版權所有. 
 * 注意：本內容僅限於炬火數位內部傳閱，禁止外洩以及用於其他商業目的
 ******************************************************************************/
package com.mmall.dao.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.mmall.service.IUserService;
import com.mmall.test.TestBase;

/**
 * <P>TODO</P>
 * 
 * @version $Id$
 * @user Eric修義 2018年1月5日 下午4:48:35
 */
public class DbTest extends TestBase {
	@Autowired
	private IUserService iUserService;

	@Test
	public void dbTest() {
		System.out.println(iUserService.selectQuestion("geely"));
	}

}
