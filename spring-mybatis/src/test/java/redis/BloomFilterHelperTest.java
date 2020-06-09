package redis;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import com.learn.redis.BloomFilterHelper;
import com.learn.redis.RedisBitmapUtil;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class BloomFilterHelperTest extends BaseTest {

    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;

    @Test
    public void add() {
        bloomFilterHelper.setFunnel((Funnel<String>) (from, into) -> {
            into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8);
        });
        bloomFilterHelper.setBitSize(100,0.01);
        bloomFilterHelper.setNumHashFunctions(100);

        bloomFilterHelper.addByBloomFilter("topic_read:8839540:20190609", "76930245");
    }

    @Test
    public void exist() {
        bloomFilterHelper.setFunnel((Funnel<String>) (from, into) -> {
            into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8);
        });
        bloomFilterHelper.setBitSize(100,0.01);
        bloomFilterHelper.setNumHashFunctions(100);

        System.out.println(bloomFilterHelper.includeByBloomFilter("topic_read:8839540:20190609", "76930242"));
        System.out.println(bloomFilterHelper.includeByBloomFilter("topic_read:8839540:20190609", "76930244"));
        System.out.println(bloomFilterHelper.includeByBloomFilter("topic_read:8839540:20190609", "76930246"));
        System.out.println(bloomFilterHelper.includeByBloomFilter("topic_read:8839540:20190609", "76930245"));
    }
}
