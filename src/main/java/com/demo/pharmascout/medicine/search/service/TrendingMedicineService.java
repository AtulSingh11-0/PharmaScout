package com.demo.pharmascout.medicine.search.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.search.model.TrendingMedicine;

import java.time.LocalDate;
import java.util.List;

public interface TrendingMedicineService {
	public List< MedicineModel > getTrendingMedicine ( LocalDate date, String name, int quantity);
	public void addTrendingMedicine ( TrendingMedicine trendingMedicine );
}
