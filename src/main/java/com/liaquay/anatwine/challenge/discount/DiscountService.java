package com.liaquay.anatwine.challenge.discount;

import java.util.List;

/**
 * Service for retrieving discounts.
 *
 * Implementations could include MongoDB, JDBC, etc.
 */
public interface DiscountService {

	/**
	 * Get a list of current percentage discounts
	 *
	 * @return a list of current discounts
	 */
	public List<PercentageDiscount> getPercentageDiscounts();
}
