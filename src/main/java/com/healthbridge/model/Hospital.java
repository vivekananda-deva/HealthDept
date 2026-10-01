package com.healthbridge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hospital")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hospitalId;

	private String hospitalName;

	private String location;

	@OneToMany(mappedBy = "hospital", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnoreProperties({"hospital"})
	private List<Doctor> doctors;
}
