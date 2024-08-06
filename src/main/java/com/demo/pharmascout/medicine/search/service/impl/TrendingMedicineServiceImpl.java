package com.demo.pharmascout.medicine.search.service.impl;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.MedicineRepository;
import com.demo.pharmascout.medicine.search.model.TrendingMedicine;
import com.demo.pharmascout.medicine.search.repository.TrendingMedicineRepository;
import com.demo.pharmascout.medicine.search.service.TrendingMedicineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrendingMedicineServiceImpl implements TrendingMedicineService {

	private final MedicineRepository medicineRepository;
	private final TrendingMedicineRepository trendingMedicineRepository;

	@Override
	public MedicineModel getTrendingMedicine ( LocalDate date, String name, int quantity ) {
		try {
			// search for the medicine in the trending medicines table
			trendingMedicineRepository.findTrendingMedicineByDateEqualsAndNameEqualsIgnoreCase(date, name)
					.ifPresentOrElse( // if the medicine is found, update the quantity
							trendingMedicine -> {
								trendingMedicine.setQuantity(trendingMedicine.getQuantity() + quantity);
								trendingMedicineRepository.save(trendingMedicine);
							},
							() -> { // if the medicine is not found, add the medicine
								TrendingMedicine trendingMedicine = buildTrendingMedicine(name, quantity, date);
								addTrendingMedicine(trendingMedicine);
							}
					);

			// search for the medicine in the medicine table
			Optional< MedicineModel > searchedMedicine = medicineRepository.findByGenericName(name);

			if ( searchedMedicine.isPresent() ) { // if the medicine is found, return the medicine
				log.info("Trending medicines fetched successfully: {}", searchedMedicine.get());
				return searchedMedicine.get();
			} else { // if the medicine is not found, return null
				log.error("Error while fetching trending medicines");
				return buildMedicineModelForNotFound(name);
			}

		} catch ( Exception e ) {
			log.error("Error while fetching trending medicines", e);
			throw new RuntimeException("Error while fetching trending medicines");
		}
	}

	@Override
	public void addTrendingMedicine ( TrendingMedicine trendingMedicine ) {
		try {
			trendingMedicineRepository.save(trendingMedicine); // add the trending medicine
		} catch ( Exception e ) {
			log.error("Error while adding trending medicine", e);
			throw new RuntimeException("Error while adding trending medicine");
		}
	}

	private TrendingMedicine buildTrendingMedicine ( String name, int quantity, LocalDate date ) {
		return TrendingMedicine.builder() // build the trending medicine and return it
				.name(name)
				.quantity(quantity)
				.date(date)
				.build();
	}

	private MedicineModel buildMedicineModelForNotFound ( String name ) {
		return MedicineModel.builder()
				.name("No medicine found")
				.manufacturer("nil")
				.genericName(name)
				.dosage("nil")
				.quantity(0)
				.price(0.0)
				.discount(0.0f)
				.expiryDates(null)
				.build();
	}

}
