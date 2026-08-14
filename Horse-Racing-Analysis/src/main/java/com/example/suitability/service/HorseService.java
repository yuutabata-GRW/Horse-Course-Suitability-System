package com.example.suitability.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.suitability.dto.request.HorseCreateRequest;
import com.example.suitability.dto.request.HorseUpdateRequest;
import com.example.suitability.dto.response.HorseResponse;
import com.example.suitability.entity.Horse;
import com.example.suitability.exception.HorseAlreadyExistsException;
import com.example.suitability.exception.HorseNotFoundException;
import com.example.suitability.repository.HorseRepository;

@Service
public class HorseService {
	
	private final HorseRepository horseRepository;
	
	public HorseService(HorseRepository horseRepository) {
		this.horseRepository = horseRepository;
	}
	
	public HorseResponse create(HorseCreateRequest request) {
		
		boolean exists = horseRepository.existsByNameAndBirthDate(
				request.name(),
				request.birthDate());
		
		if(exists) {
			throw new HorseAlreadyExistsException("同じ馬名と生年月日の馬がすでに登録されています。");
		}
		
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
	
	public Page<HorseResponse> findAll(int page){
		
		//ページを1からとして登録が新しい順に表示
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
		
		return horseRepository.findAll(pageable)
				.map(horse -> new HorseResponse(
						horse.getId(),
						horse.getName(),
						horse.getBirthDate(),
						horse.getSex()
						));
	}
	
	public HorseResponse update(Long id, HorseUpdateRequest request) {
		
		Horse horse = horseRepository.findById(id)
				.orElseThrow(
						() -> new HorseNotFoundException("指定された競走馬が存在しません。")
						);
		
		horse.setName(request.name());
		horse.setBirthDate(request.birthDate());
		horse.setSex(request.sex());
		
		Horse updatedHorse = horseRepository.save(horse);
		
		return new HorseResponse(
				updatedHorse.getId(),
				updatedHorse.getName(),
				updatedHorse.getBirthDate(),
				updatedHorse.getSex()
				);
	}

}
