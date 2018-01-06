package com.mmall.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryMapper categoryMapper;

	/**
	 * 
	 * <p>新增產品分類</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午8:48:52
	 */
	@Override
	public ServerResponse addCategory(String categoryName, Integer parentId) {
		if (parentId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.creatByErrorMessage("添加品類參數錯誤");
		}
		Category category = new Category();
		category.setName(categoryName);
		category.setParentId(parentId);
		category.setStatus(true);

		int rowCount = categoryMapper.insert(category);
		if (rowCount > 0) {
			return ServerResponse.creatBySuccessMessage("添加品類成功");
		}
		return ServerResponse.creatByErrorMessage("添加品類失敗");

	}

	/**
	 * 更新產品分類稱
	 */
	@Override
	public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
		if (categoryId == null || StringUtils.isBlank(categoryName)) {
			return ServerResponse.creatByErrorMessage("更新品類參數錯誤");
		}
		Category category = new Category();
		category.setId(categoryId);
		category.setName(categoryName);

		int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
		if (rowCount > 0) {
			return ServerResponse.creatBySuccessMessage("更新品類名稱成功");
		}
		return ServerResponse.creatByErrorMessage("更新品類名稱失敗");
	}

	/**
	 * 
	 * <p>查詢子分類</p>
	 * 
	 * @user Eric修義 2018年1月6日 上午9:15:53
	 */
	@Override
	public ServerResponse<List<Category>> getChildrenParallelCategroy(Integer categoryId) {
		List<Category> categortList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		if (CollectionUtils.isEmpty(categortList)) {
			logger.info("未找到當前分類的子分類");
		}
		return ServerResponse.creatBySuccess(categortList);
	}

	/**
	 * <p>遞規查詢本結點的id及孩子結點的id</p>
	 * 
	 * @user Eric修義 2018年1月6日 下午4:21:58
	 */

	public ServerResponse<?> selectCategoryAadChildrenBtId(Integer categoryId) {
		Set<Category> categortSet = Sets.newHashSet();
		findChildCategory(categortSet, categoryId);
		List<Integer> categoryIdList = Lists.newArrayList();
		if (categoryId != null) {
			for (Category categoryItem : categortSet) {
				categoryIdList.add(categoryItem.getId());
			}
		}
		return ServerResponse.creatBySuccess(categoryIdList);
	}

	/**
	 * 
	 * <p>遞歸算法，算出子結點</p>
	 * 
	 * @user Eric修義 2018年1月6日 下午3:48:47
	 */
	public Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
		Category category = categoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			categorySet.add(category);
		}
		List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
		for (Category categoryItem : categoryList) {
			findChildCategory(categorySet, categoryItem.getId());

		}
		return categorySet;
	}

}
