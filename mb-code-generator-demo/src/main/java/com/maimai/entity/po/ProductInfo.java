package com.maimai.entity.po;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 2024/06/01
 */
public class ProductInfo implements Serializable {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Company ID
	 */
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

}