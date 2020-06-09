package redis;

import com.learn.redis.RedisBitmapUtil;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RedisBitmapUtilTest extends BaseTest {

    @Autowired
    private RedisBitmapUtil redisBitmapUtil;

    @Test
    public void setBit() {
        System.out.println(redisBitmapUtil.setBit("bitmap-key", 8, 1));
    }

    @Test
    public void getBit() {
        System.out.println(redisBitmapUtil.getBit("bitmap-key", 8));
    }

    @Test
    public void insertBitmapFilter() {
        RedisBitmapUtil.BloomFilter bloomFilter = redisBitmapUtil.getBloomFilter(10000L, 0.01);
        System.out.println(bloomFilter.getBitmapLength());
        System.out.println(bloomFilter.getNumHashFunctions());

        System.out.println(bloomFilter.insert("topic_read:8839540:20190609", "76930245", 60000));
    }

    @Test
    public void existBitmapFilter() {
        RedisBitmapUtil.BloomFilter bloomFilter = redisBitmapUtil.getBloomFilter(10000L, 0.01);
        System.out.println(bloomFilter.isExist("topic_read:8839540:20190609", "76930242"));
        System.out.println(bloomFilter.isExist("topic_read:8839540:20190609", "76930244"));
        System.out.println(bloomFilter.isExist("topic_read:8839540:20190609", "76930246"));
        System.out.println(bloomFilter.isExist("topic_read:8839540:20190609", "76930245"));
    }
}
