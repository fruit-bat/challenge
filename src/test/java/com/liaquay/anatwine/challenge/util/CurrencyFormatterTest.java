package com.liaquay.anatwine.challenge.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for currency formatter utility
 */
public class CurrencyFormatterTest {

	/**
	 * Check currencies are formatted correctly
	 */
	@Test
	public void testCurrencyFormatter() {
		assertEquals(
				"The currency should have been formatted correctly",
				"£12.59",
				CurrencyFormatter.format(12.59));
	}
}
