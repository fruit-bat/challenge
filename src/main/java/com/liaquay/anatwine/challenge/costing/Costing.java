package com.liaquay.anatwine.challenge.costing;

import java.util.List;
import java.util.Map;

import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * This is the costing service's model of a costing.
 *
 * The costing of a basket includes the subtotal, total,
 * applied discounts and a list of unavailable products.
 */
public class Costing {

	private final Map<String, Integer> _unavilableProducts;
	private final List<AppliedDiscount> _appliedDiscounts;
	private final double _subtotal;
	private final double _total;

	/**
	 * Constructor
	 *
	 * @param unavilableProducts the names of products that are not available mapped to the missing quantity
	 * @param appliedDiscounts a list of the discounts applied to the basket
	 * @param subtotal the total cost of items in the basket before discounts are applied
	 * @param total the total cost of items in the basket after discounts are applied
	 */
	public Costing(
			final Map<String, Integer> unavilableProducts,
			final List<AppliedDiscount> appliedDiscounts,
			final double subtotal,
			final double total) {
		_unavilableProducts = unavilableProducts;
		_appliedDiscounts = appliedDiscounts;
		_subtotal = subtotal;
		_total = total;
	}

	/**
	 * Get the names of products that are not available mapped to the missing quantity
	 *
	 * @return the names of products that are not available mapped to the missing quantity
	 */
	public final Map<String, Integer> getUnavilableItems() {
		return _unavilableProducts;
	}

	/**
	 * Get a list of the discounts applied to the basket
	 *
	 * @return a list of the discounts applied to the basket
	 */
	public final List<AppliedDiscount> getAppliedDiscounts() {
		return _appliedDiscounts;
	}

	/**
	 * The total cost of items in the basket before discounts are applied
	 *
	 * @return the total cost of items in the basket before discounts are applied
	 */
	public double getSubtotal() {
		return _subtotal;
	}

	/**
	 * The total cost of items in the basket after discounts are applied
	 *
	 * @return the total cost of items in the basket after discounts are applied
	 */
	public double getTotal() {
		return _total;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Subtotal: " + CurrencyFormatter.format(_subtotal) + "\n");
		if(_appliedDiscounts.isEmpty()) {
			sb.append("(No offers availble)\n");
		}
		else {
			_appliedDiscounts.forEach(appliedDiscount -> {
				sb.append(appliedDiscount.toString() + "\n");
			});
		}
		sb.append("Total: " + CurrencyFormatter.format(_total) + "\n");
		if(!_unavilableProducts.isEmpty()) {
			sb.append("\nThe following items were unavilable:\n");
			_unavilableProducts.entrySet().forEach(unavailable -> {
				sb.append(unavailable.getKey() + " x" + unavailable.getValue() + "\n");
			});
		}
		return sb.toString();
	}
}
