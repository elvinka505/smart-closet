package ru.isgaij.smartcloset.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ColorService {
    private static final Logger log = LoggerFactory.getLogger(ColorService.class);
    private final RestTemplate restTemplate;

    public ColorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("colorInfo")
    public Map<String, String> getColorInfo(String hex) {
        try {
            String cleanHex = hex.replace("#", "");

            // Добавляем User-Agent чтобы обойти блокировку Cloudflare
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (compatible; SmartCloset/1.0)");
            org.springframework.http.HttpEntity<String> entity =
                    new org.springframework.http.HttpEntity<>(headers);

            String url = "https://colornames.org/search/json/?hex=" + cleanHex;
            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            String name = (body != null && body.get("name") != null)
                    ? (String) body.get("name")
                    : "#" + cleanHex;

            // Дополнительный цвет — считаем сами
            int r = Integer.parseInt(cleanHex.substring(0, 2), 16);
            int g = Integer.parseInt(cleanHex.substring(2, 4), 16);
            int b = Integer.parseInt(cleanHex.substring(4, 6), 16);
            String complement = String.format("%02X%02X%02X", 255 - r, 255 - g, 255 - b);

            Map<String, String> result = new HashMap<>();
            result.put("name", name);
            result.put("complement", complement);
            return result;

        } catch (Exception e) {
            log.error("Ошибка при запросе Color API: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}