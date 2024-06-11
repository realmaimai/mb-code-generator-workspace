package com.maimai.entities.query;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 20240611
 */
@Data
public class ProductInfoQuery{
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * Company ID
	 */
	private String companyId;

	private String companyIdFuzzy;

	/**
	 * Code
	 */
	private String code;

	private String codeFuzzy;

	/**
	 * Product Name
	 */
	private String productName;

	private String productNameFuzzy;

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

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * Create Date
	 */
	private Date createDate;

	private String createDateStart;

	private String createDateEnd;

	/**
	 * Stock
	 */
	private Long stock;

	/**
	 * Status
	 */
	private Integer status;

}