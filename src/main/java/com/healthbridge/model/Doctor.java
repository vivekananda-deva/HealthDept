package com.healthbridge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer doctorId;

	private String doctorName;

	private String specialization;

	@ManyToOne
	@JoinColumn(name = "hospital_Id")
	@JsonIgnoreProperties({"doctors"})
	private Hospital hospital;

	@OneToMany(mappedBy = "doctor", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	private List<Appointment> appointments;
}
