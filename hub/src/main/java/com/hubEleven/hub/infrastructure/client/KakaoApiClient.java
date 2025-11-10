package com.hubEleven.hub.infrastructure.client;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.hub.common.exception.HubErrorCode;
import com.hubEleven.hub.infrastructure.config.KakaoApiProperties;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoApiClient {

	private final KakaoApiProperties properties;

	public Double[] getCoordinates(String address) {
		try {
			String query = URLEncoder.encode(address, StandardCharsets.UTF_8);
			String urlString = "https://dapi.kakao.com/v2/local/search/address.json?query=" + query;

			log.info("Kakao API 호출: address={}", address);
			log.info("Request URL: {}", urlString);

			HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "KakaoAK " + properties.getKey());

			int status = conn.getResponseCode();
			InputStream is =
					(status == HttpURLConnection.HTTP_OK) ? conn.getInputStream() : conn.getErrorStream();

			StringBuilder sb = new StringBuilder();
			try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
					BufferedReader br = new BufferedReader(isr)) {
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
			}

			String json = sb.toString();
			log.info("Kakao API 응답: status={}, body={}", status, json);

			if (status != HttpURLConnection.HTTP_OK) {
				log.error("Kakao API 호출 실패: status={}, body={}", status, json);
				throw new GlobalException(HubErrorCode.KAKAO_API_ERROR);
			}

			// 좌표 파싱
			if (!json.contains("\"x\":\"") || !json.contains("\"y\":\"")) {
				log.warn("주소 검색 결과 없음: address={}", address);
				throw new GlobalException(HubErrorCode.GEOCODING_FAILED);
			}

			String longitude = json.split("\"x\":\"")[1].split("\"")[0];
			String latitude = json.split("\"y\":\"")[1].split("\"")[0];

			Double lat = Double.parseDouble(latitude);
			Double lon = Double.parseDouble(longitude);

			log.info("Geocoding 성공: address={}, lat={}, lon={}", address, lat, lon);

			return new Double[] {lat, lon};

		} catch (GlobalException e) {
			throw e;
		} catch (Exception e) {
			log.error("Kakao API 호출 중 예외 발생: {}", e.getMessage(), e);
			throw new GlobalException(HubErrorCode.KAKAO_API_ERROR);
		}
	}
}
