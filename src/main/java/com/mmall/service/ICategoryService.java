package com.mmall.service;

import java.util.List;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

public interface ICategoryService {

	ServerResponse<?> addCategory(String categoryName, Integer parentId);

	ServerResponse<?> updateCategoryName(Integer categoryId, String categoryName);

	ServerResponse<List<Category>> getChildrenParallelCategroy(Integer categoryId);

	ServerResponse<List<Integer>> selectCategoryAadChildrenBtId(Integer categoryId);

}
