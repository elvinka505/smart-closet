package ru.isgaij.smartcloset.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
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
            String url1 = "https://www.thecolorapi.com/id?hex=" + hex;
            Map<String, Object> response1 = restTemplate.getForObject(url1, Map.class);
            Map<String, Object> nameObj = (Map<String, Object>) response1.get("name");

            String url2 = "https://www.thecolorapi.com/scheme?hex=" + hex + "&mode=complement";
            Map<String, Object> response2 = restTemplate.getForObject(url2, Map.class);
            List<Map<String, Object>> colors = (List<Map<String, Object>>) response2.get("colors");
            Map<String, Object> complementHex = (Map<String, Object>) ((Map<String, Object>) colors.get(1)).get("hex");

            Map<String, String> result = new HashMap<>();
            result.put("name", (String) nameObj.get("value"));
            result.put("complement", (String) complementHex.get("value"));
            return result;

        } catch (Exception e) {
            log.error("Ошибка при запросе Color API: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}