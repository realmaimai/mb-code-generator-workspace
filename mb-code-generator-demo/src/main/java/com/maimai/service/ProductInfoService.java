package com.maimai.service;

import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.entities.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description: Product Information Table Service
 *
 * @Author: maimai
 * @Date: 20240615
 */
public interface ProductInfoService {

	List<ProductInfo> getListByParam(ProductInfoQuery query);

	Long getCountByParam(ProductInfoQuery query);

	PaginationResultVO<ProductInfo> getListByPage(ProductInfoQuery query);

	Long insert(ProductInfo bean);

	Long insertBatch(List<ProductInfo> beanList);

	Long insertOrUpdateBatch(List<ProductInfo> beanList);

	/**
	 * based on Id to query data
	 */
	ProductInfo getById(Integer id);

	/**
	 * based on Id to update data
	 */
	Long updateById(ProductInfo bean, Integer id);

	/**
	 * based on Id to delete data
	 */
	Long deleteById(Integer id);

	/**
	 * based on Code to query data
	 */
	ProductInfo getByCode(String code);

	/**
	 * based on Code to update data
	 */
	Long updateByCode(ProductInfo bean, String code);

	/**
	 * based on Code to delete data
	 */
	Long deleteByCode(String code);

	/**
	 * based on SkuTypeAndColorType to query data
	 */
	ProductInfo getBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * based on SkuTypeAndColorType to update data
	 */
	Long updateBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);

	/**
	 * based on SkuTypeAndColorType to delete data
	 */
	Long deleteBySkuTypeAndColorType(Integer skuType, Integer colorType);


}