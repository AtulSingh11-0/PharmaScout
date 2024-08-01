package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

import java.util.List;

public interface MedicineService {
	public MedicineModel addMedicine(MedicineModel medicineModel);
	public MedicineModel updateMedicine(MedicineModel medicineModel, int id);
	public MedicineModel getMedicineById(int id);
	public void deleteMedicineById(int id);
	public List< MedicineModel > getAllMedicines();
}
