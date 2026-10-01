package com.healthbridge.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthbridge.model.Patient;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer> {

	List<Patient> findByPatientNameContaining(String patientName);

	Patient findByPhone(String phone);

}
