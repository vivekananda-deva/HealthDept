package com.healthbridge.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthbridge.dto.DoctorCreateRequest;
import com.healthbridge.dto.DoctorUpdateRequest;
import com.healthbridge.exception.ResourceNotFoundException;
import com.healthbridge.model.Doctor;
import com.healthbridge.model.Hospital;
import com.healthbridge.repo.DoctorRepo;
import com.healthbridge.repo.HospitalRepo;
import com.healthbridge.service.DoctorService;

@Service
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	DoctorRepo doctorRepo;

	@Autowired
	HospitalRepo hospitalRepo;

	@Override
	public Doctor saveDoctor(DoctorCreateRequest request) {
		Doctor doctor = new Doctor();
		doctor.setDoctorName(request.doctorName());
		doctor.setSpecialization(request.specialization());

		if (request.hospitalId() != null) {
			Hospital hospital = hospitalRepo.findById(request.hospitalId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Hospital not found with id: " + request.hospitalId()));
			doctor.setHospital(hospital);
		}

		return doctorRepo.save(doctor);
	}

	@Override
	public List<Doctor> getDoctorList() {
		return doctorRepo.findAll();
	}

	@Override
	public List<Doctor> findByHospitalHospitalId(Integer hospitalId) {
		return doctorRepo.findByHospitalHospitalId(hospitalId);
	}

	@Override
	public List<Doctor> findBySpecialization(String specialization) {
		return doctorRepo.findBySpecialization(specialization);
	}

	@Override
	public List<Doctor> findByDoctorNameContaining(String doctorName) {
		return doctorRepo.findByDoctorNameContaining(doctorName);
	}

	@Override
	public Doctor findById(Integer id) {
		return doctorRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
	}

	@Override
	public Doctor updateDoctor(Integer id, DoctorUpdateRequest request) {
		Doctor doctor = findById(id);

		doctor.setDoctorName(request.doctorName());
		doctor.setSpecialization(request.specialization());

		if (request.hospitalId() != null) {
			Hospital hospital = hospitalRepo.findById(request.hospitalId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Hospital not found with id: " + request.hospitalId()));
			doctor.setHospital(hospital);
		}

		return doctorRepo.save(doctor);
	}

	@Override
	public void deleteDoctorById(Integer id) {
		doctorRepo.delete(findById(id));
	}

}
