package com.demo.pharmascout.medicine.expired.controller;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/expired-medicines")
public class ExpiredMedicineController {

	private final ExpiredMedicineService expiredMedicineService;

	public ExpiredMedicineController(ExpiredMedicineService expiredMedicineService) {
		this.expiredMedicineService = expiredMedicineService;
	}

	@GetMapping("/{date}")
	public ResponseEntity< List < MedicineModel > > handleGetAllExpiredMedicines(
			@PathVariable String date
	) {
		return ResponseEntity.ok(expiredMedicineService.getAllExpiredMedicines(date));
	}
}
