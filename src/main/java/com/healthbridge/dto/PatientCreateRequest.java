package com.healthbridge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientCreateRequest(

		@NotBlank(message = "patientName is required")
		String patientName,

		@NotBlank(message = "phone is required")
		@Pattern(regexp = "^[0-9]{10}$", message = "phone must be a 10-digit number")
		String phone
) {}
