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

	private final ExpiryDatesRepository expiryDatesRepository;
	private final ExpiryDatesService expiryDatesService;
	private final ExpiredMedicineRepository expiredMedicineRepository;

	@Override
	public ExpiredMedicineModel addExpiredMedicine ( MedicineModel medicineModel, int expiredQuantity ) {
		try {
			return expiredMedicineRepository.save(build(medicineModel, expiredQuantity));
		} catch ( Exception e ) {
			log.error("Error occurred while adding expired medicine: {}", e.getMessage());
			throw new RuntimeException("Error occurred while adding expired medicine");
		}
	}

	@Override
	public ExpiredMedicineModel updateExpiredMedicine( ExpiredMedicineModel expiredMedicineModel, int expiredQuantity) {
		try {
			expiredMedicineModel.setQuantity(expiredMedicineModel.getQuantity() + expiredQuantity);
			return expiredMedicineRepository.save(expiredMedicineModel);
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

			return generateExpiredMedicinesList(expiryDates);
		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private List < ExpiryDates > fetchExpiredMedicines ( LocalDate date ) {
		try {

			List< ExpiryDates > expiryDates = expiryDatesRepository.findAllByExpiryDateIsLessThanEqual(date);

			if ( expiryDates.isEmpty() ) {
				log.info("No expired medicines found");
				return new ArrayList <>();
			}

			log.info("Fetched expired medicines: {}", expiryDates.size());
			return expiryDates;

		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private ExpiredMedicineModel build ( MedicineModel medicineModel, int expiredQuantity ) {
		return ExpiredMedicineModel.builder()
				.name(medicineModel.getName())
				.manufacturer(medicineModel.getManufacturer())
				.genericName(medicineModel.getGenericName())
				.dosage(medicineModel.getDosage())
				.quantity(expiredQuantity)
				.build();
	}

	private List< ExpiredMedicineModel > generateExpiredMedicinesList ( List< ExpiryDates > expiryDates ) {
		List< ExpiredMedicineModel > expiredMedicines = new ArrayList <>();

		for ( ExpiryDates expiryDate : expiryDates ) {

			MedicineModel medicineModel = expiryDate.getMedicine();
			log.info("Medicine model: {}", medicineModel);

			expiredMedicineRepository.findByName(medicineModel.getName())
					.ifPresentOrElse(
							expiredMedicineModel -> {
								log.info("Updated Expired medicine model: {}, with quantity: {}", expiredMedicineModel,
										expiryDate.getQuantity());
								ExpiredMedicineModel updatedExpiredMedicineModel = updateExpiredMedicine(expiredMedicineModel, expiryDate.getQuantity());
								expiredMedicines.add(updatedExpiredMedicineModel);
							}, () -> {
								ExpiredMedicineModel expiredMedicineModel = addExpiredMedicine(medicineModel, expiryDate.getQuantity());
								log.info("Expired medicine model: {}, with quantity: {}", expiredMedicineModel, expiryDate.getQuantity());
								expiredMedicines.add(expiredMedicineModel);
							}
					);
		}

		expiryDatesService.setMedicinesAsExpired(expiryDates);

		return expiredMedicines;
	}

}

