package com.demo.pharmascout.medicine.expired.service.impl;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;
import com.demo.pharmascout.medicine.expired.repository.ExpiredMedicineRepository;
import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExpiredMedicineServiceImpl implements ExpiredMedicineService {

	private final ExpiredMedicineRepository expiredMedicineRepository;
	private final MedicineRepository medicineRepository;

	public ExpiredMedicineServiceImpl(
			ExpiredMedicineRepository expiredMedicineRepository,
			MedicineRepository medicineRepository
	) {
		this.expiredMedicineRepository = expiredMedicineRepository;
		this.medicineRepository = medicineRepository;
	}

	@Override
	public ExpiredMedicineModel addExpiredMedicine(MedicineModel medicineModel, int expiredQuantity) {
		try {
			Optional<ExpiredMedicineModel> expiredMedicineByName = expiredMedicineRepository.findByName(medicineModel.getName());

			if (expiredMedicineByName.isPresent()) {
				ExpiredMedicineModel expiredMedicineModel = expiredMedicineByName.get();
				expiredMedicineModel.setQuantity(expiredMedicineModel.getQuantity() + expiredQuantity);
				return expiredMedicineRepository.save(expiredMedicineModel);
			} else {
				ExpiredMedicineModel builder = ExpiredMedicineModel.builder()
						.name(medicineModel.getName())
						.manufacturer(medicineModel.getManufacturer())
						.genericName(medicineModel.getGenericName())
						.dosage(medicineModel.getDosage())
						.quantity(expiredQuantity)
						.build();

				return expiredMedicineRepository.save(builder);
			}
		} catch (Exception e) {
			log.error("Error occurred while adding expired medicine: ", e);
			throw new RuntimeException("Error occurred while adding expired medicine: ", e);
		}
	}

	@Override
	public List<ExpiredMedicineModel> getAllExpiredMedicines(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate ld = LocalDate.from(formatter.parse(date));

			List<MedicineModel> expiredMedicines = getExpiredMedicines(ld);
			List<ExpiredMedicineModel> expiredMedicineModels = new ArrayList<>();
			List<MedicineModel> medicinesToUpdate = new ArrayList<>();

			for (MedicineModel medicineModel : expiredMedicines) {
				int totalExpiredQuantity = 0;
				for (LocalDate expDate : new ArrayList<>(medicineModel.getExpDates().keySet())) {
					int quantity = medicineModel.getExpDates().get(expDate);
					if (expDate.isEqual(ld) || expDate.isBefore(ld)) {
						totalExpiredQuantity += quantity;
						medicineModel.getExpDates().remove(expDate);
					}
				}
				if (totalExpiredQuantity > 0) {
					expiredMedicineModels.add(addExpiredMedicine(medicineModel, totalExpiredQuantity));
					if (totalExpiredQuantity == medicineModel.getQuantity()) {
						deleteExpiredMedicine(medicineModel.getId());
					} else {
						medicineModel.setQuantity(medicineModel.getQuantity() - totalExpiredQuantity);
						medicinesToUpdate.add(medicineModel);
					}
				}
			}

			medicineRepository.saveAll(medicinesToUpdate);

			return expiredMedicineModels;
		} catch (Exception e) {
			log.error("Error occurred while fetching all expired medicines: ", e);
			throw new RuntimeException("Error occurred while fetching all expired medicines: ", e);
		}
	}

	private List<MedicineModel> getExpiredMedicines(LocalDate date) {
		try {
			return medicineRepository.findMedicinesExpiredBefore(date);
		} catch (Exception e) {
			log.error("Error occurred while fetching expired medicines: ", e);
			throw new RuntimeException("Error occurred while fetching expired medicines: ", e);
		}
	}

	private void deleteExpiredMedicine(int id) {
		try {
			medicineRepository.deleteById(id);
			log.info("Deleted expired medicine with id: " + id);
		} catch (Exception e) {
			log.error("Error occurred while deleting expired medicine: ", e);
			throw new RuntimeException("Error occurred while deleting expired medicine: ", e);
		}
	}
}
