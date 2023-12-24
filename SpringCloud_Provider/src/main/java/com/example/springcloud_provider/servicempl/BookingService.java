package com.example.springcloud_provider.servicempl;

// 在你的Spring Boot应用程序中的相应类中添加以下方法来处理预订功能

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private final JdbcTemplate jdbcTemplate;

    public BookingService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public boolean bookingRoom(int customerID, String roomType, String checkInDate, String checkOutDate, double amount) {
        try {
            jdbcTemplate.update("CALL bookroom_by_customerID_and_Roomtype(?, ?, ?, ?, ?)",
                    customerID, roomType, checkInDate, checkOutDate, amount);
            return true; // 预订成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 预订失败
        }
    }
}



