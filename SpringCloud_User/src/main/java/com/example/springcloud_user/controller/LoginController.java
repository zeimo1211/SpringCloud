// LoginController类
package com.example.springcloud_user.controller;

import com.example.springcloud_provider.bean.RoomAvailability;
import com.example.springcloud_provider.bean.RoomBean;
import com.example.springcloud_provider.bean.RoomTypeCount;
import com.example.springcloud_provider.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private RestTemplate restTemplate;

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
        String url = "http://localhost:8541/adminLogin";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<UserBean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, UserBean.class);
        UserBean userBean = responseEntity.getBody();

        if (userBean != null) {
            url = "http://localhost:8541/getAllRooms";
            ResponseEntity<List<RoomBean>> responseEntityRooms = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomBean>>() {});
            List<RoomBean> allRooms = responseEntityRooms.getBody(); // 获取所有房间的信息
            model.addAttribute("rooms", allRooms);
            return "allRoomsDisplay"; // 导航到展示所有房间信息的页面
        }
        return "error"; // 登录失败
    }


    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public String userLogin(String email, String phone, Model model, HttpServletRequest request) {
        String url = "http://localhost:8541/userLogin";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("phone", phone);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<UserBean> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, UserBean.class);
        UserBean userBean = responseEntity.getBody();

        if (userBean != null) {
            request.getSession().setAttribute("userId", userBean.getId());
            url = "http://localhost:8541/getAvailableRooms";
            ResponseEntity<List<RoomBean>> responseEntityRooms = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomBean>>() {});
            List<RoomBean> availableRooms = responseEntityRooms.getBody(); // 获取所有可用房间的信息
            model.addAttribute("rooms", availableRooms);
            return "roomDisplay"; // 用户登录成功，显示可预订房间
        }
        return "error"; // 登录失败
    }

    //处理预订请求
    @RequestMapping(value = "/bookRoom", method = RequestMethod.POST)
    public String bookRoom(@RequestParam Map<String, String> allRequestParams, Model model) {
        String url = "http://localhost:8541/bookRoom";
        Map<String, Object> params = new HashMap<>();
        // 从表单获取预订所需信息
        params.put("p_Name", allRequestParams.get("name"));
        params.put("p_Email", allRequestParams.get("email"));
        params.put("p_Phone", allRequestParams.get("phone"));
        params.put("p_RoomNumber", allRequestParams.get("roomNumber"));
        params.put("p_CheckInDate", allRequestParams.get("checkInDate"));
        params.put("p_CheckOutDate", allRequestParams.get("checkOutDate"));
        params.put("p_Amount", Double.parseDouble(allRequestParams.get("amount"))); // 假设前端提交的金额是字符串

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

        return "bookingResult"; // 返回预订结果页面
    }

    @RequestMapping(value = "/cancelBooking", method = RequestMethod.POST)
    public String cancelBooking(@RequestParam("bookingId") int bookingId, @RequestParam("userId") int userId, Model model) {
        String url = "http://localhost:8541/cancelBooking";
        Map<String, Object> params = new HashMap<>();
        params.put("bookingId", bookingId);
        params.put("userId", userId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String message = responseEntity.getBody();

        model.addAttribute("message", message);
        return "cancelBookingResult"; // 返回取消预订结果页面
    }

    @RequestMapping(value = "/showUserBookings", method = RequestMethod.GET)
    public String showUserBookings(Model model, HttpServletRequest request) {
        int userId = getCurrentUserId(request);
        String url = "http://localhost:8541/getUserBookings?userId=" + userId;

        ResponseEntity<List<RoomBean>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomBean>>() {});
        List<RoomBean> userBookings = responseEntity.getBody();

        model.addAttribute("userBookings", userBookings);
        return "userBookings"; // 返回显示用户预订的页面
    }

    @RequestMapping(value = "/inputUserId", method = RequestMethod.GET)
    public String inputUserId() {
        return "inputUserId"; // 返回输入用户ID的页面
    }

    @RequestMapping(value = "/showBookingsByUserId", method = RequestMethod.POST)
    public String showBookingsByUserId(@RequestParam("userId") int userId, Model model) {
        String url = "http://localhost:8541/getUserBookings?userId=" + userId;

        ResponseEntity<List<RoomBean>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomBean>>() {});
        List<RoomBean> userBookings = responseEntity.getBody();

        model.addAttribute("userBookings", userBookings);
        return "userBookings"; // 返回显示用户预订的页面
    }

    @RequestMapping(value = "/allRooms", method = RequestMethod.GET)
    public String showAllRooms(Model model) {
        String url = "http://localhost:8541/getAllRooms";

        ResponseEntity<List<RoomBean>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomBean>>() {});
        List<RoomBean> allRooms = responseEntity.getBody();

        model.addAttribute("rooms", allRooms);
        return "allRoomsDisplay"; // 新的前端页面显示所有房间信息
    }

    @RequestMapping(value = "/confirmBooking", method = RequestMethod.POST)
    public String confirmBooking(@RequestParam("bookingId") int bookingId, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8541/confirmBooking";
        Map<String, Object> params = new HashMap<>();
        params.put("bookingId", bookingId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String message = responseEntity.getBody();

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/allRooms"; // 确保此处重定向到显示所有房间的正确 URL
    }

    @RequestMapping(value = "/adminCancelBooking", method = RequestMethod.POST)
    public String adminCancelBooking(@RequestParam("bookingId") int bookingId, RedirectAttributes redirectAttributes) {
        String url = "http://localhost:8541/adminCancelBooking";
        Map<String, Object> params = new HashMap<>();
        params.put("bookingId", bookingId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String message = responseEntity.getBody();

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/allRooms"; // 确保此处重定向到显示所有房间的正确 URL
    }

    @RequestMapping(value = "/availableRoomTypes", method = RequestMethod.GET)
    public String showAvailableRoomTypes(Model model, HttpServletRequest request) {
        LocalDate today = LocalDate.now();
        Date startDate = java.sql.Date.valueOf(today);
        Date endDate = java.sql.Date.valueOf(today.plusDays(5));
        String url = "http://localhost:8541/getAvailableRoomTypesInRange?startDate=" + startDate + "&endDate=" + endDate;

        ResponseEntity<List<RoomTypeCount>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomTypeCount>>() {});
        List<RoomTypeCount> roomTypes = responseEntity.getBody();

        model.addAttribute("roomTypes", roomTypes);
        return "availableRoomTypes";
    }




    @RequestMapping(value = "/calculateAndPayRoomCharges", method = RequestMethod.GET)
    public String calculateAndPayRoomCharges(HttpServletRequest request, Model model) {
        int userId = getCurrentUserId(request);
        String url = "http://localhost:8541/calculateAndPayRoomCharges?userId=" + userId;

        ResponseEntity<BigDecimal> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, BigDecimal.class);
        BigDecimal totalCost = responseEntity.getBody();

        model.addAttribute("totalCost", totalCost);
        return "paymentSuccess"; // 跳转到支付成功页面
    }

    // 新的根据客户ID计算费用的方法
    @RequestMapping(value = "/calculateCharges", method = RequestMethod.POST)
    public String calculateCharges(@RequestParam("customerId") int customerId, Model model) {
        String url = "http://localhost:8541/calculateAndPayRoomCharges?customerId=" + customerId;

        ResponseEntity<BigDecimal> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, BigDecimal.class);
        BigDecimal totalCost = responseEntity.getBody();

        model.addAttribute("totalCost", totalCost);
        return "paymentSuccess"; // 跳转到支付成功页面
    }

    @RequestMapping(value = "/inputCustomerId", method = RequestMethod.GET)
    public String inputCustomerId() {
        return "inputCustomerId"; // 返回输入客户ID的页面
    }

    @RequestMapping(value = "/backupDatabase", method = RequestMethod.GET)
    public String backupDatabase(Model model) {
        String url = "http://localhost:8541/backupDatabase";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String message = responseEntity.getBody();

        model.addAttribute("message", message);
        return "backupRestoreResult";
    }

    @RequestMapping(value = "/restoreDatabase", method = RequestMethod.GET)
    public String restoreDatabase(Model model) {
        String url = "http://localhost:8541/restoreDatabase";

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String message = responseEntity.getBody();

        model.addAttribute("message", message);
        return "backupRestoreResult";
    }

    @PostMapping("/bookRoomByType")
    public String bookRoomByTypeAndCustomer(Model model, int customerId, String roomType, String checkInDate, String checkOutDate, double amount) {
        String url = "http://localhost:8541/bookRoomByType";
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("roomType", roomType);
        params.put("checkInDate", checkInDate);
        params.put("checkOutDate", checkOutDate);
        params.put("amount", amount);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        String message = responseEntity.getBody();

        model.addAttribute("message", message);
        return "success"; // 假设一个显示预订确认信息的页面
    }

    @RequestMapping(value = "/displayRoomAvailability", method = RequestMethod.GET)
    public String displayRoomAvailability(Model model) {
        String url = "http://localhost:8541/displayRoomAvailability";

        ResponseEntity<List<RoomAvailability>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<RoomAvailability>>() {});
        List<RoomAvailability> roomAvailabilityList = responseEntity.getBody();

        model.addAttribute("roomAvailability", roomAvailabilityList);
        return "roomAvailabilityDisplay"; // 指向新的 HTML 页面
    }

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

            String url = "http://localhost:8541/bookRoomInSevenDays";
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params);
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);

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
