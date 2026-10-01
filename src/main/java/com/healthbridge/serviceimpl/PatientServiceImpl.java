package com.healthbridge.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthbridge.dto.PatientCreateRequest;
import com.healthbridge.dto.PatientUpdateRequest;
import com.healthbridge.exception.ResourceNotFoundException;
import com.healthbridge.model.Patient;
import com.healthbridge.repo.PatientRepo;
import com.healthbridge.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientRepo patientRepo;

	@Override
	public Patient savePatient(PatientCreateRequest request) {
		Patient patient = new Patient();
		patient.setPatientName(request.patientName());
		patient.setPhone(request.phone());
		return patientRepo.save(patient);
	}

	@Override
	public List<Patient> findByPatientNameContaining(String patientName) {
		return patientRepo.findByPatientNameContaining(patientName);
	}

	@Override
	public Patient findByPhone(String phone) {
		Patient patient = patientRepo.findByPhone(phone);
		if (patient == null) {
			throw new ResourceNotFoundException("Patient not found with phone: " + phone);
		}
		return patient;
	}

	@Override
	public Patient findById(Integer id) {
		return patientRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
	}

	@Override
	public Patient updatePatient(Integer id, PatientUpdateRequest request) {
		Patient patient = findById(id);

		patient.setPatientName(request.patientName());
		patient.setPhone(request.phone());

		return patientRepo.save(patient);
	}

	@Override
	public void deletePatientById(Integer id) {
		patientRepo.delete(findById(id));
	}

	@Override
	public java.util.List<Patient> findAllPatients() {
		return patientRepo.findAll();
	}

}
