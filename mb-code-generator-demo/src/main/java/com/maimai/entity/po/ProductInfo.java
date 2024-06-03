package com.maimai.entity.po;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 2024/06/03
 */
public class ProductInfo implements Serializable {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Company ID
	 */
	@JsonIgnore
	private String companyId;

	/**
	 * Code
	 */
	private String code;

	/**
	 * Product Name
	 */
	private String productName;

	/**
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+2")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * Create Date
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+2")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;

	/**
	 * Stock
	 */
	private Long stock;

	/**
	 * Status
	 */
	@JsonIgnore
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