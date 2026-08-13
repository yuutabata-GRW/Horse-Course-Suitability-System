package com.example.suitability.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Horse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 18)
	private String name;
	
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
	
	@Column(nullable = false, length = 10)
	private String sex;
	
	@OneToMany(mappedBy = "horse")
	private List<RaceEntry> raceEntries;
}
