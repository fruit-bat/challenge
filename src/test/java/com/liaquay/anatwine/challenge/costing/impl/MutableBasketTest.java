package com.liaquay.anatwine.challenge.costing.impl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.liaquay.anatwine.challenge.basket.Basket;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;
import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;
import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * Tests for the costing service scratch-pad, used for calculating costs and discounts
 */
public class MutableBasketTest {

	private MutableBasket makeMutableBasket(final Map<String, Integer> productsMap) {
		final Basket basket = new Basket(productsMap);
		return new MutableBasket(basket);
	}

	/**
	 * Check that the products and counts from a customers basket are correctly
	 * entered into the mutable basket, ready for discounts to be applied.
	 */
	@Test
	public void testConstructor() {

		final Map<String, Integer> productsMap = ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 4)
				.build();

		final MutableBasket mutableBasket = makeMutableBasket(productsMap);

		assertEquals(
				"The mutable basket should construct with the correct product counts",
				productsMap,
				mutableBasket.getProductCounts());
	}

	/**
	 * A simple check of a positive match of a discount against the basket
	 */
	@Test
	public void testDiscountCheckPositive() {

		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 3)
				.build(),
				"Tie",
				10.0);

		assertTrue(
				"Should be able to apply this discount",
				mutableBasket.canApplyDiscount(discount));
	}

	/**
	 * Test the discount does not apply if insufficient items are available in the basket
	 */
	@Test
	public void testDiscountCheckNegativeInsufficientAvailable() {

		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 3)
				.put("Shirt", 3)
				.put("Tie", 3)
				.build(),
				"Tie",
				10.0);

		assertFalse(
				"Should not have been able to apply this discount",
				mutableBasket.canApplyDiscount(discount));
	}

	/**
	 * Test the discount does not apply if a required items is not present in the basket
	 */
	@Test
	public void testDiscountCheckNegativeMissingProduct() {

		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Antelope", 1)
				.put("Trousers", 3)
				.put("Shirt", 3)
				.put("Tie", 3)
				.build(),
				"Tie",
				10.0);

		assertFalse(
				"Should not have been able to apply this discount",
				mutableBasket.canApplyDiscount(discount));
	}

	/**
	 * Check that as a discount is applied relevant items are removed from the basket
	 */
	@Test
	public void testDiscountItemsAreRemovedFromBasket() {
		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 3)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Trousers", 1)
				.put("Shirt", 2)
				.put("Tie", 1)
				.build(),
				"Tie",
				10.0);

		assertTrue(
				"The discount should have applied",
				mutableBasket.applyDiscount(discount));

		assertEquals(
				"Discount items should have been removed from the basket",
				ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 1)
				.put("Shirt", 1)
				.put("Tie", 2)
				.build(),
				mutableBasket.getProductCounts());
	}

	/**
	 * Check that where a discount could be applied more than once, it is.
	 */
	@Test
	public void testDiscountsAreAppliedExhautively() {
		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 4)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Shirt", 2)
				.build(),
				"Tie",
				10.0);

		assertEquals(
				"The discount should have applied 2 times",
				2,
				mutableBasket.applyDiscountExhaustively(discount));

		assertEquals(
				"Discount items should have been removed from the basket",
				ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 2)
				.put("Shirt", 0)
				.put("Tie", 2)
				.build(),
				mutableBasket.getProductCounts());
	}

	/**
	 * Check that multiple offers are applied to the basket.
	 */
	@Test
	public void testMulipleDiscountsAreAppliedExhautively() {
		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 3)
				.put("Shirt", 4)
				.put("Tie", 4)
				.build());

		final PercentageDiscount discount1 = new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.put("Shirt", 2)
				.build(),
				"Tie",
				50.0);

		final PercentageDiscount discount2 =  new PercentageDiscount(
				ImmutableMap.<String, Integer>builder()
				.build(),
				"Trousers",
				10.0);

		final List<PercentageDiscount> discounts = Lists.newArrayList(discount1, discount2);

		assertEquals(
				"The correct discounts should have been applied",
				ImmutableMap.<PercentageDiscount, Integer>builder()
				.put(discount1, 2)
				.put(discount2, 3)
				.build(),
				mutableBasket.applyDiscountsExhaustively(discounts));
	}

	/**
	 * Test the calculate of the value of the basket
	 */
	@Test
	public void testCalculateSubtotal() {

		final MutableBasket mutableBasket = makeMutableBasket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 3)
				.put("Shirt", 5)
				.build());

		final Map<String, Product> pricedBasketProducts = ImmutableMap.<String, Product>builder()
				.put("Jacket", new Product("Jacket", 7.0, ProductStatus.Available))
				.put("Trousers", new Product("Trousers", 13.0, ProductStatus.Available))
				.put("Shirt", new Product("Shirt", 27.0, ProductStatus.Available))
				.build();

		final double expectedSubtotal = (1 * 7.0) + (3 * 13.0) + (5 * 27.0);

		final double actualSubtotal = mutableBasket.calculateSubtotal(pricedBasketProducts);

		assertEquals(
				"The subtotal should be caluculated correctly",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(actualSubtotal));
	}
}
