package com.demo.pharmascout.medicine.entry.repository;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository < MedicineModel, Long > {
//	Optional < MedicineModel > findById ( Long id );
	Optional < MedicineModel > findByName ( String name );
}
