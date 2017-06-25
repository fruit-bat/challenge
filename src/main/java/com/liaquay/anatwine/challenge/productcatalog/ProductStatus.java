package com.liaquay.anatwine.challenge.productcatalog;

/**
 * A simple concept of product status with just 2 states.
 *
 * In a richer implementation we might want to be considering stock levels, delivery lead time etc.
 */
public enum ProductStatus {
	/**
	 * The product is available for sale
	 */
	Available,

	/**
	 * The product is not available for sale
	 */
	Unavailable
}
