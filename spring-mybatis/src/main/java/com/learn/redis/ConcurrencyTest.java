package com.learn.redis;

import com.learn.config.AppConfig;
import com.learn.entity.City;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发场景下,分布式锁测试
 */
public class ConcurrencyTest {

    private static final int THREAD_COUNT = 500;

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.getEnvironment().setActiveProfiles("aliyun");
        context.refresh();

        RedisStringOps redisStringOps = context.getBean(RedisStringOps.class);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            executor.execute(()->{
                City city = new City();
                city.setCity("广州");
                city.setCountryId(77);
                redisStringOps.setAndExpireIfAbsent(Thread.currentThread().getName(), city, 120);
                System.out.println(redisStringOps.setAndExpireIfAbsent("lock-key", "lock-city", 60));
            });
        }
        executor.shutdown();
    }
}
