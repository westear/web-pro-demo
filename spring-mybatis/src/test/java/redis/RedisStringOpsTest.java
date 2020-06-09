package redis;

import com.learn.entity.City;
import com.learn.redis.RedisStringOps;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class RedisStringOpsTest extends BaseTest {

    @Autowired
    private RedisStringOps redisStringOps;

    @Test
    public void setAndExpireIfAbsent() {
        City city = new City();
        city.setCity("北京");
        city.setCountryId(11);
        System.out.println(redisStringOps.setAndExpireIfAbsent("nx-key", city, 60));
    }

    @Test
    public void concurrency() {
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 1; i <= 100; i++) {
            executor.execute(() -> {
                City city = new City();
                city.setCity("深圳");
                city.setCountryId(99);
                redisStringOps.setAndExpireIfAbsent(Thread.currentThread().getName(), city, 180);
            });
        }
        executor.shutdown();

        while (true) {

        }
    }
}
