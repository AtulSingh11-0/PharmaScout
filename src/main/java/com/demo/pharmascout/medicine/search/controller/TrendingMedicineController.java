package com.demo.pharmascout.medicine.search.controller;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.search.service.TrendingMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping ( "/api/v1/trending-medicine" )
@RequiredArgsConstructor
public class TrendingMedicineController {

	private final TrendingMedicineService service;

	@GetMapping ( "/" )
	public ResponseEntity< ? > handleSearchMedicine (
			@RequestParam ( value = "name", required = true ) String name,
			@RequestParam ( value = "quantity", required = true ) int quantity
	) {
		try {
			MedicineModel trendingMedicine = service.getTrendingMedicine(LocalDate.now(), name, quantity);
			if ( trendingMedicine == null ) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicine found");
			} else {
				return ResponseEntity.ok(trendingMedicine);
			}
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
