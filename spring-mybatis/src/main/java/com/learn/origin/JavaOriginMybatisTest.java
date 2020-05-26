package com.learn.origin;

import com.learn.entity.City;
import com.learn.mapper.CityMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JavaOriginMybatisTest {

    public static void main(String[] args) {
        MybatisConfig mybatisConfig = new MybatisConfig();
        Configuration configuration = mybatisConfig.getConfiguration();

        //DefaultSqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        try(SqlSession session = sqlSessionFactory.openSession()) {
            CityMapper cityMapper = session.getMapper(CityMapper.class);
            List<City> cityList = cityMapper.query(0,2);
            System.out.println(cityList);
            System.out.println(cityList);
            System.out.println(cityList);

            System.out.println(cityMapper.queryById(1));
        }
    }
}
