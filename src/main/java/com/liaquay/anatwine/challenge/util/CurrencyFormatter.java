package com.liaquay.anatwine.challenge.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility to present doubles as GBP.
 */
public class CurrencyFormatter {

	// Get the currency formatter for england
	private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.UK);

	/**
	 * Format a double as GBP
	 *
	 * @param amount value in pounds
	 * @return amount in GBP
	 */
	public static String format(final double amount) {
		return CURRENCY_FORMATTER.format(amount);
	}
}
