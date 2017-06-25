package com.liaquay.anatwine.challenge.discount.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.liaquay.anatwine.challenge.clock.ClockService;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;

public class DiscountServiceImplTest {

	final String json = "[{\"validFrom\":\"2017-06-24\",\"validTo\":\"2017-07-2\",\"conditionalProducts\":{\"Shirt\":2},\"targetProduct\": \"Tie\",\"percentage\": 50.0}]";

	private Date _testTime = null;
	private final SimpleDateFormat _persitentDateFormat =  new SimpleDateFormat("yyyy-MM-dd");

	final ClockService clock = new ClockService() {

		@Override
		public Date now() {
			return _testTime;
		}
	};

	private final DiscountServiceImpl _discountServiceImpl = new DiscountServiceImpl(clock);

	@Before
	public void loadDiscounts() {
		try {
			_discountServiceImpl.load(new ByteArrayInputStream(json.getBytes("UTF-8")));
		} catch (final UnsupportedEncodingException e) {
			// This never happens for UTF-8
		}
	}

	@Test
	public void testAppliesToCorrectTimePeriod() throws IOException, ParseException {

		// Set the clock to when we know the discount applies
		_testTime = _persitentDateFormat.parse("2017-06-25");

		assertEquals(
				"The discount should apply to this date",
				1,
				_discountServiceImpl.getPercentageDiscounts().size());
	}

	@Test
	public void testDoesNotAppliesBeforeTimePeriod() throws IOException, ParseException {

		// Set the clock to when we know the discount does not apply
		_testTime = _persitentDateFormat.parse("2017-06-22");

		assertEquals(
				"The discount should not apply to this date",
				0,
				_discountServiceImpl.getPercentageDiscounts().size());
	}

	@Test
	public void testDoesNotAppliesAfterTimePeriod() throws IOException, ParseException {

		// Set the clock to when we know the discount does not apply
		_testTime = _persitentDateFormat.parse("2017-07-3");

		assertEquals(
				"The discount should not apply to this date",
				0,
				_discountServiceImpl.getPercentageDiscounts().size());
	}

	@Test
	public void testContentsOfDiscount() throws ParseException {

		// Set the clock to when we know the discount applies
		_testTime = _persitentDateFormat.parse("2017-06-25");

		final List<PercentageDiscount> discounts = _discountServiceImpl.getPercentageDiscounts();

		assertEquals(
				"The discount should apply to this date",
				1,
				discounts.size());

		final PercentageDiscount discount = discounts.get(0);

		assertEquals(
				"The map of conditional and target product should have constructed correctly",
				ImmutableMap.<String, Integer>builder()
				.put("Shirt", 2)
				.put("Tie", 1)
				.build(),
				discount.getConditionalAndTargetProducts());

		assertEquals(
				"The percentage discount should be correct",
				50.0,
				discount.getPercentage(),
				0.001);

		assertEquals(
				"The target product should be correct",
				"Tie",
				discount.getTargetProduct());
	}
}
