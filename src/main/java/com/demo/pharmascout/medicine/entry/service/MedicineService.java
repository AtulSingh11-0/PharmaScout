package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

import java.util.List;

public interface MedicineService {
	public MedicineModel getMedicineById ( Long id );
	public List< MedicineModel > getAllMedicines();
	public MedicineModel addMedicine ( MedicineModel medicineModel );
	public MedicineModel updateMedicine ( MedicineModel medicineModel, MedicineModel medicineToUpdate );
}
