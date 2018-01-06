package com.mmall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 用戶登入
	 * 
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String userName, String passWord, HttpSession session) {
		ServerResponse<User> response = iUserService.login(userName, passWord);
		if (response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}

	/**
	 * 用戶登出
	 */

	@RequestMapping(value = "logout.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> logout(HttpSession session) {
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.creatBySuccess();
	}

	/**
	 * 註冊
	 */
	@RequestMapping(value = "register.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user) {
		return iUserService.register(user);
	}

	/**
	 * <p>校驗參數</p>
	 * 
	 * @user xxx 2018年1月5日 上午10:28:13
	 */

	@RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str, String type) {
		return iUserService.checkValid(str, type);
	}

	/**
	 * <p>獲取當前用戶，順便判斷是否已經登入</p>
	 * 
	 * @user Eric修義 2018年1月5日 上午10:33:01
	 */

	@RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user != null) {
			return ServerResponse.creatBySuccess(user);
		}
		return ServerResponse.creatByErrorMessage("用戶未登入，無法獲取當前用戶地址");
	}

	/**
	 * 
	 * <p>搜尋-找回密碼的問題</p>
	 * 
	 * @user Eric修義 2018年1月5日 上午11:45:52
	 */
	@RequestMapping(value = "forget_get_quertion.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuertion(String username) {
		return iUserService.selectQuestion(username);
	}

	/**
	 * 
	 * <p>確認找回密碼的答案</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午3:44:02
	 */
	@RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username, String quertion, String answer) {
		return iUserService.checkAnswer(username, quertion, answer);
	}

	/**
	 * 
	 * <p>重設定密碼</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午10:30:17
	 */
	@RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
		return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
	}

	/**
	 * 
	 * <p>登入狀態重設定密碼</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午10:58:20
	 */
	@RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.creatByErrorMessage("用戶未登入");
		}
		return iUserService.resetPassword(passwordOld, passwordNew, user);
	}

	/**
	 * 
	 * <p>更新個人訊息</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午11:12:42
	 */
	@RequestMapping(value = "update_information.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> update_information(HttpSession session, User user) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.creatByErrorMessage("用戶未登入");
		}
		user.setUsername(currentUser.getUsername());
		user.setId(currentUser.getId());
		ServerResponse<User> response = iUserService.updateInformation(user);
		if (response.isSuccess()) {
			response.getData().setUsername(currentUser.getUsername());
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}

	/**
	 * 
	 * <p>獲取用戶訊息</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午11:12:42
	 */
	@RequestMapping(value = "get_information.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> get_information(HttpSession session) {
		User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
		if (currentUser == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.NEED_LOGIN.getCode(), "未登錄，需要強制登陸status=10");
		}
		return iUserService.getInformation(currentUser.getId());
	}

}
