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
public class Race {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "race_date", nullable = false)
	private LocalDate raceDate;
	
	@Column(nullable = false, length = 50)
	private String venue;
	
	@Column(nullable = false, length = 20)
	private String surface;
	
	@Column(nullable = false)
	private Integer distance;
	
	@Column(name = "track_condition", nullable = false, length = 20)
	private String trackCondition;
	
	@Column(nullable = false, length = 20)
	private String weather;
	
	@OneToMany(mappedBy = "race")
	private List<RaceEntry> raceEntries;

}
