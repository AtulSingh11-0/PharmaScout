package com.demo.pharmascout.medicine.entry.repository;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpiryDatesRepository extends JpaRepository < ExpiryDates, LocalDate > {
	List< ExpiryDates > findAllByExpiryDateIsLessThanEqual ( LocalDate date );
}
