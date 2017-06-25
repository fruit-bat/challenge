package com.liaquay.anatwine.challenge.costing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liaquay.anatwine.challenge.basket.Basket;
import com.liaquay.anatwine.challenge.basket.BasketService;
import com.liaquay.anatwine.challenge.costing.Costing;
import com.liaquay.anatwine.challenge.costing.CostingService;
import com.liaquay.anatwine.challenge.discount.DiscountService;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;
import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductCatalogService;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;
import com.liaquay.anatwine.challenge.util.CurrencyFormatter;

/**
 * Tests for the costing service used for calculating costs and discounts
 */
public class CostingServiceImplTest {

	/**
	 * Test the service correctly reports for a basket with no discounts
	 */
	@Test
	public void testBasketWithNoDiscounts() {

		final BasketService basketService = mock(BasketService.class);
		final ProductCatalogService productCatalogService = mock(ProductCatalogService.class);
		final DiscountService discountService = mock(DiscountService.class);

		final CostingService costingService = new CostingServiceImpl(
				basketService,
				productCatalogService,
				discountService);

		final Basket basket = new Basket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 3)
				.put("Shirt", 5)
				.build());

		when(basketService.getCurrentBasket()).thenReturn(basket);

		when(productCatalogService.getProductByName("Jacket")).thenReturn(new Product("Jacket", 49.9, ProductStatus.Available));
		when(productCatalogService.getProductByName("Trousers")).thenReturn(new Product("Trousers", 35.50, ProductStatus.Available));
		when(productCatalogService.getProductByName("Shirt")).thenReturn(new Product("Shirt", 12.50, ProductStatus.Available));

		final Costing costing = costingService.getCosting();

		final double expectedSubtotal = (49.9 * 1) + (35.50 * 3) + (12.50 * 5);

		assertEquals(
				"The subtotal should be correct",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(costing.getSubtotal()));

		assertEquals(
				"The total should be the same as the subtotal",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(costing.getTotal()));

		assertTrue(
				"There should be no discounts applied",
				costing.getAppliedDiscounts().isEmpty());

		assertEquals(
				"The costing should convert to a string in the expected format",
				"Subtotal: " + CurrencyFormatter.format(expectedSubtotal) + "\n" +
				"(No offers availble)\n" +
				"Total: " + CurrencyFormatter.format(expectedSubtotal) + "\n",
				costing.toString());
	}

	/**
	 * Test the service correctly reports for a basket with a single discount
	 */
	@Test
	public void testBasketWithSingleDiscount() {

		final BasketService basketService = mock(BasketService.class);
		final ProductCatalogService productCatalogService = mock(ProductCatalogService.class);
		final DiscountService discountService = mock(DiscountService.class);

		final CostingService costingService = new CostingServiceImpl(
				basketService,
				productCatalogService,
				discountService);

		final Basket basket = new Basket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 1)
				.put("Trousers", 3)
				.build());

		final List<PercentageDiscount> discounts = Lists.newArrayList(
				new PercentageDiscount(Maps.newHashMap(), "Jacket", 10.0));

		when(basketService.getCurrentBasket()).thenReturn(basket);

		when(productCatalogService.getProductByName("Jacket")).thenReturn(new Product("Jacket", 49.9, ProductStatus.Available));
		when(productCatalogService.getProductByName("Trousers")).thenReturn(new Product("Trousers", 35.50, ProductStatus.Available));

		when(discountService.getPercentageDiscounts()).thenReturn(discounts);

		final Costing costing = costingService.getCosting();

		final double expectedSubtotal = (49.9 * 1) + (35.50 * 3);

		final double expectedTotal = (49.9 * 1 * 90 / 100) + (35.50 * 3);

		assertEquals(
				"The subtotal should be correct",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(costing.getSubtotal()));

		assertEquals(
				"The total should have the discount applied",
				CurrencyFormatter.format(expectedTotal),
				CurrencyFormatter.format(costing.getTotal()));

		assertEquals(
				"There should be one discounts applied",
				1,
				costing.getAppliedDiscounts().size());

		assertEquals(
				"The costing should convert to a string in the expected format",
				"Subtotal: " + CurrencyFormatter.format(expectedSubtotal) + "\n" +
				"Jacket 10.0% off: -" + CurrencyFormatter.format(expectedSubtotal - expectedTotal) + "\n" +
				"Total: " + CurrencyFormatter.format(expectedTotal) + "\n",
				costing.toString());
	}

	/**
	 * Test the service correctly reports for a basket with multiple discounts of the same product
	 */
	@Test
	public void testBasketWithMultipleDiscountsSameProduct() {

		final BasketService basketService = mock(BasketService.class);
		final ProductCatalogService productCatalogService = mock(ProductCatalogService.class);
		final DiscountService discountService = mock(DiscountService.class);

		final CostingService costingService = new CostingServiceImpl(
				basketService,
				productCatalogService,
				discountService);

		final Basket basket = new Basket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 2)
				.put("Trousers", 3)
				.build());

		final List<PercentageDiscount> discounts = Lists.newArrayList(
				new PercentageDiscount(Maps.newHashMap(), "Jacket", 10.0));

		when(basketService.getCurrentBasket()).thenReturn(basket);

		when(productCatalogService.getProductByName("Jacket")).thenReturn(new Product("Jacket", 49.9, ProductStatus.Available));
		when(productCatalogService.getProductByName("Trousers")).thenReturn(new Product("Trousers", 35.50, ProductStatus.Available));

		when(discountService.getPercentageDiscounts()).thenReturn(discounts);

		final Costing costing = costingService.getCosting();

		final double expectedSubtotal = (49.9 * 2) + (35.50 * 3);

		final double expectedTotal = (49.9 * 2 * 90 / 100) + (35.50 * 3);

		assertEquals(
				"The subtotal should be correct",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(costing.getSubtotal()));

		assertEquals(
				"The total should have the discount applied",
				CurrencyFormatter.format(expectedTotal),
				CurrencyFormatter.format(costing.getTotal()));

		assertEquals(
				"There should be one discounts applied",
				1,
				costing.getAppliedDiscounts().size());

		assertEquals(
				"The costing should convert to a string in the expected format",
				"Subtotal: " + CurrencyFormatter.format(expectedSubtotal) + "\n" +
				"Jacket 10.0% off (x2): -" + CurrencyFormatter.format(expectedSubtotal - expectedTotal) + "\n" +
				"Total: " + CurrencyFormatter.format(expectedTotal) + "\n",
				costing.toString());
	}

	/**
	 * Test the service correctly reports for a basket with discounts on different product types
	 */
	@Test
	public void testBasketWithMultipleDiscountsDifferentProducts() {

		final BasketService basketService = mock(BasketService.class);
		final ProductCatalogService productCatalogService = mock(ProductCatalogService.class);
		final DiscountService discountService = mock(DiscountService.class);

		final CostingService costingService = new CostingServiceImpl(
				basketService,
				productCatalogService,
				discountService);

		final Basket basket = new Basket(ImmutableMap.<String, Integer>builder()
				.put("Jacket", 2)
				.put("Trousers", 3)
				.build());

		final List<PercentageDiscount> discounts = Lists.newArrayList(
				new PercentageDiscount(Maps.newHashMap(), "Jacket", 10.0),
				new PercentageDiscount(Maps.newHashMap(), "Trousers", 20.0));

		when(basketService.getCurrentBasket()).thenReturn(basket);

		when(productCatalogService.getProductByName("Jacket")).thenReturn(new Product("Jacket", 49.9, ProductStatus.Available));
		when(productCatalogService.getProductByName("Trousers")).thenReturn(new Product("Trousers", 35.50, ProductStatus.Available));

		when(discountService.getPercentageDiscounts()).thenReturn(discounts);

		final Costing costing = costingService.getCosting();

		final double expectedSubtotal = (49.9 * 2) + (35.50 * 3);

		final double expectedDiscountJacket = 49.9 * 2 * 10 / 100;
		final double expectedDiscountTrousers = 35.50 * 3 * 20 / 100;

		final double expectedTotal = expectedSubtotal - expectedDiscountJacket - expectedDiscountTrousers;

		assertEquals(
				"The subtotal should be correct",
				CurrencyFormatter.format(expectedSubtotal),
				CurrencyFormatter.format(costing.getSubtotal()));

		assertEquals(
				"The total should have the discount applied",
				CurrencyFormatter.format(expectedTotal),
				CurrencyFormatter.format(costing.getTotal()));

		assertEquals(
				"There should be two discounts applied",
				2,
				costing.getAppliedDiscounts().size());

		assertEquals(
				"The costing should convert to a string in the expected format",
				"Subtotal: " + CurrencyFormatter.format(expectedSubtotal) + "\n" +
				"Jacket 10.0% off (x2): -" + CurrencyFormatter.format(expectedDiscountJacket) + "\n" +
				"Trousers 20.0% off (x3): -" + CurrencyFormatter.format(expectedDiscountTrousers) + "\n" +
				"Total: " + CurrencyFormatter.format(expectedTotal) + "\n",
				costing.toString());
	}
}
