package com.liaquay.anatwine.challenge.basket;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

/**
 * Tests for the basket service's model of a basket
 */
public class BasketTest {

	/**
	 * Test the constructors and access methods
	 */
	@Test
	public void test() {

		final Map<String, Integer> products = ImmutableMap.<String, Integer>builder()
				.put("Tie", 1)
				.put("Trousers", 2)
				.build();

		final Basket basket = new Basket(products);

		assertEquals(
				"The basket should contain the correct products",
				Sets.newHashSet("Tie", "Trousers"),
				basket.getProductNames());

		assertEquals(
				"The basket should contain the correct number of ties",
				1,
				basket.getQuantity("Tie"));

		assertEquals(
				"The basket should contain the correct number of trousers",
				2,
				basket.getQuantity("Trousers"));

		assertEquals(
				"The basket should not contain any caterpillars",
				0,
				basket.getQuantity("Caterpillars"));
	}
}
