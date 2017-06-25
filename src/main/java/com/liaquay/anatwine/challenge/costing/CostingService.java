package com.liaquay.anatwine.challenge.costing;

/**
 * The costing service is responsible for costing a basket.
 */
public interface CostingService {

	/**
	 * Cost the customer's current basket
	 *
	 * @return Costing of the customer's current basket
	 */
	public Costing getCosting();
}
