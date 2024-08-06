package com.demo.pharmascout.medicine.search.repository;

import com.demo.pharmascout.medicine.search.model.TrendingMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TrendingMedicineRepository extends JpaRepository< TrendingMedicine, Long > {
	public Optional< TrendingMedicine > findTrendingMedicineByDateEqualsAndNameEqualsIgnoreCase ( LocalDate date, String name );
}
