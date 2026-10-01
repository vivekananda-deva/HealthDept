package com.healthbridge.dto;

import jakarta.validation.constraints.NotBlank;

public record HospitalUpdateRequest(

		@NotBlank(message = "hospitalName is required")
		String hospitalName,

		@NotBlank(message = "location is required")
		String location
) {}
