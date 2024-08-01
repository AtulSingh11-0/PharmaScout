package com.demo.pharmascout.medicine.entry.service.impl;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.entry.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MedicineServiceImpl implements MedicineService {

	private final MedicineRepository repository;

	public MedicineServiceImpl ( MedicineRepository repository ) {
		this.repository = repository;
	}

	public void addItem ( MedicineModel medicineModel ) {
		repository.findByName(medicineModel.getName()).ifPresentOrElse(
				medicine -> updateMedicine(medicineModel, medicine.getId()),
				() -> addMedicine(medicineModel)
		);
	}

	@Override
	public MedicineModel addMedicine ( MedicineModel medicineModel ) {
		try {
			log.info("Adding medicine: {}", medicineModel);
			return repository.save(medicineModel);
		} catch ( Exception e ) {
			log.error("Error while adding medicine: {}", e.getMessage());
			throw new RuntimeException("Error while adding medicine");
		}
	}

	@Override
	public MedicineModel updateMedicine ( MedicineModel medicineModel, int id ) {
		try {
			MedicineModel model = repository.findById(id).orElseThrow(() -> new RuntimeException("Medicine not found"));
			model.setName(medicineModel.getName());
			model.setManufacturer(medicineModel.getManufacturer());
			model.setGenericName(medicineModel.getGenericName());
			model.setDosage(medicineModel.getDosage());
			model.setQuantity(medicineModel.getQuantity());
			model.setPrice(medicineModel.getPrice());
			model.setDiscount(medicineModel.getDiscount());
			model.setMfgDate(medicineModel.getMfgDate());
			model.setExpDate(medicineModel.getExpDate());
			return repository.save(model);
		} catch ( Exception e ) {
			throw new RuntimeException("Error while updating medicine");
		}
	}

	@Override
	public MedicineModel getMedicineById ( int id ) {
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
	public void deleteMedicineById ( int id ) {
		try {
			repository.deleteById(id);
		} catch ( Exception e ) {
			throw new RuntimeException("Error while deleting medicine");
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
}
