package com.healthbridge.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthbridge.dto.HospitalCreateRequest;
import com.healthbridge.dto.HospitalUpdateRequest;
import com.healthbridge.exception.ResourceNotFoundException;
import com.healthbridge.model.Hospital;
import com.healthbridge.repo.HospitalRepo;
import com.healthbridge.service.HospitalService;

@Service
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	HospitalRepo hospitalRepo;

	@Override
	public Hospital saveHospital(HospitalCreateRequest request) {
		Hospital hospital = new Hospital();
		hospital.setHospitalName(request.hospitalName());
		hospital.setLocation(request.location());
		return hospitalRepo.save(hospital);
	}

	@Override
	public Hospital getHospitalById(Integer hospitalId) {
		return hospitalRepo.findById(hospitalId)
				.orElseThrow(() -> new ResourceNotFoundException("Hospital not found with id: " + hospitalId));
	}

	@Override
	public List<Hospital> getHospitalByName(String hospitalName) {
		return hospitalRepo.findByHospitalNameContaining(hospitalName);
	}

	@Override
	public List<Hospital> getHospitalByLocation(String location) {
		return hospitalRepo.findByLocation(location);
	}

	@Override
	public List<Hospital> getHospitals() {
		return hospitalRepo.findAll();
	}

	@Override
	public Hospital updateHospital(Integer id, HospitalUpdateRequest request) {
		Hospital hospital = getHospitalById(id);

		hospital.setHospitalName(request.hospitalName());
		hospital.setLocation(request.location());

		return hospitalRepo.save(hospital);
	}

	@Override
	public void deleteHospitalById(Integer id) {
		hospitalRepo.delete(getHospitalById(id));
	}

}
