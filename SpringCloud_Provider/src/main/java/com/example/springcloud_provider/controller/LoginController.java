
//LoginController类
package com.example.springcloud_provider.controller;
import com.example.springcloud_provider.bean.RoomAvailability;
import com.example.springcloud_provider.bean.RoomBean;
import com.example.springcloud_provider.bean.RoomTypeCount;
import com.example .springcloud_provider.bean.UserBean;
import com.example.springcloud_provider.service.RoomService;
import com.example.springcloud_provider.service.UserService;
import com.example.springcloud_provider.servicempl.DatabaseBackupService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    @Resource
    UserService userService;
    @Resource
    RoomService roomService;
    @RequestMapping("/loginView")
    public String show() {
        return "loginView";
    }
    private int getCurrentUserId(HttpServletRequest request) {
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }
        return (Integer) userIdObj;
    }

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    public String adminLogin(String name, String password, Model model) {
        if (name != null && password != null) {
            UserBean userBean = userService.logIn(name, password);
            if (userBean != null) {
                List<RoomBean> allRooms = roomService.getAllRooms(); // 获取所有房间的信息
                model.addAttribute("rooms", allRooms);
                return "allRoomsDisplay"; // 导航到展示所有房间信息的页面
            }
        }
        return "error"; // 登录失败
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public String userLogin(String email, String phone, Model model, HttpServletRequest request) {
        if (email != null && phone != null) {
            UserBean userBean = userService.logInByEmailAndPhone(email, phone);
            if (userBean != null) {
                request.getSession().setAttribute("userId", userBean.getId());
                List<RoomBean> availableRooms = roomService.getAvailableRooms();
                model.addAttribute("rooms", availableRooms);
                return "roomDisplay"; // 用户登录成功，显示可预订房间
            }
        }
        return "error"; // 登录失败
    }

     //处理预订请求
    @RequestMapping(value = "/bookRoom", method = RequestMethod.POST)
    public String bookRoom(@RequestParam Map<String, String> allRequestParams, Model model) {
        Map<String, Object> params = new HashMap<>();
        // 从表单获取预订所需信息
        params.put("p_Name", allRequestParams.get("name"));
        params.put("p_Email", allRequestParams.get("email"));
        params.put("p_Phone", allRequestParams.get("phone"));
        params.put("p_RoomNumber", allRequestParams.get("roomNumber"));
        params.put("p_CheckInDate", allRequestParams.get("checkInDate"));
        params.put("p_CheckOutDate", allRequestParams.get("checkOutDate"));
        params.put("p_Amount", Double.parseDouble(allRequestParams.get("amount"))); // 假设前端提交的金额是字符串
        // 调用服务层进行预订
        roomService.bookRoom(params);
        return "bookingResult"; // 返回预订结果页面
    }

    @RequestMapping(value = "/cancelBooking", method = RequestMethod.POST)
    public String cancelBooking(@RequestParam("bookingId") int bookingId, @RequestParam("userId") int userId, Model model) {
        try {
            roomService.cancelBooking(bookingId, userId);
            model.addAttribute("message", "Booking cancelled successfully.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", "Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            model.addAttribute("message", "Error: You must be logged in to cancel a booking.");
        }
        return "cancelBookingResult"; // 返回取消预订结果页面
    }
    @RequestMapping(value = "/showUserBookings", method = RequestMethod.GET)
    public String showUserBookings(Model model, HttpServletRequest request) {
        try {
            int userId = getCurrentUserId(request);
            List<RoomBean> userBookings = roomService.getUserBookings(userId);
            model.addAttribute("userBookings", userBookings);
            return "userBookings"; // 返回显示用户预订的页面
        } catch (IllegalStateException e) {
            model.addAttribute("message", "Error: You must be logged in to view your bookings.");
            return "error"; // 或任何其他错误页面
        }
    }

    @RequestMapping(value = "/inputUserId", method = RequestMethod.GET)
    public String inputUserId() {
        return "inputUserId"; // 返回输入用户ID的页面
    }

    @RequestMapping(value = "/showBookingsByUserId", method = RequestMethod.POST)
    public String showBookingsByUserId(@RequestParam("userId") int userId, Model model) {
        List<RoomBean> userBookings = roomService.getUserBookings(userId);
        model.addAttribute("userBookings", userBookings);
        return "userBookings"; // 返回显示用户预订的页面
    }

    @RequestMapping(value = "/allRooms", method = RequestMethod.GET)
    public String showAllRooms(Model model) {
        List<RoomBean> allRooms = roomService.getAllRooms();
        model.addAttribute("rooms", allRooms);
        return "allRoomsDisplay"; // 新的前端页面显示所有房间信息
    }


    @RequestMapping(value = "/confirmBooking", method = RequestMethod.POST)
    public String confirmBooking(@RequestParam("bookingId") int bookingId, RedirectAttributes redirectAttributes) {
        roomService.confirmBooking(bookingId);
        redirectAttributes.addFlashAttribute("message", "Booking confirmed successfully.");
        return "redirect:/allRooms"; // 确保此处重定向到显示所有房间的正确 URL
    }

    @RequestMapping(value = "/adminCancelBooking", method = RequestMethod.POST)
    public String adminCancelBooking(@RequestParam("bookingId") int bookingId, RedirectAttributes redirectAttributes) {
    roomService.adminCancelBooking(bookingId);
    redirectAttributes.addFlashAttribute("message", "Booking cancelled successfully.");
    return "redirect:/allRooms"; // 确保此处重定向到显示所有房间的正确 URL
}

    @RequestMapping(value = "/availableRoomTypes", method = RequestMethod.GET)
    public String showAvailableRoomTypes(Model model, HttpServletRequest request) {
        LocalDate today = LocalDate.now();
        Date startDate = java.sql.Date.valueOf(today);
        Date endDate = java.sql.Date.valueOf(today.plusDays(5));
//         List<RoomTypeCount> roomTypes = roomService.getAvailableRoomTypesInRange(startDate, endDate);
//        model.addAttribute("roomTypes", roomTypes);
         return "availableRoomTypes";
}




    @RequestMapping(value = "/calculateAndPayRoomCharges", method = RequestMethod.GET)
    public String calculateAndPayRoomCharges(HttpServletRequest request, Model model) {
        int userId = getCurrentUserId(request);
        BigDecimal totalCost = roomService.calculateAndPayRoomCharges(userId);
        model.addAttribute("totalCost", totalCost);
        return "paymentSuccess"; // 跳转到支付成功页面
    }
    // 新的根据客户ID计算费用的方法

    @RequestMapping(value = "/calculateCharges", method = RequestMethod.POST)
    public String calculateCharges(@RequestParam("customerId") int customerId, Model model) {
        BigDecimal totalCost = roomService.calculateAndPayRoomCharges(customerId);
        model.addAttribute("totalCost", totalCost);
        return "paymentSuccess"; // 跳转到支付成功页面
    }

    @RequestMapping(value = "/inputCustomerId", method = RequestMethod.GET)
    public String inputCustomerId() {
        return "inputCustomerId"; // 返回输入客户ID的页面
    }

        @Autowired
        private DatabaseBackupService databaseBackupService;
        @RequestMapping(value = "/backupDatabase", method = RequestMethod.GET)
        public String backupDatabase(Model model) {
            try {
                databaseBackupService.backupDatabase();
                model.addAttribute("message", "Database backup successful.");
            } catch (Exception e) {
                model.addAttribute("message", "Database backup failed: " + e.getMessage());
            }
            return "backupRestoreResult";
        }
        @RequestMapping(value = "/restoreDatabase", method = RequestMethod.GET)
        public String restoreDatabase(Model model) {
            try {
                databaseBackupService.restoreDatabase();
                model.addAttribute("message", "Database restore successful.");
            } catch (Exception e) {
                model.addAttribute("message", "Database restore failed: " + e.getMessage());
            }
            return "backupRestoreResult";
        }
    @PostMapping("/bookRoomByType")
    public String bookRoomByTypeAndCustomer(Model model, int customerId, String roomType, String checkInDate, String checkOutDate, double amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("roomType", roomType);
        params.put("checkInDate", checkInDate);
        params.put("checkOutDate", checkOutDate);
        params.put("amount", amount);
        roomService.bookRoomByTypeAndCustomer(params);
        model.addAttribute("message", "Room booked successfully!");
        return "success"; // 假设一个显示预订确认信息的页面
    }


    @RequestMapping(value = "/displayRoomAvailability", method = RequestMethod.GET)
    public String displayRoomAvailability(Model model) {
        List<RoomAvailability> roomAvailabilityList = roomService.UpdateRoomAvailabilityStatus();
        model.addAttribute("roomAvailability", roomAvailabilityList);
        return "roomAvailabilityDisplay"; // 指向新的 HTML 页面
    }

//    @RequestMapping(value = "/bookRoomInSevenDays", method = RequestMethod.POST)
//    public String bookRoomInSevenDays(@RequestParam Map<String, String> allRequestParams, Model model) {
//        try {
//            // 转换并调用服务层方法
//            roomService.bookRoomInSevenDays(allRequestParams);
//            return "bookingResult"; // 返回预订结果页面
//        } catch (ParseException e) {
//            model.addAttribute("errorMessage", "Date parsing error: " + e.getMessage());
//            return "error"; // 返回错误页面
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Error: " + e.getMessage());
//            return "error"; // 返回错误页面
//        }
//    }
@RequestMapping(value = "/bookRoomInSevenDays", method = RequestMethod.POST)
public String bookRoomInSevenDays(@RequestParam Map<String, String> allRequestParams, Model model) {
    try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        // 提取并转换参数
        int userId = Integer.parseInt(allRequestParams.get("userId"));
        String roomType = allRequestParams.get("roomType");
        Date checkInDate = formatter.parse(allRequestParams.get("checkInDate"));
        Date checkOutDate = formatter.parse(allRequestParams.get("checkOutDate"));
        double amount = Double.parseDouble(allRequestParams.get("amount"));

        // 构建参数映射
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", userId);
        params.put("roomType", roomType);
        params.put("checkInDate", new java.sql.Date(checkInDate.getTime()));
        params.put("checkOutDate", new java.sql.Date(checkOutDate.getTime()));
        params.put("amount", amount);

        // 调用服务层方法
        roomService.bookRoomInSevenDays(params);

        return "bookingResult"; // 返回预订结果页面
    } catch (ParseException e) {
        model.addAttribute("errorMessage", "Date parsing error: " + e.getMessage());
        return "error"; // 返回错误页面
    } catch (NumberFormatException e) {
        model.addAttribute("errorMessage", "Number format error: " + e.getMessage());
        return "error"; // 返回错误页面
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error: " + e.getMessage());
        return "error"; // 返回错误页面
    }
}




}
