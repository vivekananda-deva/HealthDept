package com.healthbridge.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthbridge.dto.AppointmentCreateRequest;
import com.healthbridge.dto.AppointmentUpdateRequest;
import com.healthbridge.exception.ResourceNotFoundException;
import com.healthbridge.model.Appointment;
import com.healthbridge.model.Doctor;
import com.healthbridge.model.Patient;
import com.healthbridge.repo.AppointmentRepo;
import com.healthbridge.repo.DoctorRepo;
import com.healthbridge.repo.PatientRepo;
import com.healthbridge.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	AppointmentRepo appointmentRepo;

	@Autowired
	DoctorRepo doctorRepo;

	@Autowired
	PatientRepo patientRepo;

	@Override
	public Appointment saveAppointment(AppointmentCreateRequest request) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(request.appointmentDate());
		appointment.setStatus(request.status());

		Doctor doctor = doctorRepo.findById(request.doctorId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Doctor not found with id: " + request.doctorId()));
		Patient patient = patientRepo.findById(request.patientId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Patient not found with id: " + request.patientId()));
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);

		return appointmentRepo.save(appointment);
	}

	@Override
	public List<Appointment> findByDoctorDoctorId(Integer doctorId) {
		return appointmentRepo.findByDoctorDoctorId(doctorId);
	}

	@Override
	public List<Appointment> findByPatientPatientId(Integer patientId) {
		return appointmentRepo.findByPatientPatientId(patientId);
	}

	@Override
	public List<Appointment> findByAppointmentDate(LocalDate appointmentDate) {
		return appointmentRepo.findByAppointmentDate(appointmentDate);
	}

	@Override
	public List<Appointment> findByStatusContaining(String status) {
		return appointmentRepo.findByStatusContaining(status);
	}

	@Override
	public Appointment findById(Integer appointmentId) {
		return appointmentRepo.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id: " + appointmentId));
	}

	@Override
	public Appointment updateAppointment(Integer id, AppointmentUpdateRequest request) {
		Appointment appointment = findById(id);

		appointment.setAppointmentDate(request.appointmentDate());
		appointment.setStatus(request.status());

		if (request.doctorId() != null) {
			Doctor doctor = doctorRepo.findById(request.doctorId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Doctor not found with id: " + request.doctorId()));
			appointment.setDoctor(doctor);
		}
		if (request.patientId() != null) {
			Patient patient = patientRepo.findById(request.patientId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Patient not found with id: " + request.patientId()));
			appointment.setPatient(patient);
		}

		return appointmentRepo.save(appointment);
	}

	@Override
	public List<Appointment> findAllAppointments() {
		return appointmentRepo.findAll();
	}

	@Override
	public void deleteAppointmentById(Integer id) {
		appointmentRepo.delete(findById(id));
	}
}
