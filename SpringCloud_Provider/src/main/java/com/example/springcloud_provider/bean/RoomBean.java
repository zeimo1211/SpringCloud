
//RoomBean类
package com.example.springcloud_provider.bean;
import java.util.Date;
public class RoomBean {
    private int roomId;
    private String roomNumber;
    private String roomType;
    private double price;
    private String status;
    private Date checkInDate;
    private Date checkOutDate;
    private String customerName;
    private int bookingId;

    // Getters
    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    public Date getCheckInDate() {
        return checkInDate;
    }
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }
    public Date getCheckOutDate() {
        return checkOutDate;
    }
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    private int userId;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    private String bookingStatus;
    public String getBookingStatus() {
        return bookingStatus;
    }
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    private String paymentStatus;
    private Date paymentDate;
    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }


}