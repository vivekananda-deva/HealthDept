package com.healthbridge.service;

import java.util.List;

import com.healthbridge.dto.PatientCreateRequest;
import com.healthbridge.dto.PatientUpdateRequest;
import com.healthbridge.model.Patient;

public interface PatientService {

	Patient savePatient(PatientCreateRequest request);

	List<Patient> findByPatientNameContaining(String patientName);

	Patient findByPhone(String phone);

	List<Patient> findAllPatients();

	Patient findById(Integer id);

	Patient updatePatient(Integer id, PatientUpdateRequest request);

	void deletePatientById(Integer id);

}
