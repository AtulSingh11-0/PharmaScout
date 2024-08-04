package com.demo.pharmascout.medicine.expired.service.impl;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.ExpiryDatesRepository;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;
import com.demo.pharmascout.medicine.expired.repository.ExpiredMedicineRepository;
import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpiredMedicineServiceImpl implements ExpiredMedicineService {
	
	private final MedicineRepository medicineRepository;
	private final ExpiryDatesRepository expiryDatesRepository;
	private final ExpiredMedicineRepository expiredMedicineRepository;

	@Override
	public ExpiredMedicineModel addExpiredMedicine ( MedicineModel medicineModel, int expiredQuantity ) {
		try {
			return new ExpiredMedicineModel();
		} catch ( Exception e ) {
			log.error("Error occurred while adding expired medicine: {}", e.getMessage());
			throw new RuntimeException("Error occurred while adding expired medicine");
		}
	}

	@Override
	public List< ExpiredMedicineModel > getAllExpiredMedicines ( String date ) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate expiryDate = LocalDate.parse(date, formatter);
			List< MedicineModel > medicineModelsExpired = fetchExpiredMedicines(expiryDate);
			List < ExpiredMedicineModel > expiredMedicines = new ArrayList <>();
			for ( MedicineModel expiredMedicine : medicineModelsExpired ) {
				Optional< ExpiredMedicineModel > expiredMedicineInDB = expiredMedicineRepository.findByName(expiredMedicine.getName());

				if ( expiredMedicineInDB.isPresent() ) {
					expiredMedicineInDB.get().setQuantity(expiredMedicineInDB.get().getQuantity() + expiredMedicine.getQuantity());

					expiredMedicine.getExpiryDates().stream().forEach(expDate -> { // Remove the expired date from the expiry dates table
						if ( expDate.getExpiryDate().isEqual(expiryDate) || expDate.getExpiryDate().isBefore(expiryDate) ) {  // check if the expiry date is equal to the current date or before the current date
							int expiredMedsQuantity = expDate.getQuantity(); // get the quantity of expired medicines
							MedicineModel medicine = expDate.getMedicine(); // get the medicine
							medicine.setQuantity(expDate.getMedicine().getQuantity() - expiredMedsQuantity); // reduce the quantity of the medicine
							expDate.getMedicine().getExpiryDates().remove(expDate); // remove the expired medicine from the expiry dates
							medicineRepository.save(medicine); // save the medicineay
							expiryDatesRepository.delete(expDate);
						}
					});
					expiredMedicines.add(expiredMedicineRepository.save(expiredMedicineInDB.get()));
				} else {
				expiredMedicines.add(expiredMedicineRepository.save(build(expiredMedicine)));
				}
			}
			return expiredMedicines;
		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private List < MedicineModel > fetchExpiredMedicines ( LocalDate date ) {
		try {

			List< ExpiryDates > expiryDates = expiryDatesRepository.findAllByExpiryDateIsLessThanEqual(date);
			log.info("Fetched expiry dates: {}", expiryDates.size());
			List < MedicineModel > expiredMedicines = new ArrayList <>();

			if ( expiryDates.isEmpty() ) {
				log.info("No expired medicines found");
				return expiredMedicines;
			}

			for ( ExpiryDates expiryDate : expiryDates ) {
				Optional < MedicineModel > medicine = medicineRepository.findById(expiryDate.getMedicine().getId());
				medicine.ifPresent(expiredMedicines::add);
			}
			log.info("Fetched expired medicines: {}", expiredMedicines.size());
			return expiredMedicines;

		} catch ( Exception e ) {
			log.error("Error occurred while fetching expired medicines: {}", e.getMessage());
			throw new RuntimeException("Error occurred while fetching expired medicines");
		}
	}

	private ExpiredMedicineModel build ( MedicineModel medicineModel ) {
		return ExpiredMedicineModel.builder()
				.name(medicineModel.getName())
				.manufacturer(medicineModel.getManufacturer())
				.genericName(medicineModel.getGenericName())
				.dosage(medicineModel.getDosage())
				.quantity(medicineModel.getQuantity())
				.build();
	}
}
