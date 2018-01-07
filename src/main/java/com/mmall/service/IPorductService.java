package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IPorductService {
	ServerResponse<?> saveOrUpdateProduct(Product product);

	ServerResponse<?> setSaleStatus(Integer productId, Integer status);

	ServerResponse<?> manageProductDetail(Integer productId);

	ServerResponse<?> getProductList(int pageNum, int pageSize);

	ServerResponse<?> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

}
