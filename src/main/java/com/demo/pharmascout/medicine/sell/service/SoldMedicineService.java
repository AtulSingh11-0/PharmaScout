package com.demo.pharmascout.medicine.sell.service;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;

/**
 * @author cocat
 * @created 07-08-2024 - 09:11 PM
 * @package-name com.demo.pharmascout.medicine.sell.service.impl
 * @project PharmaScout
 */

public interface SoldMedicineService {
	public void saveSoldMedicine( MedicineModel model, int quantity, double discountedPrice, double actualPrice );
	public double calculateTotalPrice(String name, int quantity);
}
