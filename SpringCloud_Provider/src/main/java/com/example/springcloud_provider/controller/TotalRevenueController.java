package com.example.springcloud_provider.controller;

import com.example.springcloud_provider.servicempl.TotalChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TotalRevenueController {

    @Autowired
    private TotalChargeService totalChargeService;

    @GetMapping("/getTotalRevenue")
    public Map<String, Double> getTotalRevenue() {
        Map<String, Double> response = new HashMap<>();
        double totalRevenue = totalChargeService.calculateTotalRevenue();
        response.put("totalRevenue", totalRevenue);
        return response;
    }
}
