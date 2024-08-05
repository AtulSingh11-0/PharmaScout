package com.demo.pharmascout.medicine.expired.service.impl;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.ExpiryDatesRepository;
import com.demo.pharmascout.medicine.entry.service.ExpiryDatesService;
import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;
import com.demo.pharmascout.medicine.expired.repository.ExpiredMedicineRepository;
import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiredMedicineServiceImpl implements ExpiredMedicineService {

	private final ExpiryDatesService expiryDatesService;
	private final ExpiryDatesRepository expiryDatesRepository;
	private final ExpiredMedicineRepository expiredMedicineRepository;

	@Override
	public ExpiredMedicineModel addExpiredMedicine ( MedicineModel medicineModel, int expiredQuantity ) {
		try {
			return expiredMedicineRepository.save(build(medicineModel, expiredQuantity)); // save and return the expired medicine
		} catch ( Exception e ) {
			log.error("Error occurred while adding expired medicine: {}", e.getMessage());
			throw new RuntimeException("Error occurred while adding expired medicine");
		}
	}

	@Override
	public ExpiredMedicineModel updateExpiredMedicine( ExpiredMedicineModel expiredMedicineModel, int expiredQuantity) {
		try {
			expiredMedicineModel.setQuantity(expiredMedicineModel.getQuantity() + expiredQuantity); // update the quantity
			return expiredMedicineRepository.save(expiredMedicineModel); // save and return the updated expired medicine
		} catch ( Exception e ) {
			log.error("Error occurred while updating expired medicine: {}", e.getMessage());
			throw new RuntimeException("Error occurred while updating expired medicine");
		}
	}

	@Override
	@Transactional
	public List< ExpiredMedicineModel > getAllExpiredMedicines ( String date ) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate expDate = LocalDate.parse(date, formatter);

			List< ExpiryDates > expiryDates = fetchExpiredMedicines(expDate);

			return generateExpiredMedicinesList(expiryDates); // generate and return the expired medicines list
		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private List < ExpiryDates > fetchExpiredMedicines ( LocalDate date ) {
		try {
			// fetch the expiry dates that are less than or equal to the given date from the expiry dates table
			List< ExpiryDates > expiryDates = expiryDatesRepository.findAllByExpiryDateIsLessThanEqual(date);

			if ( expiryDates.isEmpty() ) { // check if the expiry dates list is empty
				log.info("No expired medicines found");
				return new ArrayList <>(); // return an empty list
			}

			log.info("Fetched expired medicines: {}", expiryDates.size());
			return expiryDates; // return the expiry dates list

		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private ExpiredMedicineModel build ( MedicineModel medicineModel, int expiredQuantity ) {
		return ExpiredMedicineModel.builder() // build the expired medicine model
				.name(medicineModel.getName()) // set the name
				.manufacturer(medicineModel.getManufacturer()) // set the manufacturer
				.genericName(medicineModel.getGenericName()) // set the generic name
				.dosage(medicineModel.getDosage()) // set the dosage
				.quantity(expiredQuantity) // set the quantity
				.build(); // build the expired medicine model
	}

	private List< ExpiredMedicineModel > generateExpiredMedicinesList ( List< ExpiryDates > expiryDates ) {
		List< ExpiredMedicineModel > expiredMedicines = new ArrayList <>(); // create a new list to store the expired medicines

		for ( ExpiryDates expiryDate : expiryDates ) { // iterate through the expiry dates list

			MedicineModel medicineModel = expiryDate.getMedicine(); // get the medicine model through the reference of the expiry date
			log.info("Medicine model: {}", medicineModel);

			expiredMedicineRepository.findByName(medicineModel.getName()) // find the expired medicine by name
					.ifPresentOrElse( // if the expired medicine is present then perform the following
							expiredMedicineModel -> {
								log.info("Updated Expired medicine model: {}, with quantity: {}", expiredMedicineModel, expiryDate.getQuantity());
								// update the expired medicine model with the quantity
								ExpiredMedicineModel updatedExpiredMedicineModel = updateExpiredMedicine(expiredMedicineModel, expiryDate.getQuantity());
								expiredMedicines.add(updatedExpiredMedicineModel); // add the updated expired medicine model to the list
							}, () -> { // if the expired medicine is not present then perform the following
								// save the expired medicine to the table
								ExpiredMedicineModel expiredMedicineModel = addExpiredMedicine(medicineModel, expiryDate.getQuantity());
								log.info("Expired medicine model: {}, with quantity: {}", expiredMedicineModel, expiryDate.getQuantity());
								expiredMedicines.add(expiredMedicineModel); // add the expired medicine model to the list
							}
					);
		}

		expiryDatesService.setMedicinesAsExpired(expiryDates); // set the medicines as expired and update the medicine quantity

		return expiredMedicines; // return the expired medicines list
	}

}

