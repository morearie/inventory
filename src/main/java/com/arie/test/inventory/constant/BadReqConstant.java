package com.arie.test.inventory.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BadReqConstant {
	public static final String PRODUCT_NOT_EXIST = "Product is not exist";
	public static final String PRODUCT_STOCK = "Insufficient stock";
}
