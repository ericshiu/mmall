package com.mmall.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IPorductService;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IPorductService iPorductService;

	/**
	 * 
	 * <p>獲取商品詳情</p>
	 * 
	 * @user Eric修義 2018年1月7日 下午6:52:56
	 */
	@RequestMapping(value = "detail.do", method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<?> detail(Integer productId) {
		return iPorductService.getProductDetail(productId);

	}

	public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "categoryId", required = false) Integer categoryId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "orderBy") String orderBy) {
		return iPorductService.getProductNyKetwordCategory(keyword, categoryId, pageNum, pageSize, orderBy);
	}

}
