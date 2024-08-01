package com.demo.pharmascout.medicine.entry.controller;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.service.MedicineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicine")
public class MedicineController {

	private final MedicineService medicineService;

	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}

	@PostMapping("/")
	public ResponseEntity<?> handleAddMedicine (
			@RequestBody MedicineModel medicineModel
	) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(medicineService.addItem(medicineModel));
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> handleGetMedicineById (
			@PathVariable int id
	) {
		try {
			return ResponseEntity.ok(medicineService.getMedicineById(id));
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> handleGetAllMedicines () {
		try {
			return ResponseEntity.ok(medicineService.getAllMedicines());
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
