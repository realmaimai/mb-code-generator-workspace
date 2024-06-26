package com.maimai.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: Product Information Table
 * @Author: maimai
 * @Date: 20240616
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {
	/**
	 * based on Id to query data
	 */
	T selectById(@Param("id") Integer id);

	/**
	 * based on Id to update data
	 */
	Integer updateById(@Param("bean") T t, @Param("id") Integer id);

	/**
	 * based on Id to delete data
	 */
	Integer deleteById(@Param("id") Integer id);

	/**
	 * based on Code to query data
	 */
	T selectByCode(@Param("code") String code);

	/**
	 * based on Code to update data
	 */
	Integer updateByCode(@Param("bean") T t, @Param("code") String code);

	/**
	 * based on Code to delete data
	 */
	Integer deleteByCode(@Param("code") String code);

	/**
	 * based on SkuTypeAndColorType to query data
	 */
	T selectBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * based on SkuTypeAndColorType to update data
	 */
	Integer updateBySkuTypeAndColorType(@Param("bean") T t, @Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * based on SkuTypeAndColorType to delete data
	 */
	Integer deleteBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

}