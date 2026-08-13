package com.example.suitability.service;

import org.springframework.stereotype.Service;

import com.example.suitability.entity.Race;
import com.example.suitability.repository.RaceRepository;

@Service
public class RaceService {
	
	private final RaceRepository raceRepository;
	
	public RaceService(RaceRepository raceRepository) {
		this.raceRepository = raceRepository;
	}
	
	public Race save(Race race) {
		return raceRepository.save(race);
	}

}
