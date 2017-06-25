package com.liaquay.anatwine.challenge.productcatalog.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductCatalogService;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;

/**
 * Test our in memory product catalog.
 *
 * This test only makes sense as the implementation is immutable.
 */
public class ProductCatalogServiceSimpleImplTest {

	private final ProductCatalogService _productCatalogService =
			new ProductCatalogServiceSimpleImpl();

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
