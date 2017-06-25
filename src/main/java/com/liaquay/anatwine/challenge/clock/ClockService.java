package com.liaquay.anatwine.challenge.clock;

import java.util.Date;

/**
 * An easily mockable clock service to ease testing.
 */
public interface ClockService {

	/**
	 * Get the current date and time
	 *
	 * @return the current date and time
	 */
	public Date now();
}
