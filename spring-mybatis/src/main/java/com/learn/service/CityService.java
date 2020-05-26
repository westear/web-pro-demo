package com.learn.service;

import com.learn.entity.City;
import com.learn.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CityService {

    private CityMapper cityMapper;

    @Autowired
    public void setCityMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    public List<City> query(Integer startNo, Integer pageSize) {
        return cityMapper.query(startNo, pageSize);
    }

    public City queryById(Integer cityId) {
        org.apache.ibatis.logging.LogFactory.useJdkLogging();
        List<City> list = cityMapper.queryById(cityId);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public boolean update(String city, Integer cityId){
        return cityMapper.update(city, cityId) > 0;
    }

    public boolean update(City city){
        return cityMapper.updateObj(city) > 0;
    }

    public boolean insert(City city) {
        return cityMapper.insert(city) > 0;
    }

    public boolean deleteById(Integer cityId) {
        return cityMapper.deleteById(cityId) > 0;
    }

    public boolean deleteByName(String city) {
        return cityMapper.deleteByName(city) > 0;
    }
}
