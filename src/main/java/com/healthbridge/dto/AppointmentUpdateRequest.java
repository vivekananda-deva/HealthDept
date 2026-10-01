package com.healthbridge.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentUpdateRequest(

		@NotNull(message = "appointmentDate is required")
		LocalDate appointmentDate,

		@NotBlank(message = "status is required")
		String status,

		// Optional on update: omit to leave doctor/patient unchanged.
		Integer doctorId,
		Integer patientId
) {}
