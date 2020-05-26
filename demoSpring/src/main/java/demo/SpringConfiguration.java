package demo;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"demo"}
        ,excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "demo.nonScan.*")}
    )
@ImportResource("classpath:spring-demo.xml")
//@EnableAspectJAutoProxy(exposeProxy=true)表示通过aop框架暴露该代理对象，aopContext能够访问.
//@EnableAspectJAutoProxy(proxyTargetClass=true),表示直接对目标类使用CGLib进行代理. 默认=false,对目标接口使用JDK动态代理
@EnableAspectJAutoProxy
@PropertySource(value = "classpath:db.properties")
public class SpringConfiguration {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    //使用注解方式注入 mybatis sqlSessionFactory, 需要引入 maven: mybatis-spring
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource(){     //方法名=bean id
        //DriverManagerDataSource 其实是 spring.jdbc 模块实现了 DataSource 的接口
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("driverName"));
        driverManagerDataSource.setUrl(env.getProperty("default.dbUrl"));
        driverManagerDataSource.setUsername(env.getProperty("default.username"));
        driverManagerDataSource.setPassword(env.getProperty("default.pwd"));
        return driverManagerDataSource;
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource(){     //方法名=bean id
        //DriverManagerDataSource 其实是 spring.jdbc 模块实现了 DataSource 的接口
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("driverName"));
        driverManagerDataSource.setUrl(env.getProperty("test.dbUrl"));
        driverManagerDataSource.setUsername(env.getProperty("test.username"));
        driverManagerDataSource.setPassword(env.getProperty("test.pwd"));
        return driverManagerDataSource;
    }

    @Bean
    @Profile("dev")
    public DataSource devDataSource(){      //方法名=bean id
        //DriverManagerDataSource 其实是 spring.jdbc 模块实现了 DataSource 的接口
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(env.getProperty("driverName"));
        driverManagerDataSource.setUrl(env.getProperty("dev.dbUrl"));
        driverManagerDataSource.setUsername(env.getProperty("dev.username"));
        driverManagerDataSource.setPassword(env.getProperty("dev.pwd"));
        return driverManagerDataSource;
    }

}
