package com.healthbridge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateRequest(

		@NotBlank(message = "doctorName is required")
		String doctorName,

		@NotBlank(message = "specialization is required")
		String specialization,

		@NotNull(message = "hospitalId is required")
		Integer hospitalId
) {}
