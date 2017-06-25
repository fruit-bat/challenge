package com.liaquay.anatwine.challenge.costing.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.liaquay.anatwine.challenge.basket.Basket;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;
import com.liaquay.anatwine.challenge.productcatalog.Product;

/**
 * This is a basket used as a scratch-pad in deciding applicable discounts
 */
public class MutableBasket {

	/**
	 * A map of product names against instance counts
	 */
	final Map<String, Integer> _productCounts;

	/**
	 * Constructor
	 *
	 * @param basket A customer basket from the basket service
	 */
	public MutableBasket(final Basket basket) {
		_productCounts = basket.getProductNames().stream()
				.collect(Collectors.toMap(
						productName -> productName,
						productName -> basket.getQuantity(productName)));
	}

	/**
	 * Get the current map of product names against instance counts
	 *
	 * @return the current map of product names against instance counts
	 */
	public Map<String, Integer> getProductCounts() {
		return _productCounts;
	}

	/**
	 * Test to see if the discount is applicable to this basket
	 *
	 * This test just checks there are enough items in the basket to
	 * meet the requirements of the 'percentage discount'.
	 *
	 * @param discount the discount service model of a discount
	 * @return true if the discount is applicable to this basket
	 */
	public boolean canApplyDiscount(
			final PercentageDiscount discount) {

		return !discount.getConditionalAndTargetProducts().entrySet().stream()
				// In this case a match is a failed condition (so the test can exit early)
				.anyMatch(entry ->  {
					final String requiredProductName = entry.getKey();
					final int requiredQuantity = entry.getValue();
					final int actualQuantity = _productCounts.containsKey(requiredProductName) ? _productCounts.get(requiredProductName) : 0;
					// True for condition not met
					return requiredQuantity > actualQuantity;
				});
	}

	/**
	 * If a discount can be applied then both the conditional and target products
	 * are removed from those available for later discounts to be applied.
	 *
	 * As such, the implementation currently prevents multiple discounts being applied to a
	 * group of items even if the item only formed a condition for a discount. This may or
	 * may not be desirable.
	 *
	 * It should be noted that the order that discounts are applied is important.
	 *
	 * @param discount The discount to apply
	 * @return true if the discount was applied
	 */
	public boolean applyDiscount(
			final PercentageDiscount discount) {

		if(canApplyDiscount(discount)) {
			discount.getConditionalAndTargetProducts().entrySet().forEach(productAndQuantity -> {
				final String productName = productAndQuantity.getKey();
				final int productQuantity = productAndQuantity.getValue();
				_productCounts.put(
						productName,
						_productCounts.get(productName) - productQuantity);
			});
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Apply a discount repeatedly until there are no products left for it to apply to.
	 *
	 * @param discount The discount to apply
	 * @return the number of times the discount was applied
	 */
	public int applyDiscountExhaustively(
			final PercentageDiscount discount) {

		int count = 0;
		while(applyDiscount(discount)) {
			++count;
		}
		return count;
	}

	/**
	 * Apply all of the discounts until no more can be applied
	 *
	 * @param discounts the discounts to apply
	 * @return a map of discounts to the number of times they can be applied
	 */
	public Map<PercentageDiscount, Integer> applyDiscountsExhaustively(
			final List<PercentageDiscount> discounts) {

		return discounts.stream().
				collect(Collectors.toMap(
						discount -> discount,
						discount -> applyDiscountExhaustively(discount)));
	}

	/**
	 * Calculate the subtotal of the items in the basket given their prices
	 *
	 * @param pricedBasketProducts a map of product names to their prices
	 * @return the subtotal of the basket in GBP
	 */
	public double calculateSubtotal(final Map<String, Product> pricedBasketProducts) {

		return _productCounts.entrySet().stream()
				.mapToDouble(productCount -> {
					final String productName = productCount.getKey();
					final int count = productCount.getValue();
					final Product product = pricedBasketProducts.get(productName);
					return product.getPrice() * count;
				})
				.sum();
	}
}
