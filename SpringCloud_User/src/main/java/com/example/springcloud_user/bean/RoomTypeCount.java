//RoomTypeCount类
package com.example.springcloud_provider.bean; // 根据您项目的实际包结构更改
public class RoomTypeCount {
    private String roomType;
    private int count;
    private double price; // 新增价格属性
    private double expectedPrice; // new field for expected price

    // 构造函数
    public RoomTypeCount() {
    }

    public RoomTypeCount(String roomType, int count) {
        this.roomType = roomType;
        this.count = count;
    }
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // new getter and setter for expectedPrice
    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }


}
