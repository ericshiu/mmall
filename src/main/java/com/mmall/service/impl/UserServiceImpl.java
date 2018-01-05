package com.mmall.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String userName, String passWord) {
		int resultCount = userMapper.checkUserName(userName);
		if (resultCount == 0) {
			return ServerResponse.creatByErrorMessage("用戶名不存在");
		}

		// todo 登入密碼md5
		String md5PassWord = MD5Util.MD5EncodeUtf8(passWord);
		User user = userMapper.selectLogin(userName, md5PassWord);
		if (user == null) {
			return ServerResponse.creatByErrorMessage("密碼錯誤");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.creatBySuccess("登入成功", user);
	}

	/**
	 * 註冊功能
	 */

	@Override
	public ServerResponse<String> register(User user) {
		// int resultCount = userMapper.checkUserName(user.getUsername());
		// if (resultCount > 0) {
		// return ServerResponse.creatByErrorMessage("用戶名存在");
		// }
		// resultCount = userMapper.checkEmail(user.getEmail());
		// if (resultCount > 0) {
		// return ServerResponse.creatByErrorMessage("Email已存在");
		// }
		ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}

		validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
		if (!validResponse.isSuccess()) {
			return validResponse;
		}
		user.setRole(Const.Role.ROLE_CUSTMER);
		// MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int resultCount = userMapper.insert(user);
		if (resultCount > 0) {
			return ServerResponse.creatByErrorMessage("註冊失敗");
		}
		return ServerResponse.creatBySuccessMessage("註冊成功");
	}

	/**
	 * 
	 * <p>確認是否已註冊過</p>
	 * 
	 * @user xxx 2018年1月5日 上午10:17:45
	 */

	@Override
	public ServerResponse<String> checkValid(String str, String type) {

		if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
			// 才開始繳驗
			if (Const.USERNAME.equals(str)) {
				int resultCount = userMapper.checkUserName(str);
				if (resultCount == 0) {
					return ServerResponse.creatByErrorMessage("用戶名不存在");
				}
			}
			if (Const.EMAIL.equals(type)) {
				int resultCount = userMapper.checkEmail(type);
				if (resultCount > 0) {
					return ServerResponse.creatByErrorMessage("Email已存在");
				}

			}
		} else {
			return ServerResponse.creatByErrorMessage("參數錯誤");
		}
		return ServerResponse.creatBySuccess("校驗成功");
	}

	/**
	 * 
	 * <p>搜尋-找回密碼的問題</p>
	 * 
	 * @user Eric修義 2018年1月5日 上午11:45:52
	 */
	@Override
	public ServerResponse<String> selectQuestion(String username) {
		ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
		if (validResponse.isSuccess()) {
			return ServerResponse.creatByErrorMessage("用戶不存在");
		}
		String question = userMapper.selectQuestionByUsername(username);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
			return ServerResponse.creatBySuccess(question);
		}
		return ServerResponse.creatByErrorMessage("找回密碼的問題是空的");

	}

	/**
	 * 
	 * <p>驗證找回密碼答案</p>
	 * 
	 * @user Eric修義 2018年1月5日 下午4:07:33
	 */
	@Override
	public ServerResponse<String> checkAnswer(String username, String quertion, String answer) {
		int resultCount = userMapper.chuckAnswer(username, quertion, answer);
		if (resultCount > 0) {
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey("token_" + username, forgetToken);
			return ServerResponse.creatBySuccess(forgetToken);
		}
		return ServerResponse.creatByErrorMessage("問題的答案錯誤");

	}

}
