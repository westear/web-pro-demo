package demo;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DemoTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //设置 profile=test的配置方案, 不设置就创建没有被@Profile声明的bean
        context.getEnvironment().setActiveProfiles("test");
        context.register(SpringConfiguration.class);
        context.refresh();

        try {
//            DataSource dataSource = (DataSource) context.getBean("testDataSource");
            SqlSessionFactoryBean sqlSessionFactoryBean = context.getBean(SqlSessionFactoryBean.class);
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            DriverManagerDataSource dataSource = (DriverManagerDataSource) sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
            String dbUrl = dataSource.getUrl();
            System.out.println(dbUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
