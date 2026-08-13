package com.example.suitability.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record HorseCreateRequest(
		
		@NotBlank(message = "馬名は必須です。")
		String name,
		
		@NotNull(message = "生年月日は必須です。")
		@Past(message = "生年月日は過去の日付を指定してください。")
		LocalDate birthDate,
		
		@NotBlank(message = "性別は必須です。")
		String sex
		) {

}
