package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;
import com.demo.pharmascout.medicine.expired.model.ExpiredMedicineModel;

import java.util.List;

public interface ExpiryDatesService {
	public void updateExpiryDates( ExpiryDates expiryDates );
	public void setMedicinesAsExpired ( List< ExpiryDates > expiryDates );
}
