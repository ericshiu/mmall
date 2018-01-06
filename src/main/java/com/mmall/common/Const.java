package com.mmall.common;

public class Const {

	public static final String CURRENT_USER = "currentUser";
	public static final String EMAIL = "email";
	public static final String USERNAME = "username";

	public interface Role {
		// 普通商戶
		int ROLE_CUSTMER = 0;
		// 管理員
		int ROLE_ADMIN = 1;
	}

}
