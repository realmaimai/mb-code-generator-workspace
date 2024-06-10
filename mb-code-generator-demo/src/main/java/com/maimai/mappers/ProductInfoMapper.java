package com.maimai.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 20240610
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {
	/**
	 * based on Id to query data
	 */
	 T selectById(@Param("id") Integer id);

	/**
	 * based on Id to update data
	 */
	 T updateById();

	/**
	 * based on Id to delete data
	 */
	 T deleteById();

	/**
	 * based on Code to query data
	 */
	 T selectByCode(@Param("code") String code);

	/**
	 * based on Code to update data
	 */
	 T updateByCode();

	/**
	 * based on Code to delete data
	 */
	 T deleteByCode();

	/**
	 * based on SkuTypeAndColorType to query data
	 */
	 T selectBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * based on SkuTypeAndColorType to update data
	 */
	 T updateBySkuTypeAndColorType();

	/**
	 * based on SkuTypeAndColorType to delete data
	 */
	 T deleteBySkuTypeAndColorType();

}