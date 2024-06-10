package com.maimai.entity.query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 20240610
 */
public class ProductInfoQuery{
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Company ID
	 */
	private String companyId;

	private String companyIdFuzzy;	/**
	 * Code
	 */
	private String code;

	private String codeFuzzy;	/**
	 * Product Name
	 */
	private String productName;

	private String productNameFuzzy;	/**
	 * Price
	 */
	private BigDecimal price;

	/**
	 * SKU Type
	 */
	private Integer skuType;

	/**
	 * Color Type
	 */
	private Integer colorType;

	/**
	 * Create Time
	 */
	private Date createTime;

	/**
	 * Create Date
	 */
	private Date createDate;

	/**
	 * Stock
	 */
	private Long stock;

	/**
	 * Status
	 */
	private Integer status;

	public Integer getId(){
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyId(){
		return this.companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCode(){
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProductName(){
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice(){
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getSkuType(){
		return this.skuType;
	}

	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}

	public Integer getColorType(){
		return this.colorType;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateDate(){
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getStock(){
		return this.stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}