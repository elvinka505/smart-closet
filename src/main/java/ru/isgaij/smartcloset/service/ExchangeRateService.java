package ru.isgaij.smartcloset.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);
    private static final String API_URL = "https://open.er-api.com/v6/latest/RUB";

    private final RestTemplate restTemplate;
    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("exchangeRates")
    public Map<String, Double> getSelectedRates() {
        try {
            Map<String, Object> response = this.restTemplate.getForObject(API_URL, Map.class);            Map<String, Double> allRates = (Map<String, Double>) response.get("rates");
            Map<String, Double> selected = new HashMap<>();
            selected.put("USD", allRates.get("USD"));
            selected.put("EUR", allRates.get("EUR"));
            selected.put("TRY", allRates.get("TRY"));


            return selected;
        } catch (Exception e) {
            log.error("Failed to fetch exchange rates: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
