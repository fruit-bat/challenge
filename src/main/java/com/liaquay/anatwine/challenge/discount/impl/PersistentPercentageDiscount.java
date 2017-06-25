package com.liaquay.anatwine.challenge.discount.impl;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Model of a discount used for persistence.
 *
 * This class must be mutable to keep jackson happy
 */
public class PersistentPercentageDiscount {

	private Date _validFrom;
	private Date _validTo;
	private Map<String, Integer> _conditionalProducts;
	private String _targetProduct;
	private double _percentage;

	public PersistentPercentageDiscount() {
	}

	public PersistentPercentageDiscount(
			final Date validFrom,
			final Date validTo,
			final Map<String, Integer> conditionalProducts,
			final String targetProduct,
			final double percentage) {

		_validFrom = validFrom;
		_validTo = validTo;
		_conditionalProducts = conditionalProducts;
		_targetProduct = targetProduct;
		_percentage = percentage;
	}

	/**
	 * Get the date the discount is valid from.
	 *
	 * @return the date the discount is valid from.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getValidFrom() {
		return _validFrom;
	}

	/**
	 * Get the date the discount is valid to.
	 *
	 * @return the date the discount is valid to.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getValidTo() {
		return _validTo;
	}

	/**
	 * Get a map of conditional products to their required quantities.
	 *
	 * @return a map of conditional products to their required quantities.
	 */
	public Map<String, Integer> getConditionalProducts() {
		return _conditionalProducts;
	}

	/**
	 * Get the name of the product to which the discount applies
	 *
	 * @return the name of the product to which the discount applies
	 */
	public String getTargetProduct() {
		return _targetProduct;
	}

	/**
	 * Get the amount of the discount as a percentage
	 *
	 * @return the amount of the discount as a percentage
	 */
	public double getPercentage() {
		return _percentage;
	}

	// Setters to keep jackson happy

	public void setValidFrom(final Date validFrom) {
		_validFrom = validFrom;
	}

	public void setValidTo(final Date validTo) {
		_validTo = validTo;
	}

	public void setConditionalProducts(final Map<String, Integer> conditionalProducts) {
		_conditionalProducts = conditionalProducts;
	}

	public void setTargetProduct(final String targetProduct) {
		_targetProduct = targetProduct;
	}

	public void setPercentage(final double percentage) {
		_percentage = percentage;
	}
}
