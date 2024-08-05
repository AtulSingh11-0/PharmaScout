package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

import java.util.List;

public interface MedicineService {
	public List< MedicineModel > getAllMedicines();
	public MedicineModel getMedicineById ( Long id );
	public MedicineModel addMedicine ( MedicineModel medicineModel );
	public void updateMedicineQuantity (MedicineModel medicineModel, int quantity );
	public MedicineModel updateMedicine ( MedicineModel medicineModel, MedicineModel medicineToUpdate );
}
