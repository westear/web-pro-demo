package com.learn.entity;

public class Order {

    private Integer orderId;
    private Integer userId;
    private Integer goodsId;
    private Integer goodsAmount;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(Integer goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    @Override
    public String toString() {
        return "Order{" +
                " orderId=" + orderId +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", goodsAmount=" + goodsAmount +
                '}';
    }
}
