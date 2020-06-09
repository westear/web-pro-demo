package com.learn.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.entity.City;
import com.learn.mapper.CityMapper;
import com.learn.redis.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class CityService {

    private CityMapper cityMapper;

    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    public void setCityMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    @Autowired
    public void setRedisTemplateUtil(RedisTemplateUtil redisTemplateUtil) {
        this.redisTemplateUtil = redisTemplateUtil;
    }

    public List<City> query(Integer startNo, Integer pageSize) {
        return cityMapper.query(startNo, pageSize);
    }

    public City queryById(Integer cityId) {
        org.apache.ibatis.logging.LogFactory.useJdkLogging();
        Object obj = redisTemplateUtil.get("cityId:" + cityId);
        if(Objects.isNull(obj)){
            List<City> list = cityMapper.queryById(cityId);
            if(list.isEmpty()) {
                return null;
            }
            City city = list.get(0);
            redisTemplateUtil.setObj("cityId:"+cityId, city);
            return city;
        }else {
            return (City) obj;
        }
    }

    /**
     * 更新数据，缓存延迟双删（懒加载:更新数据并不自动设置缓存，等待读取设置缓存）
     * @param city
     * @param cityId
     * @return
     */
    public boolean update(String city, Integer cityId){
        //删除缓存
        Boolean del = redisTemplateUtil.del("cityId:" + cityId);
        if (!Boolean.TRUE.equals(del)){
            //todo 删除失败， 放入失败处理队列
        }
        //DB 更新
        Integer dbUpdate = cityMapper.update(city, cityId);
        //延迟双删,延迟500毫秒再二次删除
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        del = redisTemplateUtil.del("cityId:1" + cityId);
        if(!Boolean.TRUE.equals(del)){
            //todo 双删失败，将需要删除的key发送至消息队列；
        }
        return dbUpdate > 0;
        //todo 后续自己消费消息，获得需要删除的key；继续重试删除操作，直到成功。
    }

    /**
     * 更新数据，异步延迟双删（懒加载:更新数据并不自动设置缓存，等待读取设置缓存）
     * @param city
     * @return
     */
    public boolean update(City city){
        Integer cityId = city.getCityId();
        //删除缓存
        Boolean del = redisTemplateUtil.del("cityId:" + cityId);
        if (!Boolean.TRUE.equals(del)){
            //todo 删除失败， 放入失败处理队列
        }
        //DB 更新
        Integer update = cityMapper.updateObj(city);
        //触发异步写入串行化mq
        new Thread(()->{
            //todo 写入串行化mq, 也可以采取一种key+version的分布式锁
        }).start();
        return update > 0;
        //todo 后续等待 mq接受消息再次删除缓存
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
