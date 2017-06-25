package com.liaquay.anatwine.challenge.costing;

import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * The costing service model of a discount that has been applied to the basket
 */
public class AppliedDiscount {

	private final String _description;
	private final int _applicationCount;
	private final double _discountPerApplication;

	/**
	 * Constructor
	 *
	 * @param description A description of the discount e.g. Tie 10% off
	 * @param applicationCount The number of times the discount was applied to the basket
	 * @param discountPerApplication The discount in GBP per application
	 */
	public AppliedDiscount(
			final String description,
			final int applicationCount,
			final double discountPerApplication) {

		_description = description;
		_applicationCount = applicationCount;
		_discountPerApplication = discountPerApplication;
	}

	/**
	 * The total discount in GBP.
	 *
	 * This is the product of the discount and the number of times it has been applied.
	 *
	 * @return the total discount in GBP
	 */
	public double getTotalDiscount() {
		return _discountPerApplication * _applicationCount;
	}

	@Override
	public String toString() {
		return _description +
				(_applicationCount > 1 ? " (x" + _applicationCount + ")" : "") +
				": -" + CurrencyFormatter.format(getTotalDiscount());
	}
}
