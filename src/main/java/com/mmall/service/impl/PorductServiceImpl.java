package com.mmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IPorductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

@Service("iPorductService")
public class PorductServiceImpl implements IPorductService {
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private ICategoryService iCategoryService;

	/**
	 * 
	 * <p>更新產品info</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午7:58:08
	 */
	@Override
	public ServerResponse<?> saveOrUpdateProduct(Product product) {
		if (product != null) {
			if (StringUtils.isNoneBlank(product.getSubImages())) {
				String[] subImageArray = product.getSubImages().split(",");
				if (subImageArray.length > 0) {
					product.setMainImage(subImageArray[0]);
				}
			}
			if (product.getId() != null) {
				int rowCount = productMapper.updateByPrimaryKey(product);
				if (rowCount > 0) {
					return ServerResponse.createByErrorMessage("更新產品失敗");
				}
				return ServerResponse.createBySuccessMessage("更新產品成功");
			} else {
				int rowCount = productMapper.insert(product);
				if (rowCount > 0) {
					return ServerResponse.createByErrorMessage("新增產品失敗");
				}
				return ServerResponse.createBySuccessMessage("新增產品成功");
			}
		}
		return ServerResponse.createByErrorMessage("參數不正確");
	}

	/**
	 * 
	 * <p>更新產品狀態</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午7:58:08
	 */
	@Override
	public ServerResponse<?> setSaleStatus(Integer productId, Integer status) {
		if (productId == null || status == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKey(product);
		if (rowCount > 0) {
			return ServerResponse.createBySuccessMessage("修改成品銷售狀態成功");
		}

		return ServerResponse.createByErrorMessage("修改成品銷售狀態失敗");
	}

	@Override
	public ServerResponse<?> manageProductDetail(Integer productId) {
		if (productId == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createByErrorMessage("產品已下架或著刪除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);

		return ServerResponse.createBySuccess(productDetailVo);
	}

	/**
	 * 
	 * <p>組裝ProductDetailVo</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午10:12:36
	 */
	private ProductDetailVo assembleProductDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setSubtitle(product.getSubImages());
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setSubImage(product.getSubImages());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo.setDetail(product.getDetail());
		productDetailVo.setName(product.getName());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setStock(product.getStock());
		productDetailVo
				.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
		if (category == null) {
			productDetailVo.setParendCategoryId(0);
		} else {
			productDetailVo.setParendCategoryId(category.getParentId());
		}

		productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
		productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
		return productDetailVo;
	}

	/**
	 * 
	 * <p>查詢商品list</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午10:21:23
	 */

	@Override
	public ServerResponse<?> getProductList(int pageNum, int pageSize) {
		// 1.PageHelper-startPage
		// 2.填充自己的sql查詢邏輯
		// 3.PageHelper-收尾
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productList = productMapper.selectList();
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVO = assembleProductListVO(productItem);
			productListVoList.add(productListVO);
		}
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(productListVoList);

	}

	/**
	 * 
	 * <p>組裝ProductListVO</p>
	 * 
	 * @user Eric修義 2018年1月7日 上午10:12:36
	 */
	private ProductListVo assembleProductListVO(Product product) {
		ProductListVo productDetailVo = new ProductListVo();
		productDetailVo.setId(product.getId());
		productDetailVo.setName(product.getName());
		productDetailVo.setCategoryId(product.getCategoryId());
		productDetailVo
				.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
		productDetailVo.setMainImage(product.getMainImage());
		productDetailVo.setPrice(product.getPrice());
		productDetailVo.setStatus(product.getStatus());
		productDetailVo.setSubtitle(product.getSubtitle());
		return productDetailVo;
	}

	@Override
	public ServerResponse<?> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		if (StringUtils.isNotBlank(productName)) {
			productName = new StringBuilder().append("%").append(productName).append("%").toString();

		}
		List<Product> productList = productMapper.selectByNameAndProduct(productName, productId);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product productItem : productList) {
			ProductListVo productListVO = assembleProductListVO(productItem);
			productListVoList.add(productListVO);
		}
		PageInfo pageResult = new PageInfo(productList);
		pageResult.setList(productListVoList);
		return ServerResponse.createBySuccess(productListVoList);

	}

	@Override
	public ServerResponse<?> getProductDetail(Integer productId) {
		if (productId == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.createByErrorMessage("產品已下架或著刪除");
		}
		if (product.getStatus() != Const.ProductStatusEunm.ON_SALE.getCode()) {
			return ServerResponse.createByErrorMessage("產品已下架或著刪除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);
		return ServerResponse.createBySuccess(productDetailVo);
	}

	public ServerResponse<PageInfo> getProductNyKetwordCategory(String keyword, Integer categoryId, int pageNum,
			int pageSize, String orderBy) {
		if (StringUtils.isBlank(keyword) && categoryId == null) {
			return ServerResponse.createByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		List<Integer> categoryIdList = new ArrayList<Integer>();
		if (categoryId != null) {
			Category category = categoryMapper.selectByPrimaryKey(categoryId);
			if (category == null && StringUtils.isBlank(keyword)) {
				PageHelper.startPage(pageNum, pageSize);
				List<ProductListVo> productListVoList = Lists.newArrayList();
				PageInfo pageInfo = new PageInfo(productListVoList);
				return ServerResponse.createBySuccess(pageInfo);
			}
			categoryIdList = iCategoryService.selectCategoryAadChildrenBtId(categoryId).getData();
		}
		if (StringUtils.isNotBlank(keyword)) {
			keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
		}
		PageHelper.startPage(pageNum, pageSize);
		// 排序處理
		if (StringUtils.isNotBlank(orderBy)) {
			if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
				String[] orderByArray = orderBy.split("_");
				PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
			}
		}
		List<Product> productList = productMapper.selectByNameAndCategoryIds(
				StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);
		List<ProductListVo> productListVoList = Lists.newArrayList();
		for (Product product : productList) {
			ProductListVo productListVo = assembleProductListVO(product);
			productListVoList.add(productListVo);
		}
		PageInfo pageInfo = new PageInfo(productList);
		pageInfo.setList(productListVoList);
		return ServerResponse.createBySuccess(pageInfo);

	}
}
