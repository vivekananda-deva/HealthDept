package com.healthbridge.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthbridge.model.Hospital;

@Repository
public interface HospitalRepo extends JpaRepository<Hospital, Integer> {
	
	List<Hospital> findByHospitalNameContaining(String hospitalName);
	
	List<Hospital> findByLocation(String location);

	
}
