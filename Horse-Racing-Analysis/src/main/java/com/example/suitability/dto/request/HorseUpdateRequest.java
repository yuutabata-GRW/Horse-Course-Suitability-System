package com.example.suitability.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record HorseUpdateRequest(
		
		@NotBlank(message = "馬名は必須です。")
		@Size(min = 2, max = 18, message = "馬名は2文字以上18文字以内で入力してください。")
		String name,
		
		@NotNull(message = "生年月日は必須です。")
		@Past(message = "生年月日は過去の日付を指定してください。")
		LocalDate birthDate,
		
		@NotBlank(message = "性別は必須です。")
		@Size(max = 10, message = "性別は10文字以内で入力してください。")
		String sex
		
		) {

}
