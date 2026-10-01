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

import com.healthbridge.dto.DoctorCreateRequest;
import com.healthbridge.dto.DoctorUpdateRequest;
import com.healthbridge.model.Doctor;
import com.healthbridge.service.DoctorService;

@RestController
@RequestMapping("doctor/api/v1")
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@PostMapping("insertDoc")
	public Doctor saveDoctorInfo(@RequestBody @Valid DoctorCreateRequest request) {
		return doctorService.saveDoctor(request);
	}

	@GetMapping("/getDoctors")
	public List<Doctor> getAllDoctors() {
		return doctorService.getDoctorList();
	}

	@GetMapping("/getDoctorById/{id}")
	public Doctor getDoctorById(@PathVariable Integer id) {
		return doctorService.findById(id);
	}

	@GetMapping("/getDoctorsByHId/{HId}")
	public List<Doctor> getDoctorByHId(@PathVariable("HId") Integer Hid) {
		return doctorService.findByHospitalHospitalId(Hid);
	}

	@GetMapping("/getDoctorBySpecialization/{specialization}")
	public List<Doctor> getDoctorBySpecialization(@PathVariable("specialization") String specialization) {
		return doctorService.findBySpecialization(specialization);
	}

	@GetMapping("/getDoctorByName/{name}")
	public List<Doctor> getDoctorByName(@PathVariable String name) {
		return doctorService.findByDoctorNameContaining(name);
	}

	@PutMapping("updateById/{id}")
	public Doctor updateDoctor(@PathVariable Integer id, @RequestBody @Valid DoctorUpdateRequest request) {
		return doctorService.updateDoctor(id, request);
	}

	@DeleteMapping("deleteById/{id}")
	public void deleteById(@PathVariable Integer id) {
		doctorService.deleteDoctorById(id);
	}

}
