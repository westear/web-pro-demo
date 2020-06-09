package com.learn.entity;

import java.util.List;

public class User {

    private Integer userId;
    private String username;
    private String password;
    private String tel;
    private String address;
    private List<Order> orderList;
    private UserExtInfo userExtInfo;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public UserExtInfo getUserExtInfo() {
        return userExtInfo;
    }

    public void setUserExtInfo(UserExtInfo userExtInfo) {
        this.userExtInfo = userExtInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", orderList=" + orderList +
                ", userExtInfo=" + userExtInfo +
                '}';
    }
}
