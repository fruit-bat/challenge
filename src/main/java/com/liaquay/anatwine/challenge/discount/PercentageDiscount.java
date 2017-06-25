package com.liaquay.anatwine.challenge.discount;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

/**
 * The discount service's model of a percentage discount.
 *
 * The name of the product the discount applies to is referred to as the 'target product'.
 * Additional products that are required to apply the discount are referred to as 'conditional products'.
 */
public class PercentageDiscount {

	private final Map<String, Integer> _conditionalAndTargetProducts;
	private final String _targetProduct;
	private final double _percentage;

	/**
	 * Constructor
	 *
	 * @param conditionalProducts a map of conditional products to their required quantities
	 * @param targetProduct The name of the product to which the discount will be applied
	 * @param percentage The percentage discount to apply to the target product
	 */
	public PercentageDiscount(
			final Map<String, Integer> conditionalProducts,
			final String targetProduct,
			final double percentage) {

		// Combine the target and conditional products
		final Map<String, Integer> conditionalProductsWithTarget = new HashMap<>(conditionalProducts);
		conditionalProductsWithTarget.put(
				targetProduct,
				conditionalProducts.containsKey(targetProduct) ? conditionalProducts.get(targetProduct) + 1 : 1);

		_conditionalAndTargetProducts = Collections.unmodifiableMap(conditionalProductsWithTarget);
		_targetProduct = targetProduct;
		_percentage = percentage;
	}

	/**
	 * Get a map of conditional and target products to their required quantities.
	 *
	 * This map is useful in determining the applicability of a discount.
	 *
	 * @return a map of conditional and target products to their required quantities.
	 */
	public Map<String, Integer> getConditionalAndTargetProducts() {
		return _conditionalAndTargetProducts;
	}

	/**
	 * Get the name of the product to which the discount applies
	 *
	 * @return the name of the product to which the discount applies
	 */
	public String getTargetProduct() {
		return _targetProduct;
	}

	/**
	 * Get the amount of the discount as a percentage
	 *
	 * @return the amount of the discount as a percentage
	 */
	public double getPercentage() {
		return _percentage;
	}

	@Override
	public boolean equals(final Object o) {
		if(!(o instanceof PercentageDiscount)) return false;
		final PercentageDiscount percentageDiscount = (PercentageDiscount)o;
		return Objects.equal(_conditionalAndTargetProducts, percentageDiscount._conditionalAndTargetProducts) &&
				Objects.equal(_targetProduct, percentageDiscount._targetProduct) &&
				Objects.equal(_percentage, percentageDiscount._percentage);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(_conditionalAndTargetProducts, _targetProduct, _percentage);
	}
}
