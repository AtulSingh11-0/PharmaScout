package com.demo.pharmascout.medicine.expired.controller;

import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;
import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ( "api/v1/expired-medicines" )
@RequiredArgsConstructor
public class ExpiredMedicineController {

	private final ExpiredMedicineService expiredMedicineService;

	@GetMapping ( "/{date}" )
	public ResponseEntity< ? > handleGetAllExpiredMedicines (
			@PathVariable String date
	) {
		try {
			return ResponseEntity.ok(expiredMedicineService.getAllExpiredMedicines(date));
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
