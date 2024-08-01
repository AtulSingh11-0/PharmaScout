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

	public MedicineModel addItem ( MedicineModel medicineModel ) {
		try {
			Optional< MedicineModel > optionalMedicineModel = repository.findByName(medicineModel.getName());

			if ( optionalMedicineModel.isPresent() ) {
				MedicineModel model = optionalMedicineModel.get();
				model.setQuantity(model.getQuantity() + medicineModel.getQuantity());
				updateMedicineMfgDates(model, medicineModel);
				updateMedicineExpDates(model, medicineModel);
				return repository.save(model);
			} else {
				return addMedicine(medicineModel);
			}

		} catch ( Exception e ) {
			log.error("Error while adding medicine: {}", e.getMessage());
			throw new RuntimeException("Error while adding medicine");
		}
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
	public List< MedicineModel > getAllMedicines () {
		try {
			return repository.findAll();
		} catch ( Exception e ) {
			throw new RuntimeException("Error while fetching medicines");
		}
	}

	private void updateMedicineMfgDates ( MedicineModel model, MedicineModel medicineModel ) {
		medicineModel.getMfgDates().forEach(( mfgDate, quantity ) -> {
			if ( model.getMfgDates().containsKey(mfgDate) ) {
				model.getMfgDates().put(mfgDate, model.getMfgDates().get(mfgDate) + quantity);
			} else {
				model.getMfgDates().put(mfgDate, quantity);
			}
		});
	}

	private void updateMedicineExpDates ( MedicineModel model, MedicineModel medicineModel ) {
		medicineModel.getExpDates().forEach(( expDate, quantity ) -> {
			if ( model.getExpDates().containsKey(expDate) ) {
				model.getExpDates().put(expDate, model.getExpDates().get(expDate) + quantity);
			} else {
				model.getExpDates().put(expDate, quantity);
			}
		});
	}
}