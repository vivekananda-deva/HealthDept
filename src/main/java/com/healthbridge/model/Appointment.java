package com.healthbridge.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer appointmentId;

	private LocalDate appointmentDate;

	private String status;

	@ManyToOne
	@JoinColumn(name = "doctor_id")
	@JsonIgnoreProperties({"appointments"})
	private Doctor doctor;

	@ManyToOne
	@JoinColumn(name = "patient_id")
	@JsonIgnoreProperties({"appointments"})
	private Patient patient;
}
