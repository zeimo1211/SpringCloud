// BookingController类
package com.example.springcloud_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BookingController {
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/bookingRoom")
    public String bookingRoom(@RequestParam("customerID") int customerID,
                              @RequestParam("roomType") String roomType,
                              @RequestParam("checkInDate") String checkInDate,
                              @RequestParam("checkOutDate") String checkOutDate,
                              @RequestParam("amount") double amount,
                              Model model) {
        String url = "http://localhost:8541/bookingRoom";
        Map<String, Object> params = new HashMap<>();
        params.put("customerID", customerID);
        params.put("roomType", roomType);
        params.put("checkInDate", checkInDate);
        params.put("checkOutDate", checkOutDate);
        params.put("amount", amount);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Boolean.class);
        boolean success = responseEntity.getBody();

        if (success) {
            // 预订成功，可以根据需要进行其他处理
            model.addAttribute("message", "预订成功！");
        } else {
            // 预订失败，可以根据需要进行其他处理
            model.addAttribute("message", "预订失败，请重试。");
        }
        return "redirect:/availableRoomTypes"; // 重定向到可用房间类型页面
    }
}
