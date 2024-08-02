package com.demo.pharmascout.medicine.entry.repository;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

import java.time.LocalDate;
import java.util.List;

public interface CustomMedicineRepository {
	List< MedicineModel > findMedicinesExpiredBefore( LocalDate date);
}
