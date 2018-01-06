package com.mmall.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
	@Autowired
	private IUserService iUserService;
	@Autowired
	private ICategoryService iCategoryService;

	/**
	 * 
	 * <p>管理員添加產品分類</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午8:52:14
	 */
	@RequestMapping("add_category.do")
	@ResponseBody
	public ServerResponse addCategory(HttpSession session, String categoryName,
			@RequestParam(value = "parentId", defaultValue = "0") int parentId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入");
		}
		// 確認是否是管理員
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 是管理員
			return iCategoryService.addCategory(categoryName, parentId);
		} else {
			return ServerResponse.creatByErrorMessage("無權限操作，需要管理員權限");
		}
	}

	/**
	 * 
	 * <p>更新分類名稱</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午9:00:12
	 */
	@RequestMapping("set_category_name.do")
	@ResponseBody
	public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入");
		}
		// 確認是否是管理員
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 是管理員
			return iCategoryService.updateCategoryName(categoryId, categoryName);
		} else {
			return ServerResponse.creatByErrorMessage("無權限操作，需要管理員權限");
		}
	}

	/**
	 * 
	 * <p>查詢子分類</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午9:15:53
	 */
	@RequestMapping("get_category.do")
	@ResponseBody
	public ServerResponse<?> getChildrenParallelCategory(HttpSession session,
			@RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入");
		}
		// 確認是否是管理員
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iCategoryService.getChildrenParallelCategroy(categoryId);
		} else {
			return ServerResponse.creatByErrorMessage("無權限操作，需要管理員權限");
		}
	}

	/**
	 * 
	 * <p>查詢子分類</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午9:15:53
	 */
	@RequestMapping("get_deep_category.do")
	@ResponseBody
	public ServerResponse<?> getCategoryAndDeepChildrenParallelCategory(HttpSession session,
			@RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入");
		}
		// 確認是否是管理員
		if (iUserService.checkAdminRole(user).isSuccess()) {
			// 查詢當前節點的id和關聯子節點的id
			// 0-->1000-->10000
			return iCategoryService.selectCategoryAadChildrenBtId(categoryId);
		} else {
			return ServerResponse.creatByErrorMessage("無權限操作，需要管理員權限");
		}
	}

}
