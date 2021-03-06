package com.mmall.common;

import java.util.Set;

import com.google.common.collect.Sets;

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

	public interface Cart {
		int CHECKED = 1;// 即购物车选中状态
		int UN_CHECKED = 0;// 购物车中未选中状态

		String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
		String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
	}

	public interface ProductListOrderBy {
		Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
	}

	public enum ProductStatusEunm {
		ON_SALE(1, "在現");
		private String value;
		private int code;

		ProductStatusEunm(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public int getCode() {
			return code;
		}

	}

}
