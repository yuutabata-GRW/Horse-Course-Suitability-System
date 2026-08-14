package com.example.suitability.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(
				name = "uk_race_horse",
				columnNames = {"race_id", "horse_id"}
				)
		}
)
@Getter
@Setter
public class RaceEntry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "race_id", nullable = false)
	private Race race;
	
	@ManyToOne
	@JoinColumn(name = "horse_id", nullable = false)
	private Horse horse;
	
	@Column(name = "frame_number", nullable = false)
	private int frameNumber;
	
	@Column(name = "horse_number", nullable = false)
	private int horseNumber;
	
	@Column(nullable = false)
	private Boolean finished;
	
	@Column(name = "finish_position")
	private Integer finishPosition;
	
	@Column(name = "time_seconds", precision = 6, scale = 3)
	private BigDecimal timeSeconds;
	
	@Column(name = "last_3f", precision = 4, scale = 1)
	private BigDecimal last3f;
	
	@Column(name = "running_style", length = 20)
	private String runningStyle;

}
