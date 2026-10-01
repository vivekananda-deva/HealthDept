package com.healthbridge.service;

import java.util.List;

import com.healthbridge.dto.DoctorCreateRequest;
import com.healthbridge.dto.DoctorUpdateRequest;
import com.healthbridge.model.Doctor;

public interface DoctorService {

	Doctor saveDoctor(DoctorCreateRequest request);

	List<Doctor> getDoctorList();

	List<Doctor> findByHospitalHospitalId(Integer hospitalId);

	List<Doctor> findBySpecialization(String specialization);

	List<Doctor> findByDoctorNameContaining(String doctorName);

	Doctor findById(Integer id);

	Doctor updateDoctor(Integer id, DoctorUpdateRequest request);

	void deleteDoctorById(Integer id);

}
