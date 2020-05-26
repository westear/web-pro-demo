package test;

import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CityService;

public class TransactionTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CityService cityService = context.getBean(CityService.class);
        boolean updateResult = cityService.update(
                "insert into country (country, last_update) value ('中国', now())",
                "update city set country_id=110 where city='西安'");
        System.out.println(updateResult);
    }
}
