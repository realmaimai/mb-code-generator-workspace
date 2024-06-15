package com.maimai.service;

import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.entities.vo.PaginationResultVO;

import java.util.List;/**
 * @Description: Product Information Table Service
 *
 * @Author: maimai
 * @Date: 20240615
 */
public interface ProductInfoService {

	List<ProductInfo> getListByParam(ProductInfoQuery query);

	Long getCountByParam(ProductInfoQuery query);

	PaginationResultVO<ProductInfo> getListByPage(ProductInfoQuery query);
}