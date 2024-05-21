package com.cookshare.board.controller;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cookshare.board.dto.Coordinates;
import com.cookshare.board.service.NaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NaverController {

	private final NaverService naverService;

	@GetMapping("/staticmap")
	public ResponseEntity<byte[]> getStaticMap(@RequestParam double lat, @RequestParam double lng) {
		Optional<byte[]> mapImage = naverService.getStaticMap(lat, lng);
		if (mapImage.isPresent()) {
			return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_PNG) // 이미지 형식 지정
				.body(mapImage.get());
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/geocode")
	public ResponseEntity<Coordinates> getGeocode(@RequestParam String address) {
		Optional<Coordinates> coordinates = naverService.getCoordinates(address);
		log.info("Geocode coordinates: {}", coordinates);
		return coordinates
			.map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/suggest")
	public ResponseEntity<String> getSuggestions(@RequestParam String query) {
		log.info("Suggest query: {}", query);
		String suggestions = naverService.getSuggestions(query);
		if (suggestions != null) {
			return ResponseEntity.ok(suggestions);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}



