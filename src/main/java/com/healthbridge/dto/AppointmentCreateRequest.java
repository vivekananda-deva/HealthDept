package com.healthbridge.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentCreateRequest(

		@NotNull(message = "appointmentDate is required")
		@FutureOrPresent(message = "appointmentDate cannot be in the past")
		LocalDate appointmentDate,

		@NotBlank(message = "status is required")
		String status,

		@NotNull(message = "doctorId is required")
		Integer doctorId,

		@NotNull(message = "patientId is required")
		Integer patientId
) {}
