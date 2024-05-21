package com.cookshare.board.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.cookshare.board.dto.Coordinates;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

	@Value("${naver.geocode.api.client-id}")
	private String geoCodeClientId;

	@Value("${naver.geocode.api.client-secret}")
	private String geoCodeClientSecret;

	@Value("${naver.search.api.client-id}")
	private String searchClientId;

	@Value("${naver.search.api.client-secret}")
	private String searchClientSecret;

	private final RestTemplate restTemplate;
	private final ConcurrentHashMap<String, String> suggestionCache = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, Coordinates> geocodeCache = new ConcurrentHashMap<>();
	public Optional<byte[]> getStaticMap(double lat, double lng) {
		if (Math.abs(lat) > 90 || Math.abs(lng) > 180) {
			log.error("Invalid coordinates: latitude must be between -90 and 90, longitude must be between -180 and 180.");
			return Optional.empty();
		}

		String url = String.format("https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=640&h=360&markers=type:d|size:mid|pos:%f %f", lng, lat);

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", geoCodeClientId);
		headers.set("X-NCP-APIGW-API-KEY", geoCodeClientSecret);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			log.info("Requesting static map: {}", url);
			ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
			if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
				return Optional.of(response.getBody());
			}
			log.error("Failed to fetch static map, status code: {}, response: {}", response.getStatusCode(), response.getBody());
		} catch (RestClientException e) {
			log.error("Error during static map API call: ", e);
		}
		return Optional.empty();
	}

	public Optional<Coordinates> getCoordinates(String address) {
		if (geocodeCache.containsKey(address)) {
			return Optional.of(geocodeCache.get(address));
		}

		URI uri = UriComponentsBuilder
			.fromHttpUrl("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
			.queryParam("query", address)
			.encode(StandardCharsets.UTF_8)
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-NCP-APIGW-API-KEY-ID", geoCodeClientId);
		headers.set("X-NCP-APIGW-API-KEY", geoCodeClientSecret);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
				JSONObject jsonObject = new JSONObject(response.getBody());
				JSONArray addresses = jsonObject.optJSONArray("addresses");
				if (addresses != null && addresses.length() > 0) {
					JSONObject addressInfo = addresses.getJSONObject(0);
					if(addressInfo.has("y") && addressInfo.has("x")) {
						double lat = addressInfo.getDouble("y");
						double lon = addressInfo.getDouble("x");
						Coordinates coordinates = new Coordinates(lat, lon);
						geocodeCache.put(address, coordinates);
						return Optional.of(coordinates);
					}
				}
				log.info("No valid address data found for: {}", address);
			} else {
				log.error("Failed to fetch coordinates, status code: {}", response.getStatusCode());
			}
		} catch (RestClientException e) {
			log.error("Error during geocode API call: ", e);
		}
		return Optional.empty();
	}



	public String getSuggestions(String query) {
		if (suggestionCache.containsKey(query)) {
			return suggestionCache.get(query);
		}

		log.info("Using search API with clientId: {} and clientSecret: {}", searchClientId, searchClientSecret);

		URI uri = UriComponentsBuilder
			.fromHttpUrl("https://openapi.naver.com/v1/search/local.json")
			.queryParam("query", query)
			.queryParam("display", 5)
			.encode(StandardCharsets.UTF_8)
			.build()
			.toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", searchClientId);
		headers.set("X-Naver-Client-Secret", searchClientSecret);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
			log.info("Request URL: {}", uri);
			log.info("Request Headers: {}", headers);

			if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
				String body = response.getBody();
				log.info("Response Body: {}", body);
				suggestionCache.put(query, body); // 캐싱
				return body;
			}
			log.error("Failed to fetch suggestions, status code: {}", response.getStatusCode());
		} catch (RestClientException e) {
			log.error("Error during suggestions API call: ", e);
		}
		return null;
	}
}



