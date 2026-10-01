package com.healthbridge.controller;

import java.time.LocalDate;
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

import com.healthbridge.dto.AppointmentCreateRequest;
import com.healthbridge.dto.AppointmentUpdateRequest;
import com.healthbridge.model.Appointment;
import com.healthbridge.service.AppointmentService;

@RestController
@RequestMapping("appointment/api/v1")
public class AppointmentController {

	@Autowired
	AppointmentService appointmentService;

	@PostMapping("insertAp")
	public Appointment saveAppointmentInfo(@RequestBody @Valid AppointmentCreateRequest request) {
		return appointmentService.saveAppointment(request);
	}

	@GetMapping("getById/{id}")
	public Appointment getById(@PathVariable Integer id) {
		return appointmentService.findById(id);
	}

	@GetMapping("byDoctor/{doctorId}")
	public List<Appointment> getByDoctor(@PathVariable Integer doctorId) {
		return appointmentService.findByDoctorDoctorId(doctorId);
	}

	@GetMapping("byPatient/{patientId}")
	public List<Appointment> getByPatient(@PathVariable Integer patientId) {
		return appointmentService.findByPatientPatientId(patientId);
	}

	@GetMapping("byDate/{date}")
	public List<Appointment> getByDate(@PathVariable LocalDate date) {
		return appointmentService.findByAppointmentDate(date);
	}

	@GetMapping("byStatus/{status}")
	public List<Appointment> getByStatus(@PathVariable String status) {
		return appointmentService.findByStatusContaining(status);
	}

	@PutMapping("updateById/{id}")
	public Appointment updateAppointment(@PathVariable Integer id, @RequestBody @Valid AppointmentUpdateRequest request) {
		return appointmentService.updateAppointment(id, request);
	}

	@GetMapping("getAllAppointments")
	public List<Appointment> getAllAppointments() {
		return appointmentService.findAllAppointments();
	}

	@DeleteMapping("deleteById/{id}")
	void deleteById(@PathVariable Integer id) {
		appointmentService.deleteAppointmentById(id);
	}
}
