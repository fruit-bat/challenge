package com.liaquay.anatwine.challenge.basket.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.liaquay.anatwine.challenge.basket.Basket;
import com.liaquay.anatwine.challenge.basket.BasketService;

/**
 * Simple in memory implementation of a shopping basket.
 *
 * This implementation is only suitable for a single customer.
 */
@Component
public class BasketServiceImpl implements BasketService {

	/**
	 * A mutable integer
	 */
	private static class Quantity {
		private int _value;

		public Quantity() {
			_value = 0;
		}

		public void increment() {
			_value++;
		}

		public int getValue() {
			return _value;
		}
	}

	/**
	 * A map of product names to their quantities in the basket
	 */
	private final Map<String, Quantity> _products = new HashMap<>();

	@Override
	public void clear() {
		_products.clear();
	}

	@Override
	public void add(final String productName) {
		if(!_products.containsKey(productName)) {
			_products.put(productName, new Quantity());
		}
		_products.get(productName).increment();
	}

	@Override
	public Basket getCurrentBasket() {
		final Map<String, Integer> products = _products
				.entrySet()
				.stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> e.getValue().getValue()));

		return new Basket(products);
	}
}
