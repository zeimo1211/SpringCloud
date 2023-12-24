//RoomServiceImpl类
package com.example.springcloud_provider.servicempl;

import com.example.springcloud_provider.bean.RoomAvailability;
import com.example.springcloud_provider.bean.RoomBean;
import com.example.springcloud_provider.bean.RoomTypeCount;
import com.example.springcloud_provider.mapper.RoomMapper;
import com.example.springcloud_provider.service.RoomService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceImpl implements RoomService {
    @Resource
    private RoomMapper roomMapper;

    @Override
    public List<RoomBean> getAvailableRooms() {
        return roomMapper.getAvailableRooms();
    }

    @Override
    public void bookRoom(Map<String, Object> params) {
        roomMapper.callBookRoom(params);
    }




    @Override
    public void cancelBooking(int bookingId, int userId) {
        // 检查预订是否存在，且是否属于当前用户
        if (roomMapper.isBookingExistsAndBelongsToUser(bookingId, userId)) {
            roomMapper.cancelBooking(bookingId);
        } else {
            throw new IllegalArgumentException("Booking does not exist or does not belong to the user.");
        }
    }
    @Override
    public void confirmBooking(int bookingId) {
        roomMapper.checkIn(bookingId);
    }

    @Override
    public void adminCancelBooking(int bookingId) {
        roomMapper.checkOut(bookingId);
    }


    @Override
    public List<RoomBean> getUserBookings(int userId) {
        return roomMapper.getBookingsByUserId(userId);
    }

    @Override
    public List<RoomBean> getAllRooms() {
        return roomMapper.getAllRooms();
    }

    @Override
    public List<RoomTypeCount> getAvailableRoomTypes() {
        return roomMapper.getAvailableRoomTypes();
    }


    public List<RoomTypeCount> getAvailableRoomTypes(Date checkInDate, Date checkOutDate) {
        List<RoomTypeCount> roomTypes = roomMapper.getAvailableRoomTypes();
        long daysBetween = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);

        for (RoomTypeCount type : roomTypes) {
            double expectedPrice = type.getPrice() * daysBetween;
            type.setExpectedPrice(expectedPrice);
        }

        return roomTypes;
    }
    @Override
    public BigDecimal calculateAndPayRoomCharges(int customerId) {
        // 直接调用 RoomMapper 中的方法
        return roomMapper.calculateTotalCost(customerId);
    }

    public void bookRoomByTypeAndCustomer(Map<String, Object> params) {
        roomMapper.bookRoomByTypeAndCustomer(params);
    }

    @Override
    public List<RoomTypeCount> getAvailableRoomTypesInRange(Date startDate, Date endDate) {
        return roomMapper.getAvailableRoomTypesInRange(startDate, endDate);
    }

    @Override
    public List<RoomAvailability> UpdateRoomAvailabilityStatus() {
        return roomMapper.updateRoomAvailabilityStatus();
    }

    // RoomServiceImpl.java

//    @Override
//    public void bookRoomInSevenDays(Map<String, Object> params) {
//        // 提取参数
//        int userId = Integer.parseInt(params.get("userId").toString());
//        String roomType = params.get("roomType").toString();
//        Date checkInDate = (Date)params.get("checkInDate"); // 确保日期格式正确
//        Date checkOutDate = (Date)params.get("checkOutDate"); // 确保日期格式正确
//        double amount = Double.parseDouble(params.get("amount").toString());
//
//        // 调用 RoomMapper 方法
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("customerId", userId);
//        paramMap.put("roomType", roomType);
//        paramMap.put("checkInDate", checkInDate);
//        paramMap.put("checkOutDate", checkOutDate);
//        paramMap.put("amount", amount);
//        roomMapper.bookRoomInSevenDays(paramMap);
//    }
@Override
public void bookRoomInSevenDays(Map<String, Object> params) {
    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // 提取并转换参数
        int userId = Integer.parseInt(params.get("userId").toString());
        String roomType = params.get("roomType").toString();
        Date checkInDate = formatter.parse(params.get("checkInDate").toString());
        Date checkOutDate = formatter.parse(params.get("checkOutDate").toString());
        double amount = Double.parseDouble(params.get("amount").toString());

        // 构建参数映射
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("customerId", userId);
        paramMap.put("roomType", roomType);
        paramMap.put("checkInDate", new java.sql.Date(checkInDate.getTime()));
        paramMap.put("checkOutDate", new java.sql.Date(checkOutDate.getTime()));
        paramMap.put("amount", amount);

        // 调用 RoomMapper 方法
        roomMapper.bookRoomInSevenDays(paramMap);
    } catch (ParseException e) {
        // 处理 ParseException 异常，例如记录日志或者抛出自定义异常
        e.printStackTrace();
        // 可以根据业务需要进行异常处理
    }
}


}
