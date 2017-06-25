package com.liaquay.anatwine.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.liaquay.anatwine.challenge.basket.BasketService;
import com.liaquay.anatwine.challenge.costing.Costing;
import com.liaquay.anatwine.challenge.costing.CostingService;

/**
 * Application implementation for the Anatwine Challenge
 */
@Configuration
@ComponentScan
@Component
public class AnatwineBasket {

	private final BasketService _basketService;
	private final CostingService _costingService;

	@Autowired
	public AnatwineBasket(
			final BasketService service,
			final CostingService costingService) {
		_basketService = service;
		_costingService = costingService;
	}

	/**
	 * Cost the supplied products and deliver the output to standard out
	 *
	 * @param productNames
	 */
	private void costProducts(final String[] productNames) {

		// Add items to the basket
		for(final String productName : productNames) {
			_basketService.add(productName);
		}

		// Cost the basket
		final Costing costing =_costingService.getCosting();

		// Write results to stdout
		System.out.println(costing);
	}

	/**
	 * Command line input for basket.
	 *
	 * @param productNames The names of the products in the customers basket
	 */
	public static void main(final String[] productNames) {

		@SuppressWarnings("resource") // Ignore warnings about resource closing as it handled by the shutdown hook below
		// Build the application using spring to inject dependencies into our components
		final AbstractApplicationContext springContext = new AnnotationConfigApplicationContext(AnatwineBasket.class);

	    // add a shutdown hook for the above context...
		springContext.registerShutdownHook();

		// Lookup the main application bean in the spring context (which happens to be an instance of this class).
		final AnatwineBasket anatwineBasket = springContext.getBean(AnatwineBasket.class);

		// Perform the calculation and write out any results
		anatwineBasket.costProducts(productNames);
	}
}
