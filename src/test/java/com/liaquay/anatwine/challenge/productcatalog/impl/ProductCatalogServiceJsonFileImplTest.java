package com.liaquay.anatwine.challenge.productcatalog.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;

/**
 * Test our in memory product catalog.
 *
 * This test only makes sense as the implementation is immutable.
 */
public class ProductCatalogServiceJsonFileImplTest {

	private final String json = "{\"Jacket\": 49.90, \"Trousers\": 35.50, \"Shirt\": 12.50, \"Tie\": 9.50}";

	private final ProductCatalogServiceJsonFileImpl _productCatalogService =
			new ProductCatalogServiceJsonFileImpl();

	@Before
	public void loadProducts() {
		try {
			_productCatalogService.load(new ByteArrayInputStream(json.getBytes("UTF-8")));
		} catch (final UnsupportedEncodingException e) {
			// This never happens for UTF-8
		}
	}

	@Test
	public void testInMemoryCatalog() {
		assertEquals(
				"Simple Catalog correct for Jacket",
				new Product("Jacket", 49.90, ProductStatus.Available),
				_productCatalogService.getProductByName("Jacket"));
		assertEquals(
				"Simple Catalog correct for Trousers",
				new Product("Trousers", 35.50, ProductStatus.Available),
				_productCatalogService.getProductByName("Trousers"));
		assertEquals(
				"Simple Catalog correct for Shirt",
				new Product("Shirt", 12.50, ProductStatus.Available),
				_productCatalogService.getProductByName("Shirt"));
		assertEquals(
				"Simple Catalog correct for Tie",
				new Product("Tie", 9.50, ProductStatus.Available),
				_productCatalogService.getProductByName("Tie"));
	}

	@Test
	public void testProductNotInCatalog() {
		assertEquals(
				"The missing product is correctly identified",
				new Product("NotAProduct", 0.00, ProductStatus.Unavailable),
				_productCatalogService.getProductByName("NotAProduct"));
	}
}
