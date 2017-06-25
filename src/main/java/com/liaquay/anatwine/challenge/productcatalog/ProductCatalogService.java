package com.liaquay.anatwine.challenge.productcatalog;

/**
 * Interface to the product catalog service.
 *
 * Implementations could include MongoDB, JDBC, etc.
 */
public interface ProductCatalogService {

	/**
	 * Fetch a product given its name
	 *
	 * @param name the name of the product
	 *
	 * @return the product price and availability
	 */
	public Product getProductByName(final String name);
}
