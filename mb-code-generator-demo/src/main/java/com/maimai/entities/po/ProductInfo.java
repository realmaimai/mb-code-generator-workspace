package com.maimai.entities.po;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description: Product Information Table
 *
 * @Author: maimai
 * @Date: 20240612
 */
@Data
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

}