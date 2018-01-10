package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 
	 * <p>管理員登入</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午11:31:41
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username, String password, HttpSession session) {
		ServerResponse<User> response = iUserService.login(username, password);
		if (response.isSuccess()) {
			User user = response.getData();
			if (user.getRole() == Const.Role.ROLE_ADMIN) {
				session.setAttribute(Const.CURRENT_USER, user);
			} else {
				return ServerResponse.createByErrorMessage("不是管理員,無法登入");
			}
		}
		return response;
	}
}
