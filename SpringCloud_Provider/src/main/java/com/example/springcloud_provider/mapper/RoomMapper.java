//RoomMapper类
package com.example.springcloud_provider.mapper;


import com.example.springcloud_provider.bean.RoomAvailability;
import com.example.springcloud_provider.bean.RoomBean;
import com.example.springcloud_provider.bean.RoomTypeCount;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RoomMapper {

    List<RoomBean> getAvailableRooms();
    void bookRoom(@Param("params") Map<String, Object> params);
    void callBookRoom(Map<String, Object> params);
    void cancelBooking(@Param("p_BookingID") int bookingId);
    boolean isBookingExistsAndBelongsToUser(@Param("bookingId") int bookingId, @Param("userId") int userId);
    List<RoomBean> getBookingsByUserId(@Param("userId") int userId);

    List<RoomBean> getAllRooms();

    @Update("CALL CheckIn(#{bookingId})")
    void checkIn(@Param("bookingId") int bookingId);

    @Update("CALL CheckOut(#{bookingId})")
    void checkOut(@Param("bookingId") int bookingId);

//    @Select("SELECT RoomType, COUNT(*) as count FROM Rooms WHERE Status = 'available' GROUP BY RoomType")
//    List<RoomTypeCount> getAvailableRoomTypes(); // 使用 RoomTypeCount


    void calculateTotalCostAndUpdatePayments(Map<String, Object> params);
    // RoomMapper.java
    BigDecimal calculateTotalCost(@Param("customerId") int customerId);

    @Select("SELECT RoomType, COUNT(*) as count, AVG(Price) as price FROM Rooms WHERE Status = 'available' GROUP BY RoomType")
    List<RoomTypeCount> getAvailableRoomTypes();

    void bookRoomByTypeAndCustomer(@Param("params") Map<String, Object> params);

    List<RoomTypeCount> getAvailableRoomTypesInRange(@Param("startDate") java.util.Date startDate,
                                                     @Param("endDate") java.util.Date endDate);


    // 更新房间可用性状态
    @Select("CALL UpdateRoomAvailabilityStatus()")
    @Results({
            @Result(property = "date", column = "Date"),
            @Result(property = "roomType", column = "RoomType"),
            @Result(property = "availableCount", column = "AvailableRooms")
    })
    List<RoomAvailability> updateRoomAvailabilityStatus();

    // 用于预订房间
    // RoomMapper.java
//    @Insert("CALL bookinsevendays(#{name, jdbcType=VARCHAR}, #{email, jdbcType=VARCHAR}, #{phone, jdbcType=VARCHAR}, #{checkInDate, jdbcType=DATE}, #{checkOutDate, jdbcType=DATE}, #{amount, jdbcType=DECIMAL}, #{roomType, jdbcType=VARCHAR})")
//    void bookRoomInSevenDays(Map<String, Object> params);

    @Insert("CALL bookinsevendays(#{customerId, jdbcType=INTEGER}, #{roomType, jdbcType=VARCHAR}, #{checkInDate, jdbcType=DATE}, #{checkOutDate, jdbcType=DATE}, #{amount, jdbcType=DECIMAL})")
    void bookRoomInSevenDays(Map<String, Object> params);







}


