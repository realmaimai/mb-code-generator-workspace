package com.maimai.controller;
import java.util.List;
import com.maimai.entities.po.ProductInfo;
import com.maimai.entities.query.ProductInfoQuery;
import com.maimai.service.ProductInfoService;
import com.maimai.entities.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description: ProductInfoController
 * @Author: maimai
 * @Date: 20240616
 */
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController extends BaseController {

	@Resource
	private ProductInfoService productInfoService;
	/**
	 * get data list by pages
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query) {
		return getSuccessResponseVO(productInfoService.getListByPage(query));
	}

	/**
	 * insert new data
	 */
	@RequestMapping("insert")
	public ResponseVO insert(ProductInfo bean) {
		productInfoService.insert(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * insert or update
	 */
	@RequestMapping("insertOrUpdate")
	public ResponseVO insertOrUpdate(ProductInfo bean) {
		productInfoService.insertOrUpdate(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * batch insert
	 */
	@RequestMapping("insertBatch")
	public ResponseVO insertBatch(@RequestBody List<ProductInfo> listBean) {
		productInfoService.insertBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * batch insert or update
	 */
	@RequestMapping("insertOrUpdateBatch")
	 public ResponseVO insertOrUpdateBatch(@RequestBody List<ProductInfo> listBean) {
		productInfoService.insertOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * query based onId
	 */
	@RequestMapping("getProductInfoById")
	public  ResponseVO getProductInfoById(Integer id) {
		return getSuccessResponseVO(productInfoService.getProductInfoById(id));
	}

	/**
	 * update by Id
	 */
	@RequestMapping("updateProductInfoById")
	public ResponseVO updateProductInfoById(ProductInfo bean, Integer id) {
		productInfoService.updateProductInfoById(bean, id);
		return getSuccessResponseVO(null);
	}

	/**
	 * delete byId
	 */
	@RequestMapping("deleteProductInfoById")
	public ResponseVO deleteProductInfoById(Integer id) {
		productInfoService.deleteProductInfoById(id);
		return getSuccessResponseVO(null);
	}

	/**
	 * query based onCode
	 */
	@RequestMapping("getProductInfoByCode")
	public  ResponseVO getProductInfoByCode(String code) {
		return getSuccessResponseVO(productInfoService.getProductInfoByCode(code));
	}

	/**
	 * update by Code
	 */
	@RequestMapping("updateProductInfoByCode")
	public ResponseVO updateProductInfoByCode(ProductInfo bean, String code) {
		productInfoService.updateProductInfoByCode(bean, code);
		return getSuccessResponseVO(null);
	}

	/**
	 * delete byCode
	 */
	@RequestMapping("deleteProductInfoByCode")
	public ResponseVO deleteProductInfoByCode(String code) {
		productInfoService.deleteProductInfoByCode(code);
		return getSuccessResponseVO(null);
	}

	/**
	 * query based onSkuTypeAndColorType
	 */
	@RequestMapping("getProductInfoBySkuTypeAndColorType")
	public  ResponseVO getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return getSuccessResponseVO(productInfoService.getProductInfoBySkuTypeAndColorType(skuType, colorType));
	}

	/**
	 * update by SkuTypeAndColorType
	 */
	@RequestMapping("updateProductInfoBySkuTypeAndColorType")
	public ResponseVO updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		productInfoService.updateProductInfoBySkuTypeAndColorType(bean, skuType, colorType);
		return getSuccessResponseVO(null);
	}

	/**
	 * delete bySkuTypeAndColorType
	 */
	@RequestMapping("deleteProductInfoBySkuTypeAndColorType")
	public ResponseVO deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		productInfoService.deleteProductInfoBySkuTypeAndColorType(skuType, colorType);
		return getSuccessResponseVO(null);
	}

}