package com.demo.pharmascout.medicine.sell.service.impl;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.entry.service.ExpiryDatesService;
import com.demo.pharmascout.medicine.entry.service.MedicineService;
import com.demo.pharmascout.medicine.sell.model.SoldMedicine;
import com.demo.pharmascout.medicine.sell.repository.SoldMedicineRepository;
import com.demo.pharmascout.medicine.sell.service.SoldMedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.SortedSet;

/**
 * @author cocat
 * @created 07-08-2024 - 09:11 PM
 * @package-name com.demo.pharmascout.medicine.sell.service.impl
 * @project PharmaScout
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SoldMedicineServiceImpl implements SoldMedicineService {

	private final MedicineService medicineService;
	private final ExpiryDatesService expiryDatesService;
	private final MedicineRepository medicineRepository;
	private final SoldMedicineRepository soldMedicineRepository;

	@Override
	public void saveSoldMedicine (MedicineModel model, int quantity, double discountedPrice, double actualPrice) {
		try {
			SoldMedicine soldMedicine = SoldMedicine.builder() // create a new SoldMedicine object
					.name( model.getName() )
					.manufacturer( model.getManufacturer() )
					.genericName( model.getGenericName() )
					.dosage( model.getDosage() )
					.quantity( quantity )
					.discountedPrice( discountedPrice )
					.actualPrice( actualPrice )
					.discount( model.getDiscount() )
					.build();

			soldMedicineRepository.save( soldMedicine ); // save the sold medicine
		} catch ( Exception e ) {
			log.error( "Error while saving sold medicine", e );
		}
	}

	@Override
	@Transactional
	public double calculateTotalPrice (String name, int quantity) {
		try {
			Optional< MedicineModel > model = medicineRepository.findByName(name); // find the medicine in the medicine repository
			if ( model.isEmpty() ) { // if the medicine is not found
				log.error("Medicine not found");
				return 0.0;
			}
			MedicineModel medicine = model.get(); // get the medicine

			log.info( "Medicine found: {}", model);
			double unDiscountedPrice; // calculate the un-discounted price
			double discountedPrice; // calculate the discounted price

			if ( quantity > medicine.getQuantity() ) { // if the quantity is greater than the available quantity
				quantity = medicine.getQuantity(); // set the quantity to the available quantity
			}

			unDiscountedPrice = medicine.getPrice() * quantity; // calculate the price for the given quantity
			discountedPrice = unDiscountedPrice - (unDiscountedPrice * medicine.getDiscount() / 100); // calculate the discounted price
			checkSoldMedicineInDB(medicine, quantity, discountedPrice, unDiscountedPrice); // save the sold medicine

			updateExpiryDatesQuantity(medicine.getExpiryDates(), quantity); // update the expiry dates
			medicineService.updateMedicineQuantity( medicine, quantity ); // update the quantity of the medicine

			return discountedPrice; // return the discounted price
		} catch ( Exception e ) {
			log.error( "Error while calculating total price", e );
			throw new RuntimeException( "Error while calculating total price" );
		}
	}

	private void checkSoldMedicineInDB (
			MedicineModel medicineModel,
			int quantity,
			double discountedPrice,
			double actualPrice) {
		try {
			soldMedicineRepository.findByName(medicineModel.getName()) // find the sold medicine by name
					.ifPresentOrElse( // if the sold medicine is present
							medicine -> {
								log.info( "Sold-Medicine found: {}", medicine);
								medicine.setQuantity(medicine.getQuantity() + quantity); // update the quantity
								medicine.setDiscountedPrice(medicine.getDiscountedPrice() + discountedPrice); // update the discounted price
								medicine.setActualPrice(medicine.getActualPrice() + actualPrice); // update the actual price
								soldMedicineRepository.save(medicine); // save the sold medicine
							}, () -> saveSoldMedicine(medicineModel, quantity, discountedPrice, actualPrice) // save the sold medicine
					);
		} catch ( Exception e ) {
			log.error( "Error while finding medicine", e );
		}
	}

	/*
	* TODO: Update the expiry dates quantity
	*  1. for expdate with quantity 65 and my quantity 120, its throwing error, solve this
	* */
	private void updateExpiryDatesQuantity ( SortedSet< ExpiryDates > expiryDates, int quantity ) {
		try {
			for ( ExpiryDates expiryDate : expiryDates ) { // iterate through the expiry dates
				if ( expiryDate.getQuantity() >= quantity ) {
					expiryDate.setQuantity( expiryDate.getQuantity() - quantity ); // update the quantity
					break;
				} else {
					quantity -= expiryDate.getQuantity();
					expiryDate.setQuantity( 0 );
				}
				expiryDatesService.updateExpiryDates( expiryDate ); // update the expiry date
			}
		} catch ( Exception e ) {
			log.error( "Error occurred while updating expiry dates: {}", e.getMessage() );
			throw new RuntimeException( "Error occurred while updating expiry dates" );
		}
	}

}
