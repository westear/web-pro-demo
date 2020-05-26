package com.learn.origin;

import com.learn.entity.City;
import com.learn.mapper.CityMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlOriginMybatisTest {

    public static void main(String[] args) throws IOException {
        String resource = "org/mybatis/example/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try(SqlSession session = sqlSessionFactory.openSession()) {
            CityMapper cityMapper = session.getMapper(CityMapper.class);
            List<City> cityList = cityMapper.query(0,3);
            System.out.println(cityList);
            System.out.println(cityList);
            System.out.println(cityList);

            System.out.println(cityMapper.queryById(2));
        }
    }
}
