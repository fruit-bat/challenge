package com.liaquay.anatwine.challenge.productcatalog.impl;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductCatalogService;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;

/**
 * A simple immutable implementation of a product catalog
 */
public class ProductCatalogServiceSimpleImpl implements ProductCatalogService {

	final Map<String, Double> _catalog = ImmutableMap.<String, Double>builder().
			put("Jacket", 49.90).
			put("Trousers", 35.50).
			put("Shirt", 12.50).
			put("Tie", 9.50).
			build();

	@Override
	public Product getProductByName(final String name) {
		final Double price =  _catalog.get(name);
		if(price == null) {
			return new Product(name, 0.00, ProductStatus.Unavailable);
		}
		return new Product(name, price, ProductStatus.Available);
	}
}
