package com.example.suitability.service;

import org.springframework.stereotype.Service;

import com.example.suitability.entity.RaceEntry;
import com.example.suitability.repository.RaceEntryRepository;

@Service
public class RaceEntryService {
	
	private final RaceEntryRepository raceEntryRepository;
	
	public RaceEntryService(RaceEntryRepository raceEntryRepository) {
		this.raceEntryRepository = raceEntryRepository;
	}

	public RaceEntry save(RaceEntry raceEntry) {
		return raceEntryRepository.save(raceEntry);
	}
}
