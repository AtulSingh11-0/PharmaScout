package com.demo.pharmascout.medicine.entry.service.impl;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.entry.service.MedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
	private final MedicineRepository repository;

	public MedicineModel updateMedicine ( MedicineModel medicineModel, MedicineModel medicineToUpdate ) {
		try {

			log.info("Updating medicine: {}", medicineModel); // log the medicineModel
			medicineToUpdate.setQuantity(medicineToUpdate.getQuantity() + medicineModel.getQuantity()); // update the quantity

			Map < LocalDate, ExpiryDates > expiryDatesMap = new HashMap<>(); // create a map for expiry dates

			for ( ExpiryDates expiryDate : medicineToUpdate.getExpiryDates() ) { // iterate through the expiry dates
				expiryDatesMap.put(expiryDate.getExpiryDate(), expiryDate); // put the expiry date in the map
			}

			for ( ExpiryDates newExpiryDate : medicineModel.getExpiryDates() ) {
				ExpiryDates existingExpiryDate = expiryDatesMap.get(newExpiryDate.getExpiryDate());

				if ( existingExpiryDate != null ) {
					existingExpiryDate.setQuantity(existingExpiryDate.getQuantity() + newExpiryDate.getQuantity()); // update the quantity
				} else {
					newExpiryDate.setMedicine(medicineToUpdate); // set the medicine in the expiry date
					medicineToUpdate.getExpiryDates().add(newExpiryDate); // add the expiry date to the medicine
				}
			}

			return repository.save(medicineToUpdate); // save and return the updated medicine

		} catch ( Exception e ) {
			log.error("Error while adding medicine: {}", e.getMessage());
			throw new RuntimeException("Error while adding medicine");
		}
	}

	@Override
	public MedicineModel addMedicine ( MedicineModel medicineModel ) {
		try {

			Optional< MedicineModel > medicineByName = repository.findByName(medicineModel.getName()); // find the medicine by name

			if ( medicineByName.isEmpty() ) { // check if the medicine is not present
				setMedicineExpiryDates(medicineModel); // set the medicine in the expiry dates
				log.info("Adding medicine: {}", medicineModel); // log the medicineModel
				return repository.save(medicineModel); // save and return the medicine
			} else { // if the medicine is present
				return updateMedicine(medicineModel, medicineByName.get()); // update and return the medicine
			}
		} catch ( Exception e ) {
			log.error("Error while adding medicine: {}", e.getMessage());
			throw new RuntimeException("Error while adding medicine");
		}
	}

	@Override
	public void updateMedicineQuantity ( MedicineModel medicineModel, int quantity ) {
		try {
			if ( medicineModel.getQuantity() == quantity ) {
				log.info("Deleting medicine: {}", medicineModel); // log the medicineModel
				repository.delete(medicineModel); // delete the medicine
			} else {
				log.info("Updating medicine quantity: {}", medicineModel); // log the medicineModel
				medicineModel.setQuantity(medicineModel.getQuantity() - quantity); // update the quantity
				if ( medicineModel.getQuantity() < 0 ) {
					repository.delete(medicineModel); // delete the medicine if the quantity is less than 0
				}
				repository.save(medicineModel); // save the medicine
			}
		} catch ( Exception e ) {
			log.error("Error while updating medicine quantity: {}", e.getMessage());
			throw new RuntimeException("Error while updating medicine quantity");
		}
	}

	@Override
	public MedicineModel getMedicineById ( Long id ) {
		try {
			Optional< MedicineModel > medicineModel = repository.findById(id);

			if ( medicineModel.isPresent() ) {
				return medicineModel.get();
			} else {
				throw new RuntimeException("Medicine not found");
			}

		} catch ( Exception e ) {
			throw new RuntimeException("Error while fetching medicine");
		}
	}

	@Override
	public List< MedicineModel > getAllMedicines () {
		try {
			return repository.findAll();
		} catch ( Exception e ) {
			throw new RuntimeException("Error while fetching medicines");
		}
	}

	private void setMedicineExpiryDates(MedicineModel medicineModel) {
		for (ExpiryDates expiryDate : medicineModel.getExpiryDates()) {
			expiryDate.setMedicine(medicineModel);
		}
	}
}