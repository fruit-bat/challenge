package com.liaquay.anatwine.challenge.productcatalog;

import com.google.common.base.Objects;
import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * The product catalog model for a product
 */
public class Product {

	private final String _name;
	private final double _price;
	private final ProductStatus _status;

	/**
	 * Construct a product
	 *
	 * @param name The name of the product (e.g. Tie, Shirt)
	 * @param price The price of the product in GBP
	 * @param status The availability status of the product
	 */
	public Product(
			final String name,
			final double price,
			final ProductStatus status) {
		_name = name;
		_price = price;
		_status = status;
	}

	/**
	 * Get the name of the product (e.g. Tie, Shirt)
	 *
	 * @return The name of the product (e.g. Tie, Shirt)
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Get the price of the product in GBP
	 *
	 * @return The price of the product in GBP
	 */
	public double getPrice() {
		return _price;
	}

	/**
	 * Get a textual version of the price including currency symbol
	 *
	 * @return a textual version of the price including currency symbol
	 */
	public String getFormattedPrice() {
		return CurrencyFormatter.format(_price);
	}

	/**
	 * Get the availability status of the product
	 *
	 * @return the availability status of the product
	 */
	public ProductStatus getStatus() {
		return _status;
	}

	@Override
	public boolean equals(final Object o) {
		if(!(o instanceof Product)) return false;
		final Product product = (Product)o;
		return Objects.equal(_name, product._name) &&
				Objects.equal(_price, product._price) &&
				Objects.equal(_status, product._status);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_name, _price, _status);
	}

	@Override
	public String toString() {
		return _name + " - " + getFormattedPrice();
	}
}
