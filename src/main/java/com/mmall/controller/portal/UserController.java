package com.mmall.controller.portal;

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
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private IUserService iUserService;

	/**
	 * 用戶登入
	 * 
	 * @param userName
	 * @param passWord
	 * @param session
	 * @return
	 * 
	 */
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String userName, String passWord, HttpSession session) {
		ServerResponse<User> response = iUserService.login(userName, passWord);
		if (response.isSuccess()) {
			session.setAttribute(Const.CONST_USER, response.getData());
		}
		return response;
	}

	/**
	 * 用戶登出
	 */

	@RequestMapping(value = "logout.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> logout(HttpSession session) {
		session.removeAttribute(Const.CONST_USER);
		return ServerResponse.creatBySuccess();
	}

	/**
	 * 註冊
	 */
	@RequestMapping(value = "register.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> register(User user) {
		return iUserService.register(user);
	}

	/**
	 * <p>校驗參數</p>
	 * 
	 * @user xxx 2018年1月5日 上午10:28:13
	 */

	@RequestMapping(value = "check_valid.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> checkValid(String str, String type) {
		return iUserService.checkValid(str, type);
	}

	/**
	 * <p>獲取當前用戶，順便判斷是否已經登入</p>
	 * 
	 * @user Eric修義 2018年1月5日 上午10:33:01
	 */

	@RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session) {
		User user = (User) session.getAttribute(Const.CONST_USER);
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
	@RequestMapping(value = "forget_get_quertion.do", method = RequestMethod.GET)
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
	@RequestMapping(value = "forget_check_answer.do", method = RequestMethod.GET)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username, String quertion, String answer) {
		return iUserService.checkAnswer(username, quertion, answer);
	}

}
