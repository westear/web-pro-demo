package com.learn.origin;

import com.learn.mapper.CityMapper;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Objects;
import java.util.Properties;

@PropertySource(value = "classpath:db.properties")
public class MybatisConfig {

    private Configuration configuration;


    public MybatisConfig() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Properties properties = new Properties();
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","utf8");
        properties.setProperty("allowMultiQueries","true");
        properties.setProperty("useSSL","false");
        properties.setProperty("serverTimezone","UTC");

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("qyc514");
        //数据库链接属性: //url?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC
        dataSource.setConnectionProperties(properties);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        this.configuration = new Configuration(environment);
        configuration.addMapper(CityMapper.class);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
