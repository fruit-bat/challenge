package com.liaquay.anatwine.challenge.basket.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.liaquay.anatwine.challenge.basket.BasketService;

/**
 * Test our in memory basket implementation
 */
public class BasketServiceImplTest {

	/**
	 * Factory method for the basket service
	 *
	 * @return the basket service
	 */
	private BasketService createBasketService() {
		return new BasketServiceImpl();
	}

	/**
	 * Check we can add and recall products in our customers basket
	 */
	@Test
	public void testAddAndRecall() {
		final BasketService basketService = createBasketService();

		assertTrue(
				"The basket should not have any items in it",
				basketService.getCurrentBasket().getProductNames().isEmpty());

		basketService.add("Tie");
		basketService.add("Trousers");
		basketService.add("Trousers");

		assertEquals(
				"The basket should contain the correct products",
				Sets.newHashSet("Tie", "Trousers"),
				basketService.getCurrentBasket().getProductNames());

		assertEquals(
				"The basket should contain the correct number of ties",
				1,
				basketService.getCurrentBasket().getQuantity("Tie"));

		assertEquals(
				"The basket should contain the correct number of trousers",
				2,
				basketService.getCurrentBasket().getQuantity("Trousers"));

		assertEquals(
				"The basket should not contain any caterpillars",
				0,
				basketService.getCurrentBasket().getQuantity("Caterpillars"));
	}

	/**
	 * Check we can clear the basket
	 */
	@Test
	public void testClearBasket() {
		final BasketService basketService = createBasketService();


		basketService.add("Tie");
		basketService.add("Trousers");
		basketService.add("Trousers");

		assertTrue(
				"The basket should have items in it",
				!basketService.getCurrentBasket().getProductNames().isEmpty());

		basketService.clear();

		assertTrue(
				"The basket should not have any items in it",
				basketService.getCurrentBasket().getProductNames().isEmpty());

		assertEquals(
				"The basket should contain no ties",
				0,
				basketService.getCurrentBasket().getQuantity("Tie"));

		assertEquals(
				"The basket should contain no trousers",
				0,
				basketService.getCurrentBasket().getQuantity("Trousers"));
	}
}
