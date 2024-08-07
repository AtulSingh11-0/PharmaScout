package com.demo.pharmascout.medicine.search.controller;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.search.service.TrendingMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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
			List< MedicineModel > trendingMedicine = service.getTrendingMedicine(LocalDate.now(), name, quantity);
			return ResponseEntity.ok(trendingMedicine);
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
