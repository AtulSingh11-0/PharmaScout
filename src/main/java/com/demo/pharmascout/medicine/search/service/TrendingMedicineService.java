package com.demo.pharmascout.medicine.search.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.search.model.TrendingMedicine;

import java.time.LocalDate;

public interface TrendingMedicineService {
	public MedicineModel getTrendingMedicine ( LocalDate date, String name, int quantity);
	public void addTrendingMedicine ( TrendingMedicine trendingMedicine );
}
