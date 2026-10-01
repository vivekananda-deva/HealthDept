package com.healthbridge.dto;

import jakarta.validation.constraints.NotBlank;

public record DoctorUpdateRequest(

		@NotBlank(message = "doctorName is required")
		String doctorName,

		@NotBlank(message = "specialization is required")
		String specialization,

		// Optional on update: omit to leave the doctor's hospital unchanged.
		Integer hospitalId
) {}
