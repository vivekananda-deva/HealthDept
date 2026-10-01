package com.healthbridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthbridge.dto.PatientCreateRequest;
import com.healthbridge.dto.PatientUpdateRequest;
import com.healthbridge.model.Patient;
import com.healthbridge.service.PatientService;

@RestController
@RequestMapping("patient/api/v1")
public class PatientController {

	@Autowired
	PatientService patientService;

	@PostMapping("insertPa")
	public Patient savePatientInfo(@RequestBody @Valid PatientCreateRequest request) {
		return patientService.savePatient(request);
	}

	@GetMapping("getPatients")
	public List<Patient> getAll() {
		return patientService.findAllPatients();
	}

	@GetMapping("getPatientById/{id}")
	public Patient getById(@PathVariable Integer id) {
		return patientService.findById(id);
	}

	@GetMapping("getByName/{name}")
	public List<Patient> getByName(@PathVariable String name) {
		return patientService.findByPatientNameContaining(name);
	}

	@GetMapping("getByPhone/{phone}")
	public Patient getByPhone(@PathVariable String phone) {
		return patientService.findByPhone(phone);
	}

	@PutMapping("updateById/{id}")
	public Patient updatePatient(@PathVariable Integer id, @RequestBody @Valid PatientUpdateRequest request) {
		return patientService.updatePatient(id, request);
	}

	@DeleteMapping("deleteById/{id}")
	public void deleteById(@PathVariable Integer id) {
		patientService.deletePatientById(id);
	}
}
