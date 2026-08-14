package com.example.suitability.service;

import org.springframework.stereotype.Service;

import com.example.suitability.dto.request.HorseCreateRequest;
import com.example.suitability.dto.response.HorseResponse;
import com.example.suitability.entity.Horse;
import com.example.suitability.exception.HorseNotFoundException;
import com.example.suitability.repository.HorseRepository;

@Service
public class HorseService {
	
	private final HorseRepository horseRepository;
	
	public HorseService(HorseRepository horseRepository) {
		this.horseRepository = horseRepository;
	}
	
	public HorseResponse create(HorseCreateRequest request) {
		
		Horse horse = new Horse();
		
		horse.setName(request.name());
		horse.setBirthDate(request.birthDate());
		horse.setSex(request.sex());
		
		Horse savedHorse = horseRepository.save(horse);
		
		return new HorseResponse(
				savedHorse.getId(),
				savedHorse.getName(),
				savedHorse.getBirthDate(),
				savedHorse.getSex()
				);
	}
	
	public HorseResponse findById(Long id) {
		
		Horse horse = horseRepository.findById(id)
				.orElseThrow(
						() -> new HorseNotFoundException("指定された競走馬が存在しません。")
						);
		
		return new HorseResponse(
				horse.getId(),
				horse.getName(),
				horse.getBirthDate(),
				horse.getSex()
				);
	}

}
