package com.learn.entity;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
public class City implements Serializable {

    /**
     * 使用 mybatis 二级缓存需要序列化
     */
    private static final long serialVersionUID = -1850655994100347026L;

    private Integer cityId;

    private String city;

    private Integer countryId;

    private Timestamp lastUpdate;

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

//    @Override
//    public String toString() {
//        return "City{" +
//                "cityId=" + cityId +
//                ", city='" + city + '\'' +
//                ", countryId=" + countryId +
//                ", lastUpdate=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdate.getTime()) +
//                '}';
//    }
}
