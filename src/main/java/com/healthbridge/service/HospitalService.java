package com.healthbridge.service;

import java.util.List;

import com.healthbridge.dto.HospitalCreateRequest;
import com.healthbridge.dto.HospitalUpdateRequest;
import com.healthbridge.model.Hospital;

public interface HospitalService {

	List<Hospital> getHospitals();

	Hospital saveHospital(HospitalCreateRequest request);

	Hospital getHospitalById(Integer hospitalId);

	List<Hospital> getHospitalByName(String hospitalName);

	List<Hospital> getHospitalByLocation(String location);

	Hospital updateHospital(Integer id, HospitalUpdateRequest request);

	void deleteHospitalById(Integer id);

}
