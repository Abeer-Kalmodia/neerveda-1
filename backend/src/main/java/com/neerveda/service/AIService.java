package com.neerveda.service;

import com.neerveda.model.PredictionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    public PredictionResponse predict(
            double ph,
            int tds,
            double turbidity,
            double temperature) {

        try {

            RestTemplate restTemplate =
                    new RestTemplate();

            Map<String,Object> body =
                    new HashMap<>();

            body.put("ph", ph);
            body.put("tds", tds);
            body.put("turbidity", turbidity);
            body.put("temperature", temperature);

            return restTemplate.postForObject(
                    "http://localhost:5000/predict",
                    body,
                    PredictionResponse.class
            );

        } catch (Exception e) {

            PredictionResponse fallback =
                    new PredictionResponse();

            fallback.setOutbreakRisk("UNKNOWN");
            fallback.setConfidence(0);

            return fallback;
        }
    }
}