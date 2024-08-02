package com.demo.pharmascout.medicine.entry.repository.impl;

import com.demo.pharmascout.medicine.entry.model.MedicineModel;
import com.demo.pharmascout.medicine.entry.repository.CustomMedicineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.util.List;

public class CustomMedicineRepositoryImpl implements CustomMedicineRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List< MedicineModel > findMedicinesExpiredBefore ( LocalDate date ) {
		String queryString = "SELECT m FROM MedicineModel m JOIN m.expDates e WHERE KEY(e) <= :date";
		Query query = entityManager.createQuery(queryString, MedicineModel.class);
		query.setParameter("date", date);
		return query.getResultList();
	}
}
