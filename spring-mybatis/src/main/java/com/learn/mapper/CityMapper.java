package com.learn.mapper;

import com.learn.entity.City;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

@Mapper
@CacheNamespace  //二级缓存
public interface CityMapper {

    @Select("select * from city limit #{startNo}, #{pageSize}")
    @Results(id="cityMap", value = {
            @Result(column = "city_id", property = "cityId", jdbcType = JdbcType.SMALLINT, id = true),
            @Result(column = "city", property = "city", jdbcType = JdbcType.VARCHAR),
            @Result(column = "country_id", property = "countryId", jdbcType = JdbcType.SMALLINT),
            @Result(column = "last_update", property = "lastUpdate", jdbcType = JdbcType.TIMESTAMP)
    })
    List<City> query(@Param("startNo") Integer startNo, @Param("pageSize") Integer pageSize);

    @Select("select * from city where city_id=#{cityId}")
    @ResultMap(value = "cityMap")
    List<City> queryById(@Param("cityId") Integer cityId);

    @Update("update city set city=#{city}, last_update=now() where city_id=#{cityId}")
    Integer update(@Param("city") String city, @Param("cityId") Integer cityId);

    /**
     * update 的方法名不能相同
     * @param city 对象实例
     * @return 操作数
     */
    @Update("update city set city_id=#{city.cityId}, city=#{city.city}, country_id=#{city.countryId}, last_update=now() where city_id=#{city.cityId}")
    Integer updateObj(@Param("city") City city);

    @Insert("insert into city (city, country_id, last_update) value (#{city.city}, #{city.countryId}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "city.cityId")
    Integer insert(@Param("city") City city);

    @Delete("delete from city where city_id=#{cityId}")
    Integer deleteById(@Param("cityId") Integer cityId);

    @Delete("delete from city where city=#{city}")
    Integer deleteByName(@Param("city") String city);
}
