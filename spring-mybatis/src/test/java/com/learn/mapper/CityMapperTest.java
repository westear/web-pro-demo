package com.learn.mapper;

import com.learn.entity.City;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class CityMapperTest extends BaseTest {

    @Autowired
    private CityMapper cityMapper;

    @Test
    public void insertBatch() {
        List<City> cityList = new ArrayList<>();
        City city = new City();
        city.setCity("杭州");
        city.setCountryId(22);
        cityList.add(city);

        City city1 = new City();
        city1.setCity("苏州");
        city1.setCountryId(23);
        cityList.add(city1);

        City city2 = new City();
        city2.setCity("厦门");
        city2.setCountryId(33);
        cityList.add(city2);

        System.out.println(cityMapper.insertBatch(cityList));
    }
}
