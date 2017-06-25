package com.liaquay.anatwine.challenge.basket;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

/**
 * The basket service's model of a basket.
 *
 * This model just stores the names of products mapped to their quantity.
 */
public class Basket {

	/**
	 * A map of the names of products mapped to their quantity
	 */
	private final Map<String, Integer> _products;

	/**
	 * Constructor
	 *
	 * @param products the names of products mapped to their quantity
	 */
	public Basket(final Map<String, Integer> products) {
		_products = ImmutableMap.copyOf(products);
	}

	/**
	 * Get the names of the products in the basket.
	 *
	 * @return the names of the products in the basket.
	 */
	public Set<String> getProductNames() {
		return _products.keySet();
	}

	/**
	 * Get the quantity of a given product in the basket.
	 *
	 * If a product is not present in the basket a quantity of
	 * zero will be reported.
	 *
	 * @param productName the product name
	 * @return the quantity of a given product in the basket
	 */
	public int getQuantity(final String productName) {
		// Get the recorded number of products from the basket
		final Integer quantity = _products.get(productName);
		// If the product is not in the basket we have none of them
		return quantity == null ? 0 : quantity;
	}

	@Override
	public boolean equals(final Object o) {
		if(!(o instanceof Basket)) return false;
		final Basket basket = (Basket)o;
		return Objects.equal(_products, basket._products);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_products);
	}
}
