package com.liaquay.anatwine.challenge.productcatalog;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the product catalog model class
 */
public class ProductTest {

	/**
	 * Test the Product class constructs correctly
	 */
	@Test
	public void testConstructAndInspect() {

		final Product product = new Product("Tie", 12.45, ProductStatus.Available);
		assertEquals(
				"The name of the product should be correct",
				"Tie",
				product.getName());
		assertEquals(
				"The price of the product should be correct",
				12.45,
				product.getPrice(),
				0.001);
		assertEquals(
				"The formatted price should be correct",
				"£12.45",
				product.getFormattedPrice());
		assertEquals(
				"The status should be correct",
				ProductStatus.Available,
				product.getStatus());
		assertEquals(
				"The string form should be correct",
				"Tie - £12.45",
				product.toString());
	}
}
