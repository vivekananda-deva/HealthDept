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

import com.healthbridge.dto.HospitalCreateRequest;
import com.healthbridge.dto.HospitalUpdateRequest;
import com.healthbridge.model.Hospital;
import com.healthbridge.service.HospitalService;

//	https://localhost:8080/hospital/api/v1
@RestController
@RequestMapping("hospital/api/v1")
public class HospitalController {

	@Autowired
	HospitalService hospitalService;

	@PostMapping("insertHs")
	Hospital saveHospitalInfo(@RequestBody @Valid HospitalCreateRequest request) {
		return hospitalService.saveHospital(request);
	}

	@GetMapping("getHospitals")
	List<Hospital> getHospitals() {
		return hospitalService.getHospitals();
	}

	@GetMapping("getHospitalById/{id}")
	Hospital getHospitalById(@PathVariable Integer id) {
		return hospitalService.getHospitalById(id);
	}

	@GetMapping("getHospitalByName/{name}")
	List<Hospital> getHospitalByname(@PathVariable String name) {
		return hospitalService.getHospitalByName(name);
	}

	@GetMapping("getHospitalByLocation/{location}")
	List<Hospital> getHospitalByLocation(@PathVariable String location) {
		return hospitalService.getHospitalByLocation(location);
	}

	@PutMapping("updateById/{id}")
	Hospital updateHospital(@PathVariable Integer id, @RequestBody @Valid HospitalUpdateRequest request) {
		return hospitalService.updateHospital(id, request);
	}

	@DeleteMapping("deleteById/{id}")
	void deleteById(@PathVariable Integer id) {
		hospitalService.deleteHospitalById(id);
	}
}
