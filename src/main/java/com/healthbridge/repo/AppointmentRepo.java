package com.healthbridge.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthbridge.model.Appointment;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

	List<Appointment> findByDoctorDoctorId(Integer doctorId);

	List<Appointment> findByPatientPatientId(Integer patientId);

	List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

	List<Appointment> findByStatusContaining(String status);

}
