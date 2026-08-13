package com.example.suitability.dto.response;

import java.util.List;

public record ErrorResponse(
		int status,
		List<String> messages
		) {

}
