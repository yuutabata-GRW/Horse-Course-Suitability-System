package com.example.suitability.dto.response;

import java.time.LocalDate;

public record HorseResponse(
		Long id,
		String name,
		LocalDate birthDate,
		String sex
		) {

}
