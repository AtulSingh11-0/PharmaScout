package com.demo.pharmascout.medicine.sell.controller;

import com.demo.pharmascout.medicine.sell.service.SoldMedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cocat
 * @created 07-08-2024 - 10:37 PM
 * @package-name com.demo.pharmascout.medicine.sell.controller
 * @project PharmaScout
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/sold-medicine")
public class SoldMedicineController {

	private final SoldMedicineService service;

	@GetMapping("/")
	public ResponseEntity < ? > handleCalculateTotalPrice(
			@RequestParam String name,
			@RequestParam int quantity
	) {
		try {
			double result = service.calculateTotalPrice(name, quantity); // calculate the total price

			if ( result == 0.0 ) { // if the medicine is not found
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine not found"); // return a 404 response
			}

			return ResponseEntity.ok(result); // return the total price
		} catch ( Exception e ) {
			return ResponseEntity.badRequest().body("Error while calculating total price");
		}
	}

}
