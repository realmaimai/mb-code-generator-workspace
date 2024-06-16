package com.maimai.service.impl;

import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.mappers.ProductInfoMapper;
import com.maimai.entities.query.Paginator;
import com.maimai.entities.vo.PaginationResultVO;
import com.maimai.service.ProductInfoService;
import com.maimai.enums.PageSize;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
/**
 * @Description: Product Information Table Service
 * @Author: maimai
 * @Date: 20240616
 */
@Service("productInfoService")
public class ProductInfoServiceImpl implements ProductInfoService {

	@Resource
	private ProductInfoMapper<ProductInfo,ProductInfoQuery> productInfoMapper;

	@Override
	public List<ProductInfo> getListByParam(ProductInfoQuery query) {
		return this.productInfoMapper.selectList(query);
	}

	@Override
	public Integer getCountByParam(ProductInfoQuery query) {
		return this.productInfoMapper.selectCount(query);
	}

	@Override
	public PaginationResultVO<ProductInfo> getListByPage(ProductInfoQuery query) {
		Integer count = this.getCountByParam(query);
		Integer pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
		Paginator page = new Paginator(query.getPageNo(), count, pageSize);
		query.setPaginator(page);
		List<ProductInfo> list = this.getListByParam(query);
		PaginationResultVO<ProductInfo> result = new PaginationResultVO<>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	@Override
	public Integer insert(ProductInfo bean) {
		return this.productInfoMapper.insert(bean);
	}

	@Override
	public Integer insertOrUpdate(ProductInfo bean) {
		return this.productInfoMapper.insertOrUpdate(bean);
	}

	@Override
	public Integer insertBatch(List<ProductInfo> listBean) {
		if(listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.productInfoMapper.insertBatch(listBean);
	}

	@Override
	public Integer insertOrUpdateBatch(List<ProductInfo> listBean) {
		if(listBean == null || listBean.isEmpty()){
			return 0;
		}
		return this.productInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * based on Id to query data
	 */
	public ProductInfo getProductInfoById(Integer id) {
		return productInfoMapper.selectById(id);
	}

	/**
	 * based on Id to update data
	 */
	public Integer updateProductInfoById(ProductInfo bean, Integer id) {
		return productInfoMapper.updateById(bean, id);
	}

	/**
	 * based on Id to delete data
	 */
	public Integer deleteProductInfoById(Integer id) {
		return productInfoMapper.deleteById(id);
	}

	/**
	 * based on Code to query data
	 */
	public ProductInfo getProductInfoByCode(String code) {
		return productInfoMapper.selectByCode(code);
	}

	/**
	 * based on Code to update data
	 */
	public Integer updateProductInfoByCode(ProductInfo bean, String code) {
		return productInfoMapper.updateByCode(bean, code);
	}

	/**
	 * based on Code to delete data
	 */
	public Integer deleteProductInfoByCode(String code) {
		return productInfoMapper.deleteByCode(code);
	}

	/**
	 * based on SkuTypeAndColorType to query data
	 */
	public ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return productInfoMapper.selectBySkuTypeAndColorType(skuType, colorType);
	}

	/**
	 * based on SkuTypeAndColorType to update data
	 */
	public Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		return productInfoMapper.updateBySkuTypeAndColorType(bean, skuType, colorType);
	}

	/**
	 * based on SkuTypeAndColorType to delete data
	 */
	public Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return productInfoMapper.deleteBySkuTypeAndColorType(skuType, colorType);
	}


}