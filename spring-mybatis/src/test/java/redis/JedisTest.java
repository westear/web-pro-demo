package redis;

import com.learn.config.AppConfig;
import com.learn.redis.RedisUtil;
import common.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisTest extends BaseTest {

    @Test
    public void connect() {
        Jedis jedis = new Jedis("47.115.46.150",6379);
        jedis.auth("aliyun-redis");
        jedis.connect();
        System.out.println(jedis.set("jedis-test", "setValue"));
        System.out.println(jedis.del("jedis-test"));
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
    }

    @Test
    public void redisUtil() {
        RedisUtil redisUtil = new RedisUtil();
//        System.out.println(redisUtil.set("jedis-pool-test", "test-value"));
//        System.out.println(redisUtil.get("jedis-pool-test"));
//        System.out.println(redisUtil.del("jedis-pool-test"));
//        System.out.println(redisUtil.setNx("nx-key", "nx-value"));
//        System.out.println(redisUtil.setMulti("nx-lock", "nx-value", 30, 60000L));
//        System.out.println(redisUtil.set("ex-key", "old-value"));
//        System.out.println(redisUtil.setEx("no-ex-key", "update-value", 20));
    }
}
