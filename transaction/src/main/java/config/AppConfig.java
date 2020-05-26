package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"entity","dao","service"})
@PropertySource(value = "classpath:db.properties")
@EnableTransactionManagement
public class AppConfig {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("driverName")));
        dataSource.setUrl(env.getProperty("dev.url"));
        dataSource.setUsername(env.getProperty("dev.username"));
        dataSource.setPassword(env.getProperty("dev.pwd"));
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

    /**
     * 事务管理器
     * @return DataSourceTransactionManager
     */
    @Bean
    public DataSourceTransactionManager txManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    /**
     * jdbcTemplate
     * @return
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }
}
