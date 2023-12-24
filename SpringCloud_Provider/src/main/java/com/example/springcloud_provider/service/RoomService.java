//RoomService类

package com.example.springcloud_provider.service;
import com.example.springcloud_provider.bean.RoomAvailability;
import com.example.springcloud_provider.bean.RoomBean;
import com.example.springcloud_provider.bean.RoomTypeCount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RoomService {
    List<RoomBean> getAvailableRooms();
    void bookRoom(Map<String, Object> params);
    void cancelBooking(int bookingId, int userId);
    List<RoomBean> getUserBookings(int userId); // 新增的方法

    List<RoomBean> getAllRooms();

    void confirmBooking(int bookingId);
    void adminCancelBooking(int bookingId);

    List<RoomTypeCount> getAvailableRoomTypes(); // 新方法
    BigDecimal calculateAndPayRoomCharges(int customerId);
    void bookRoomByTypeAndCustomer(Map<String, Object> params);
  List<RoomTypeCount> getAvailableRoomTypesInRange(Date startDate, Date endDate);
    List<RoomAvailability> UpdateRoomAvailabilityStatus();

    void bookRoomInSevenDays(Map<String, Object> params);

}









