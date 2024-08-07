package com.demo.pharmascout.medicine.entry.service.impl;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.entry.repository.ExpiryDatesRepository;
import com.demo.pharmascout.medicine.entry.service.ExpiryDatesService;
import com.demo.pharmascout.medicine.entry.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiryDatesServiceImpl implements ExpiryDatesService {

	private final MedicineService medicineService;
	private final ExpiryDatesRepository expiryDatesRepository;

	@Override
	@Transactional
	public void setMedicinesAsExpired ( List< ExpiryDates > expiryDates ) {
		try {
			for ( ExpiryDates expiryDate : expiryDates ) { // iterate through the expiry dates
				expiryDate.setExpired(true); // set the expiry date as expired
				updateExpiryDates(expiryDate); // update the expiry dates
				medicineService.updateMedicineQuantity(expiryDate.getMedicine(), expiryDate.getQuantity()); // update the medicine quantity
			}
		} catch ( Exception e ) {
			log.error("Error occurred while setting medicines as expired: {}", e.getMessage());
			throw new RuntimeException("Error occurred while setting medicines as expired");
		}
	}

	@Override
	public void updateExpiryDates ( ExpiryDates expiryDates ) {
		try {
			if ( expiryDates.getQuantity() == 0 || expiryDates.isExpired() ) {
				expiryDatesRepository.delete(expiryDates); // delete the expiry date
			}
			expiryDatesRepository.save(expiryDates); // save the expiry date
		} catch ( Exception e ) {
			log.error("Error occurred while updating expiry dates: {}", e.getMessage());
			throw new RuntimeException("Error occurred while updating expiry dates");
		}
	}
}
