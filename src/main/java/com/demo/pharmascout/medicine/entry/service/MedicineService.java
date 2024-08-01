package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

import java.util.List;

public interface MedicineService {
	public MedicineModel addItem(MedicineModel medicineModel);
	public MedicineModel addMedicine(MedicineModel medicineModel);
	public MedicineModel getMedicineById(int id);
	public List< MedicineModel > getAllMedicines();
}
