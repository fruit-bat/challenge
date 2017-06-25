package com.liaquay.anatwine.challenge.discount.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.liaquay.anatwine.challenge.clock.ClockService;
import com.liaquay.anatwine.challenge.discount.DiscountService;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;

/**
 * Simple immutable implementation of the discount service read from the resource file
 * discounts.json
 */
@Component
public class DiscountServiceImpl implements DiscountService {

	/**
	 * Keep an object mapper as these are slow to create but are thread safe
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * Keep an object reader as these are slow to create but are thread safe
	 */
	private static final ObjectReader READER = MAPPER.readerFor(new TypeReference<ArrayList<PersistentPercentageDiscount>>() {});

	/**
	 * Use a clock service to get the time so the service can be tested
	 */
	private final ClockService _clockService;

	@Autowired
	public DiscountServiceImpl(final ClockService clockService) {
		_clockService = clockService;
	}

	/**
	 * A list of the discounts with their applicable dates
	 */
	private List<PersistentPercentageDiscount> _persistentDiscounts;

	/**
	 *	Load the discounts from the stream
	 *
	 * 	@param is the stream from which to load the discounts
	 */
	public void load(final InputStream is) {
		try {
			_persistentDiscounts = READER.readValue(is);
		}
		catch (final IOException e) {
			// Need to know what we are supposed to do if we cannot read the discounts
			throw new RuntimeException("Sorry, could not read product discounts", e);
		}
	}

	/**
	 * Get an instance of the product discounts loading them from file if necessary
	 *
	 * @return the product discounts
	 */
	private List<PersistentPercentageDiscount> getDiscounts() {
		if(_persistentDiscounts == null) {
			load(this.getClass().getClassLoader().getResourceAsStream("discounts.json"));
		}
		return _persistentDiscounts;
	}

	/**
	 * Get a list of discounts filtered by date range
	 */
	@Override
	public List<PercentageDiscount> getPercentageDiscounts() {
		// Get the current date from out clock service
		final Date now = _clockService.now();

		return getDiscounts().stream()
				// filter the discounts by date
				.filter(discount -> {
					return discount.getValidFrom().before(now) &&
							discount.getValidTo().after(now);
				})
				// Convert to the Discount service model
				.map(discount -> {
					return new PercentageDiscount(
							discount.getConditionalProducts(),
							discount.getTargetProduct(),
							discount.getPercentage());
				})
				// Return as a list
				.collect(Collectors.toList());
	}
}
