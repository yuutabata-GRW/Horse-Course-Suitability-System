package com.example.suitability.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.suitability.dto.request.HorseCreateRequest;
import com.example.suitability.dto.request.HorseUpdateRequest;
import com.example.suitability.dto.response.HorseResponse;
import com.example.suitability.service.HorseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/horses")
@Validated
public class HorseController {
	
	private final HorseService horseService;
	
	public HorseController(HorseService horseService) {
		this.horseService = horseService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public HorseResponse create(@Valid @RequestBody HorseCreateRequest request) {
		return horseService.create(request);
	}
	
	@GetMapping("/{id}")
	public HorseResponse findById(@PathVariable Long id) {
		return horseService.findById(id);
	}
	
	@GetMapping
	public Page<HorseResponse> findAll(
			@RequestParam(defaultValue = "1")
			@Min(value = 1, message = "pageは1以上を指定してください。")
			int page){
		return horseService.findAll(page);
	}
	
	@PutMapping("/{id}")
	public HorseResponse update(
			@PathVariable Long id,
			@Valid @RequestBody HorseUpdateRequest request
			) {
		return horseService.update(id, request);
	}

}
