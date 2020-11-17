package com.alibaba.entity;

/**
 * 商品模型
 */
public class ItemEntity {

    public ItemEntity() {

    }

    public ItemEntity(Long id, String itemNo, String name, Long price) {
        this.id = id;
        this.itemNo = itemNo;
        this.name = name;
        this.price = price;
    }

    private Long id;

    /**
     * 商品编号
     */
    private String itemNo;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private Long price;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
