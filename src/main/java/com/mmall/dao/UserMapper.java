package com.mmall.dao;

import org.apache.ibatis.annotations.Param;

import com.mmall.pojo.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	int checkUserName(String userName);

	int checkEmail(String userName);

	User selectLogin(@Param("username") String username, @Param("password") String passWord);

	String selectQuestionByUsername(String username);

	int checkAnswer(@Param("username") String username, @Param("quertion") String quertion,
			@Param("answer") String answer);

	int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);

	int checkPassword(@Param("password") String passwordNew, @Param("userId") Integer userId);

	int checkEmailByUserId(@Param("email") String email, @Param("userId") Integer userId);
}