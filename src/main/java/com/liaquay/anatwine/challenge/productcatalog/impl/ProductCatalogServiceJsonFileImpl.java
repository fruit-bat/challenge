package com.liaquay.anatwine.challenge.productcatalog.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductCatalogService;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;

/**
 * A simple immutable implementation of a product catalog read from the resource file
 * products.json
 */
@Component
public class ProductCatalogServiceJsonFileImpl implements ProductCatalogService {

	/**
	 * Keep an object mapper as these are slow to create but are thread safe
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Keep an object reader as these are slow to create but are thread safe
	 */
	private static final ObjectReader READER = MAPPER.readerFor(new TypeReference<HashMap<String,Double>>() {});

	/**
	 * A map of product name to price in GBP lazily initialised
	 */
	private Map<String, Double> _catalog = null;

	/**
	 *	Load the catalog from the stream
	 *
	 * 	@param is the stream from which to load the catalog
	 */
	public void load(final InputStream is) {
		try {
			_catalog = READER.readValue(is);
		}
		catch (final IOException e) {
			// Need to know what we are supposed to do if we cannot read the catalog
			throw new RuntimeException("Sorry, could not read product catalog", e);
		}
	}

	/**
	 * Get an instance of the product price map loading it from file if necessary
	 *
	 * @return an instance of the product price map
	 */
	private Map<String, Double> getCatalog() {
		if(_catalog == null) {
			load(this.getClass().getClassLoader().getResourceAsStream("products.json"));
		}
		return _catalog;
	}

	@Override
	public Product getProductByName(final String name) {
		final Double price =  getCatalog().get(name);
		if(price == null) {
			return new Product(name, 0.00, ProductStatus.Unavailable);
		}
		return new Product(name, price, ProductStatus.Available);
	}
}
