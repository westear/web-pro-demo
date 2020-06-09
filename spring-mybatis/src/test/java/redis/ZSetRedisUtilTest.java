package redis;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.learn.entity.City;
import com.learn.redis.ZSetRedisUtil;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 使用 redisTemplate 操作常用的 redis 命令， 参考网站: http://www.redis.cn/commands.html
 */
public class ZSetRedisUtilTest extends BaseTest {

    @Autowired
    private ZSetRedisUtil zSetRedisUtil;

    @Test
    public void zaddValue() {
        System.out.println(zSetRedisUtil.zadd("zset-key", "zset-member1", 1.1));
        System.out.println(zSetRedisUtil.zadd("zset-key", "zset-member2", 2.2));
        System.out.println(zSetRedisUtil.zadd("zset-key", "zset-member3", 3.3));
    }

    @Test
    public void zaddValues() {
        Map<Object, Double> valueMap = new HashMap<>();
        City city1 = new City();
        city1.setCityId(1000);
        city1.setCity("BEIJING");
        city1.setCountryId(1);
        valueMap.put(city1, 9.9);

        City city2 = new City();
        city2.setCityId(1001);
        city2.setCity("SHANGHAI");
        city2.setCountryId(2);
        valueMap.put(city2, 8.8);

        City city3 = new City();
        city3.setCityId(1003);
        city3.setCity("SHENZHEN");
        city3.setCountryId(3);
        valueMap.put(city3, 7.7);

        System.out.println(zSetRedisUtil.zadd("zset-key", valueMap));
    }

    @Test
    public void zrangeWithScore() {
        List<Map<Object, Double>> list = zSetRedisUtil.zrangeWithScore("zset-key", 0, -1);
        for (Map<Object, Double> map : list) {
            System.out.println(map);
        }
    }

    @Test
    public void zrange() {
        Set<Object> objectSet = zSetRedisUtil.zrange("zset-key", 0, -1);
        for (Object obj : objectSet) {
            System.out.println(obj);
        }
    }

    @Test
    public void zrangeByLex() {
        Set<Object> objectSet = zSetRedisUtil.zrangeByLex("zset", "aa", true,"c", false);
        for (Object member : objectSet) {
            System.out.println(member);
        }
    }
}
