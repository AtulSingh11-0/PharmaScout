package com.demo.pharmascout.medicine.sell.repository;

import com.demo.pharmascout.medicine.sell.model.SoldMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author cocat
 * @created 07-08-2024 - 09:10 PM
 * @package-name com.demo.pharmascout.medicine.sell.repository
 * @project PharmaScout
 */

@Repository
public interface SoldMedicineRepository extends JpaRepository< SoldMedicine, Long > {
	public Optional < SoldMedicine > findByName (String name);
}
