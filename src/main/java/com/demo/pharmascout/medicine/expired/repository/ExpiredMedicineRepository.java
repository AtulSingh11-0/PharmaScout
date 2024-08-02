package com.demo.pharmascout.medicine.expired.repository;

import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpiredMedicineRepository extends JpaRepository< ExpiredMedicineModel, Integer> {
	Optional < ExpiredMedicineModel > findByName ( String name );
}
