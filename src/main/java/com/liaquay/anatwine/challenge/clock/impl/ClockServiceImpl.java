package com.liaquay.anatwine.challenge.clock.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.liaquay.anatwine.challenge.clock.ClockService;

@Component
public class ClockServiceImpl implements ClockService {

	@Override
	public Date now() {
		return new Date();
	}
}
