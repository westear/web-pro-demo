package com.learn;

import com.learn.config.AppConfig;
import com.learn.entity.City;
import com.learn.service.CityService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.getEnvironment().setActiveProfiles("aliyun");
        context.refresh();

        CityService cityService = context.getBean(CityService.class);

        cityService.query(0, 5);
        cityService.queryById(1);

        System.out.println(cityService.query(0, 5));
        System.out.println(cityService.queryById(1));

//        City city = new City();
//        city.setCity("北京");
//        city.setCountryId(11);
//        System.out.println(cityService.insert(city));

//        City city = cityService.queryById(602);
//        city.setCountryId(88);
//        System.out.println(cityService.update(city));

//        System.out.println(cityService.update("西安", 1));
//        System.out.println(cityService.queryById(1));

//        System.out.println(cityService.deleteById(601));

//        System.out.println(cityService.deleteByName("上海"));
    }
}
