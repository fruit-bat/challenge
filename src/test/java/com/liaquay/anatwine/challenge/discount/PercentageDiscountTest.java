package com.liaquay.anatwine.challenge.discount;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests for the discount service's model of a percentage discount.
 */
public class PercentageDiscountTest {

	/**
	 * Test constructor and access methods
	 */
	@Test
	public void testConstructorAndAccessMethods() {

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Shirt", 2)
				.build(),
				"Tie",
				10.0);

		assertEquals(
				"The map of conditional and target product should have constructed correctly",
				ImmutableMap.<String, Integer>builder()
				.put("Shirt", 2)
				.put("Tie", 1)
				.build(),
				discount.getConditionalAndTargetProducts());

		assertEquals(
				"The percentage discount should be correct",
				10.0,
				discount.getPercentage(),
				0.001);

		assertEquals(
				"The target product should be correct",
				"Tie",
				discount.getTargetProduct());
	}
}
