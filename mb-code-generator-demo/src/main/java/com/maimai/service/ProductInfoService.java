package com.maimai.service;

import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.entities.vo.PaginationResultVO;

import java.util.List;
/**
 * @Description: Product Information Table Service
 *
 * @Author: maimai
 * @Date: 20240616
 */
public interface ProductInfoService {

	List<ProductInfo> getListByParam(ProductInfoQuery query);

	Integer getCountByParam(ProductInfoQuery query);

	PaginationResultVO<ProductInfo> getListByPage(ProductInfoQuery query);

	Integer insert(ProductInfo bean);

	Integer insertOrUpdate(ProductInfo bean);

	Integer insertBatch(List<ProductInfo> beanList);

	Integer insertOrUpdateBatch(List<ProductInfo> beanList);

	/**
	 * based on Id to query data
	 */
	ProductInfo getProductInfoById(Integer id);

	/**
	 * based on Id to update data
	 */
	Integer updateProductInfoById(ProductInfo bean, Integer id);

	/**
	 * based on Id to delete data
	 */
	Integer deleteProductInfoById(Integer id);

	/**
	 * based on Code to query data
	 */
	ProductInfo getProductInfoByCode(String code);

	/**
	 * based on Code to update data
	 */
	Integer updateProductInfoByCode(ProductInfo bean, String code);

	/**
	 * based on Code to delete data
	 */
	Integer deleteProductInfoByCode(String code);

	/**
	 * based on SkuTypeAndColorType to query data
	 */
	ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * based on SkuTypeAndColorType to update data
	 */
	Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);

	/**
	 * based on SkuTypeAndColorType to delete data
	 */
	Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);


}