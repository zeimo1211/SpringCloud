//RoomAvailability类
package com.example.springcloud_provider.bean;
import java.util.Date;

public class RoomAvailability {
    private Date date;
    private String roomType;
    private int availableCount;

    // Constructors, Getters, and Setters
    public RoomAvailability() {
    }
    public RoomAvailability(Date date, String roomType, int availableCount) {
        this.date = date;
        this.roomType = roomType;
        this.availableCount = availableCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }
}
