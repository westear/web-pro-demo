package com.learn.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.learn.log.MyJulLogImpl;
import org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.DefaultedRedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.learn.entity","com.learn.service"})
@MapperScan(basePackages = {"com.learn.mapper"})
@PropertySource(value = {"classpath:db.properties"})
@Import(value = {RedisConfig.class,RedissonConfig.class})
public class AppConfig {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    /**
     * 设置 SqlSessionTemplate, 在spring中使用 SqlSessionTemplate，sqlSessionProxy 代理了 SqlSession
     * SqlSessionTemplate 最后会调用 closeSqlSession,所以被spring管理的mybatis是无法使用一级缓存的
     * @see SqlSessionTemplate 方法 SqlSessionInterceptor
     * mybatis一级缓存、二级缓存 https://www.cnblogs.com/happyflyingpig/p/7739749.html
     * @return SqlSessionFactoryBean
     * @throws Exception 异常
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(@Autowired DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        //从1.3.0开始，已经添加了configuration属性。它可以直接指定一个配置实例，而不需要MyBatis XML配置文件
        //Configuration 的值设置，参考 https://mybatis.org/mybatis-3/configuration.html
        org.apache.ibatis.session.Configuration configuration = Objects.requireNonNull(factoryBean.getObject()).getConfiguration();

        /*
            mybatis 日志实现类:
                org.apache.ibatis.logging.slf4j.Slf4jImpl.class         SLF4J 日志接口框架
                org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl.class    JCL 日志接口框架
                org.apache.ibatis.logging.log4j.Log4jImpl.class         对应配置文件 log4j.properties 自由设置打印哪些 sql(推荐)
                org.apache.ibatis.logging.log4j2.Log4j2Impl.class;      对应配置文件 log4j2.xml
                org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl.class  默认info级别以上的信息,所以没有信息
                org.apache.ibatis.logging.stdout.StdOutImpl.class       用这个实现最简单(推荐), 控制台输出 mybatis sql, 不用依赖引入的 log 框架
                org.apache.ibatis.logging.nologging.NoLoggingImpl.class

            项目使用 logback 日志输出时，mybatis 使用 StdOutImpl.class 不冲突， logback 实现了 slf4j 接口
            项目使用 logback 日志输出时，自由设置打印哪些 sql(推荐)
         */
//        configuration.setLogImpl(StdOutImpl.class);

        //自定义扩展 JUL, 修改日志级别
//        configuration.setLogImpl(MyJulLogImpl.class);

        //开启二级缓存
        //mybatis 和 spring 整合时，mybatis 的一级缓存会失效, spring 每次使用代理操作 session 都会清空
        configuration.setCacheEnabled(Boolean.TRUE);

        //使用mybatis懒加载, 默认是false,true表示立即加载,
        // 延迟加载就是懒加载，先去查询主表信息，如果用到从表的数据的话，再去查询从表的信息，也就是如果没用到从表的数据的话，就不查询从表的信息。
        configuration.setLazyLoadingEnabled(false);

        factoryBean.setConfiguration(configuration);
        return factoryBean;
    }

    @Bean
    @Profile("dev")
    public DataSource devDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverName")));
        dataSource.setUrl(env.getProperty("dev.url"));
        dataSource.setUsername(env.getProperty("dev.username"));
        dataSource.setPassword(env.getProperty("dev.pwd"));
        //数据库链接属性: //url?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC
        dataSource.setConnectionProperties(dbConnectProperties());
        return dataSource;
    }

    @Bean
    @Profile("aliyun")
    public DataSource aliyunDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverName")));
        dataSource.setUrl(env.getProperty("aliyun.url"));
        dataSource.setUsername(env.getProperty("aliyun.username"));
        dataSource.setPassword(env.getProperty("aliyun.pwd"));
        //数据库链接属性: //url?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC
        dataSource.setConnectionProperties(dbConnectProperties());
        return dataSource;
    }

    /**
     * 数据库链接属性
     * @return url?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&useSSL=false&serverTimezone=UTC
     */
    @Bean
    public Properties dbConnectProperties() {
        Properties properties = new Properties();
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","utf8");
        properties.setProperty("allowMultiQueries","true");
        properties.setProperty("useSSL","false");
        properties.setProperty("serverTimezone","UTC");
        return properties;
    }


}
