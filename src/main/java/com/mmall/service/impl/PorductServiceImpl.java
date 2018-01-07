package com.mmall.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
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
					return ServerResponse.creatByErrorMessage("更新產品失敗");
				}
				return ServerResponse.creatBySuccessMessage("更新產品成功");
			} else {
				int rowCount = productMapper.insert(product);
				if (rowCount > 0) {
					return ServerResponse.creatByErrorMessage("新增產品失敗");
				}
				return ServerResponse.creatBySuccessMessage("新增產品成功");
			}
		}
		return ServerResponse.creatByErrorMessage("參數不正確");
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
			return ServerResponse.creatByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = new Product();
		product.setId(productId);
		product.setStatus(status);
		int rowCount = productMapper.updateByPrimaryKey(product);
		if (rowCount > 0) {
			return ServerResponse.creatBySuccessMessage("修改成品銷售狀態成功");
		}

		return ServerResponse.creatByErrorMessage("修改成品銷售狀態失敗");
	}

	@Override
	public ServerResponse<?> manageProductDetail(Integer productId) {
		if (productId == null) {
			return ServerResponse.creatByErrorMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
					ResponseCode.ILLEGAL_ARGUMENT.getDesc());
		}
		Product product = productMapper.selectByPrimaryKey(productId);
		if (product == null) {
			return ServerResponse.creatByErrorMessage("產品已下架或著刪除");
		}
		ProductDetailVo productDetailVo = assembleProductDetailVo(product);

		return ServerResponse.creatBySuccess(productDetailVo);
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
		return ServerResponse.creatBySuccess(productListVoList);

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
		return ServerResponse.creatBySuccess(productListVoList);

	}
}
