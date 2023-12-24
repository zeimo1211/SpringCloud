//BookingController类
package com.example.springcloud_provider.controller;

import com.example.springcloud_provider.servicempl.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookingRoom")
    public String bookingRoom(@RequestParam("customerID") int customerID,
                              @RequestParam("roomType") String roomType,
                              @RequestParam("checkInDate") String checkInDate,
                              @RequestParam("checkOutDate") String checkOutDate,
                              @RequestParam("amount") double amount,
                              Model model) {
        boolean success = bookingService.bookingRoom(customerID, roomType, checkInDate, checkOutDate, amount);
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