package com.liaquay.anatwine.challenge.basket;

/**
 * Service for storing/retrieving products placed in a customers basket.
 *
 * Implementations could include MongoDB, JDBC, etc.
 *
 * Note:
 *
 * Implementations of this interface may require knowledge of the
 * current customers identity. Here it is assumed that the customer
 * identity will be transported without need for additional method
 * arguments e.g. thread local storage.
 */
public interface BasketService {

	/**
	 * Remove all products from the customers basket.
	 */
	public void clear();

	/**
	 * Add a single product to the customers basket.
	 */
	public void add(final String productName);

	/**
	 * Get the current basket contents including the names of the
	 * products to purchase along with their quantity.
	 *
	 * This snapshot gives a stable model for costing.
	 *
	 * @return the current basket
	 */
	public Basket getCurrentBasket();
}
