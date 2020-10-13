package redis;

import com.learn.entity.City;
import com.learn.redis.RedisTemplateUtil;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisTemplateTest extends BaseTest {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Test
    public void existsKey() {
        List<String> list = new ArrayList<>();
        list.add("set-key");
        list.add("cityId:1");
        System.out.println(redisTemplateUtil.existsKey(list));
    }

    @Test
    public void expireKey() {
        System.out.println(redisTemplateUtil.expireKey("set-key", 300L, TimeUnit.SECONDS));
    }

    @Test
    public void ttl() {
        System.out.println(redisTemplateUtil.ttlKey("set-key"));
    }

    @Test
    public void incr() {
        System.out.println(redisTemplateUtil.incr("incr-number"));
    }

    @Test
    public void incrby() {
        System.out.println(redisTemplateUtil.incrBy("key2",7.7));
    }

    @Test
    public void decr() {
        System.out.println(redisTemplateUtil.decr("decr-number"));
    }

    @Test
    public void scan() {
//        Set<String> scan = redisTemplateUtil.scan(50, "pool-1-thread-*");
        Set<String> scan = redisTemplateUtil.scan("pool-1-thread-*", 50, 100);
        System.out.println(scan.size());
        for (String key : scan) {
            System.out.println(key);
        }
    }


    @Test
    public void set() {
        City city = new City();
        city.setCity("上海");
        city.setCountryId(88);
        System.out.println(redisTemplateUtil.setObj("cityId:1", city));
    }

    @Test
    public void get() {
        Object o = redisTemplateUtil.get("cityId:1");
        System.out.println(Objects.nonNull(o) ? o.toString() : "");
    }

    @Test
    public void del() {
        System.out.println(redisTemplateUtil.del("string-key"));
    }

    @Test
    public void setAndGetOldValue() {
        City city = new City();
        city.setCity("上海");
        city.setCountryId(666);
        City newCity = (City) redisTemplateUtil.setAndGetOldValue("cityId:1", city);
        System.out.println(newCity.getCountryId());
    }

    @Test
    public void addSet() {
        System.out.println(redisTemplateUtil.addSet("set-key", "name=ada", 10, 28.8, 10000L, new Date()));
    }

    @Test
    public void getMembers() {
        Set<Object> members = redisTemplateUtil.getMembers("set-key");
        for (Object obj : members) {
            System.out.println(obj);
        }
    }

    @Test
    public void containSet() {
        System.out.println(redisTemplateUtil.containSet("set-key", 10));
        System.out.println(redisTemplateUtil.containSet("set-key", 888));
    }

    @Test
    public void sizeSet() {
        System.out.println(redisTemplateUtil.sizeSet("set-key"));
    }

    @Test
    public void removeValue() {
        System.out.println(redisTemplateUtil.removeValue("set-key", 28.8));
    }

    @Test
    public void randomMembers() {
        System.out.println(redisTemplateUtil.randomMembers("set-key"));
    }

    @Test
    public void randomPop() {
        System.out.println(redisTemplateUtil.randomPop("set-key"));
    }

    @Test
    public void moveValue() {
//        redisTemplateUtil.addSet("sourceKey", "sourceValue", 0, Boolean.TRUE);
//        redisTemplateUtil.addSet("destKey", "destValue",1);
        System.out.println(redisTemplateUtil.moveValue("sourceKey", "destKey", Boolean.TRUE));
    }

    @Test
    public void diffSet() {
        Set<Object> objects = redisTemplateUtil.diffSet("sourceKey", "destKey");
        for (Object obj : objects) {
            System.out.println(obj);
        }
    }

    @Test
    public void intersectSet() {
        Set<Object> objects = redisTemplateUtil.intersectSet("sourceKey", "destKey");
        System.out.println(Objects.isNull(objects));
        for (Object obj : objects) {
            System.out.println(obj);
        }
    }

    @Test
    public void unionSet() {
        Set<Object> objects = redisTemplateUtil.unionSet("sourceKey", "destKey");
        for (Object obj : objects) {
            System.out.println(obj);
        }
    }

    @Test
    public void pushList() {
        System.out.println(redisTemplateUtil.leftPush("list-key", "one", 2, new Date()));
        System.out.println(redisTemplateUtil.rightPush("list-key", 1, "second", new Date()));
    }

    @Test
    public void removeList() {
        System.out.println(redisTemplateUtil.removeList("list-key", -1, "one"));
    }

    @Test
    public void setList() {
        System.out.println(redisTemplateUtil.setList("list-key", 10, "end"));
    }

    @Test
    public void hashSet() {
        Map<String, Object> map = new HashMap<>();
//        map.put("key1", "String");
//        map.put("key2", 2);
//        map.put("key3",new Date());
//        List<Object> list = new ArrayList<>();
//        list.add("list-one");
//        list.add("list-2");
//        map.put("list-key",list);
        for (int i=5; i <= 2000; i++) {
            map.put("key"+i, "value-"+i);
        }
        System.out.println(redisTemplateUtil.hashSet("hash-key", map));
    }

    @Test
    public void hashGet() {
        Map<Object, Object> entries = redisTemplateUtil.hashGet("hash-key");
        for (Map.Entry<Object, Object> entry : Objects.requireNonNull(entries).entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ";");
        }
    }

    @Test
    public void hashGetByMember() {
        System.out.println(redisTemplateUtil.hashGet("hash-key", "key3"));
    }

    @Test
    public void hashIfExist() {
        System.out.println(redisTemplateUtil.hashIfExist("hash-key", "list-key"));
    }

    @Test
    public void hashIncrByFloat() {
        System.out.println(redisTemplateUtil.hashIncrByFloat("hash-key", "key1", 9.9));
    }

    @Test
    public void hashGetKeys() {
        Set<Object> keys = redisTemplateUtil.hashGetKeys("hash-key");
        for (Object key : keys) {
            System.out.println(key);
        }
    }

    @Test
    public void hashScan() {
        List<Map.Entry<String, String>> entryList = redisTemplateUtil.hashScan("hash-key", 2, "key*", 3);
        for (Map.Entry<String, String> entry : entryList) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ";");
        }
    }

    @Test
    public void hScan() {
        Map<Object, Object> map = redisTemplateUtil.hScan("hash-key", 2, "key*", 3);
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + ";");
        }
    }
}
