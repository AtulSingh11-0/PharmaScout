package com.demo.pharmascout.medicine.entry.service;

import com.demo.pharmascout.medicine.entry.model.ExpiryDates;

import java.util.List;

public interface ExpiryDatesService {
	public void updateExpiryDates( ExpiryDates expiryDates );
	public void setMedicinesAsExpired ( List< ExpiryDates > expiryDates );
	public void updateExpiryDatesList(List<ExpiryDates> toDelete, List<ExpiryDates> toUpdate);
}
