package com.liaquay.anatwine.challenge.costing.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liaquay.anatwine.challenge.basket.Basket;
import com.liaquay.anatwine.challenge.basket.BasketService;
import com.liaquay.anatwine.challenge.costing.AppliedDiscount;
import com.liaquay.anatwine.challenge.costing.Costing;
import com.liaquay.anatwine.challenge.costing.CostingService;
import com.liaquay.anatwine.challenge.discount.DiscountService;
import com.liaquay.anatwine.challenge.discount.PercentageDiscount;
import com.liaquay.anatwine.challenge.productcatalog.Product;
import com.liaquay.anatwine.challenge.productcatalog.ProductCatalogService;
import com.liaquay.anatwine.challenge.productcatalog.ProductStatus;

/**
 * This service implementation calculates the costing of a basket
 * including the subtotal, total, applied discounts and builds a
 * list of unavailable products.
 */
@Component
public class CostingServiceImpl implements CostingService {

	private final BasketService _basketService;
	private final ProductCatalogService _productCatalogService;
	private final DiscountService _discountService;

	@Autowired
	public CostingServiceImpl(
			final BasketService basketService,
			final ProductCatalogService productCatalogService,
			final DiscountService discountService) {

		_basketService = basketService;
		_productCatalogService = productCatalogService;
		_discountService = discountService;
	}

	/**
	 * From the customer's basket make note of products that are not available in the product catalog.
	 *
	 * @param basket the customer's basket from the basket service
	 * @param pricedBasketProducts a map of product names to product details
	 * @return a map of product names to the number unavailable in the basket.
	 */
	private Map<String, Integer> getUnavilableProducts(
			final Basket basket,
			final Map<String, Product> pricedBasketProducts) {

		return pricedBasketProducts.values().stream()
				.filter(product -> product.getStatus() == ProductStatus.Unavailable)
				.collect(Collectors.toMap(
						Product::getName,
						product -> basket.getQuantity(product.getName())));
	}

	/**
	 * From the customer's basket use the product catalog to price the products
	 *
	 * @param basket the customer's basket from the basket service
	 * @return a map of product names to product details, including price
	 */
	private Map<String, Product> priceBasketProducts(final Basket basket) {

		return basket.getProductNames().stream()
				.map(productName -> _productCatalogService.getProductByName(productName))
				.collect(Collectors.toMap(
						Product::getName,
						product -> product));
	}

	/**
	 * Apply discount to the customer's basket.
	 *
	 * This method uses a copy of the customers basket from which products
	 * are eliminated as discounts are applied.
	 *
	 * @param mutableBasket a copy of the customers basket from which products are eliminated as discounts are applied
	 * @param pricedBasketProducts a map of product names to product details, including price
	 * @return A sorted list of the discounts applied.
	 */
	private List<AppliedDiscount> getAppliedDiscounts(
			final MutableBasket mutableBasket,
			final Map<String, Product> pricedBasketProducts) {

		final List<PercentageDiscount> discounts = _discountService.getPercentageDiscounts();
		final Map<PercentageDiscount, Integer> discountMatches = mutableBasket.applyDiscountsExhaustively(discounts);

		return discountMatches.entrySet().stream()
				// Remove entries with a match count of 0
				.filter(discountMatch -> discountMatch.getValue() > 0)
				// Sort the discounts so they appear in a predictable order
				.sorted((d1, d2) -> {
						final String discountProductName1 = d1.getKey().getTargetProduct();
						final String discountProductName2 = d2.getKey().getTargetProduct();
						return discountProductName1.compareToIgnoreCase(discountProductName2);
					}
				)
				// Convert the discounts to the report model
				.map(discountMatch -> {
					final PercentageDiscount discount = discountMatch.getKey();
					final double originalPrice = pricedBasketProducts.get(discount.getTargetProduct()).getPrice();
					final double discountPerApplication = originalPrice * discount.getPercentage() / 100.0;
					return new AppliedDiscount(
							discount.getTargetProduct() + " " + discount.getPercentage() + "% off",
							discountMatch.getValue(),
							discountPerApplication);
				})
				.collect(Collectors.toList());
	}

	/**
	 * Calculate the total value of discounts applied to the basket
	 *
	 * @param appliedDiscounts the discounts applied
	 * @return the total value of discounts applied to the basket
	 */
	private double getTotalDiscount(final List<AppliedDiscount> appliedDiscounts) {
		return appliedDiscounts.stream()
				.mapToDouble(appliedDiscount -> appliedDiscount.getTotalDiscount())
				.sum();
	}

	@Override
	public Costing getCosting() {

		// Get an immutable copy of the current basket (we do not want the items in the basket
		// to change while this calculation is taking place).
		final Basket basket = _basketService.getCurrentBasket();

		// Get price and availability of the products in the basket
		final Map<String, Product> pricedBasketProducts = priceBasketProducts(basket);

		// Create this services model of the basket
		final MutableBasket mutableBasket = new MutableBasket(basket);

		// Calculate the subtotal
		final double subtotal = mutableBasket.calculateSubtotal(pricedBasketProducts);

		// Make a report on missing products
		final Map<String, Integer> unavilableProducts = getUnavilableProducts(basket, pricedBasketProducts);

		// Get the discounts that have been applied
		final List<AppliedDiscount> appliedDiscounts = getAppliedDiscounts(mutableBasket, pricedBasketProducts);

		// Get the total discount applied
		final double totalDiscount = getTotalDiscount(appliedDiscounts);

		// Build and return a model of the costing
		return new Costing(
				unavilableProducts,
				appliedDiscounts,
				subtotal,
				subtotal - totalDiscount);
	}
}
