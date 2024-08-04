package com.demo.pharmascout.medicine.expired.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;

import java.util.List;

public interface ExpiredMedicineService {
	public ExpiredMedicineModel addExpiredMedicine( MedicineModel medicineModel, int expiredQuantity);
	public List < ExpiredMedicineModel > getAllExpiredMedicines ( String date);
}
