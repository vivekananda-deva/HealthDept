package com.healthbridge.service;

import java.time.LocalDate;
import java.util.List;

import com.healthbridge.dto.AppointmentCreateRequest;
import com.healthbridge.dto.AppointmentUpdateRequest;
import com.healthbridge.model.Appointment;

public interface AppointmentService {

	List<Appointment> findAllAppointments();

	Appointment saveAppointment(AppointmentCreateRequest request);

	List<Appointment> findByDoctorDoctorId(Integer doctorId);

	List<Appointment> findByPatientPatientId(Integer patientId);

	List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

	List<Appointment> findByStatusContaining(String status);

	Appointment findById(Integer appointmentId);

	Appointment updateAppointment(Integer id, AppointmentUpdateRequest request);

	void deleteAppointmentById(Integer id);
}
