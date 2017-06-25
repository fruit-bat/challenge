package com.liaquay.anatwine.challenge.costing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * Tests for the costing service's model of a discount
 */
public class AppliedDiscountTest {

	/**
	 * Test the discount when applying once
	 */
	@Test
	public void testApplyDiscountOnce() {

		final String description = "description";
		final int applicationCount = 1;
		final double discountPerApplication = 21.3;

		final AppliedDiscount appliedDiscount = new AppliedDiscount(
				description,
				applicationCount,
				discountPerApplication);

		assertEquals(
			"The applied discount should be the same as the one supplied",
			discountPerApplication,
			appliedDiscount.getTotalDiscount(),
			0.001);

		assertEquals(
				"The applied discount should format correctly as a string",
				description + ": -" + CurrencyFormatter.format(discountPerApplication),
				appliedDiscount.toString());
	}

	/**
	 * Test the discount when applying twice
	 */
	@Test
	public void testApplyDiscountTwice() {

		final String description = "description";
		final int applicationCount = 2;
		final double discountPerApplication = 21.3;

		final AppliedDiscount appliedDiscount = new AppliedDiscount(
				description,
				applicationCount,
				discountPerApplication);

		final double expectedDiscount = applicationCount * discountPerApplication;

		assertEquals(
			"The applied discount should mulitpled by the number of applications",
			expectedDiscount,
			appliedDiscount.getTotalDiscount(),
			0.001);

		assertEquals(
				"The applied discount should format correctly as a string",
				description + " (x" + applicationCount + "): -" + CurrencyFormatter.format(expectedDiscount),
				appliedDiscount.toString());
	}
}
