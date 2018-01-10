package com.mmall.controller.backend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IPorductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

	@Autowired
	IUserService iUserService;

	@Autowired
	private IPorductService iPorductService;

	@Autowired
	private IFileService iFileService;

	/**
	 * 
	 * <p>更新產品info</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午7:58:08
	 */
	@RequestMapping(value = "save.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> productSave(HttpSession session, Product product) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iPorductService.saveOrUpdateProduct(product);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	/**
	 * 
	 * <p>更新產品狀態</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午7:58:08
	 */
	@RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> setSaleStatus(HttpSession session, Integer productId, Integer status) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iPorductService.setSaleStatus(productId, status);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	/**
	 * 
	 * <p>獲取產品詳情</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午7:58:08
	 */
	@RequestMapping(value = "detail.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> getDetail(HttpSession session, Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iPorductService.manageProductDetail(productId);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	/**
	 * 
	 * <p>查詢商品list</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午10:21:23
	 */

	@RequestMapping(value = "list.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> getList(HttpSession session,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iPorductService.getProductList(pageNum, pageSize);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	@RequestMapping(value = "search.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> productSearch(HttpSession session, String productName,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Integer productId) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			return iPorductService.searchProduct(productName, productId, pageNum, pageSize);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	@RequestMapping(value = "upload.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> upload(HttpSession session,
			@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		if (user == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode() + "用戶未登入,請登入管理員");
		}
		if (iUserService.checkAdminRole(user).isSuccess()) {
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
			Map fileMap = Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ServerResponse.createBySuccess(fileMap);
		} else {
			return ServerResponse.createByErrorMessage("無操作權限");
		}
	}

	@RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
	@ResponseBody
	public Map richtextImgUpload(HttpSession session,
			@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		User user = (User) session.getAttribute(Const.CURRENT_USER);
		Map resiltMap = Maps.newHashMap();
		if (user == null) {
			resiltMap.put("success", false);
			resiltMap.put("msg", "請登入管理員");
			return resiltMap;
		}
		// 傅文本中對於返回職有自己的要求，我們使用simditor要求
		if (iUserService.checkAdminRole(user).isSuccess()) {
			String path = request.getSession().getServletContext().getRealPath("upload");
			String targetFileName = iFileService.upload(file, path);
			if (StringUtils.isBlank(targetFileName)) {
				resiltMap.put("success", false);
				resiltMap.put("msg", "上傳失敗");
				return resiltMap;
			}
			String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
			resiltMap.put("success", true);
			resiltMap.put("msg", "上傳成功");
			resiltMap.put("file_path", url);
			response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
			return resiltMap;
		} else {
			resiltMap.put("success", false);
			resiltMap.put("msg", "無操作權限");
			return resiltMap;
		}
	}

}
