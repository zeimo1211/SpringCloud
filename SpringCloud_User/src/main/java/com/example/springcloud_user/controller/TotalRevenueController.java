// TotalRevenueController类
package com.example.springcloud_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class TotalRevenueController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getTotalRevenue")
    public Map<String, Double> getTotalRevenue() {
        String url = "http://localhost:8541/getTotalRevenue";

        ResponseEntity<Map<String, Double>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Double>>() {});
        Map<String, Double> response = responseEntity.getBody();

        return response;
    }
}
