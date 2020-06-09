package com.learn.redis;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.commands.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 位图 bitmap 操作： 参考网站：http://www.redis.cn/commands.html
 */
@Component
public class RedisBitmapUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * SETBIT [KEY] [OFFSET] [0|1]
     * 设置二进制位 bitmap offset 位置上的值（0|1）
     * @param key
     * @param offset
     * @param value false=0, true=1
     * @return 返回旧值（0=false, 1=true)
     */
    public boolean setBit(String key, long offset, int value) {
        if(value == 0) {
            redisTemplate.opsForValue().setBit(key, offset, false);
        }else if (value == 1) {
            redisTemplate.opsForValue().setBit(key, offset, true);
        }else {
            throw new IllegalArgumentException("参数 value 的值只能是0或者1");
        }
        return true;
    }

    /**
     * GETBIT [KEY] [OFFSET]
     * 返回key对应的string在offset处的bit值 当offset超出了字符串长度的时候，这个字符串就被假定为由0比特填充的连续空间。
     * 当key不存在的时候，它就认为是一个空字符串，所以offset总是超出范围，然后value也被认为是由0比特填充的连续空间。
     * @param key
     * @param offset
     * @return 在offset处的bit值
     */
    public int getBit(String key, long offset) {
        Boolean bit = redisTemplate.opsForValue().getBit(key, offset);
        if(Boolean.TRUE.equals(bit)) {
            return 1;
        }else if(Boolean.FALSE.equals(bit)) {
            return 0;
        }else {
            throw new RuntimeException("设置失败");
        }
    }


    /**
     * ========= 布隆过滤器 ==============
     * bitmapLength = -(numApproxElements * ln fpp)/(ln2^ln2)
     * hashFunctionCount = (m/n)*ln2
     * numApproxElements = 预计插入长度; fpp = 误差率; hashFunctionCount = hash函数个数; bitmapLength = bitmap长度
     */
    public class BloomFilter {

        /**
         * bitmap 键值的前缀
         */
        private static final String BF_KEY_PREFIX = "bm:";

        /**
         * 预估元素数量
         */
        private long numApproxElements;

        /**
         * 可接受最大误差（假阳性率）
         */
        private double fpp;

        /**
         * 哈希函数个数
         */
        private int numHashFunctions;

        /**
         * Bitmap长度
         */
        private int bitmapLength;

        public BloomFilter(long numApproxElements, double fpp) {
            this.numApproxElements = numApproxElements;
            this.fpp = fpp;
            bitmapLength = (int) (-numApproxElements * Math.log(fpp) / (Math.log(2) * Math.log(2)));
            numHashFunctions = (int) Math.max(1, Math.round((double) bitmapLength / numApproxElements * Math.log(2)));
        }

        public int getBitmapLength() {
            return bitmapLength;
        }

        public int getNumHashFunctions() {
            return numHashFunctions;
        }

        /**
         * 计算一个元素值哈希后映射到Bitmap的哪些bit上。借鉴了Guava的BloomFilterStrategies实现，采用MurmurHash和双重哈希进行散列
         *
         * @param element 元素值
         * @return bit下标的数组
         */
        private long[] getBitIndices(String element) {
            long[] indices = new long[numHashFunctions];
            byte[] bytes = Hashing.murmur3_128()
                    .hashObject(element, Funnels.stringFunnel(StandardCharsets.UTF_8)).asBytes();

            long hash1 = Longs.fromBytes(
                    bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]
            );
            long hash2 = Longs.fromBytes(
                    bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]
            );

            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; i++) {
                indices[i] = (combinedHash & Long.MAX_VALUE) % bitmapLength;
                combinedHash += hash2;
            }
            return indices;
        }

        /**
         * 插入元素
         *
         * @param key       原始Redis键，会自动加上'bf:'前缀
         * @param element   元素值，字符串类型
         * @param expireSec 过期时间（毫秒）
         */
        public boolean insert(String key, String element, long expireSec) {
            if (key == null || element == null) {
                throw new RuntimeException("键值均不能为空");
            }
            String actualKey = BF_KEY_PREFIX.concat(key);

            Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                connection.openPipeline();
                for (long index : getBitIndices(element)) {
                    connection.setBit(actualKey.getBytes(), index, true);
                }
                connection.pExpire(actualKey.getBytes(), expireSec);
                connection.closePipeline();
                return Boolean.TRUE;
            });
            return Boolean.TRUE.equals(execute);
        }

        /**
         * 检查元素在集合中是否（可能）存在
         *
         * @param key     原始Redis键，会自动加上'bf:'前缀
         * @param element 元素值，字符串类型
         */
        public boolean isExist(String key, String element) {
            if (key == null || element == null) {
                throw new RuntimeException("键值均不能为空");
            }

            String actualKey = BF_KEY_PREFIX.concat(key);

            Boolean execute = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                connection.openPipeline();
                for (long index : getBitIndices(element)) {
                    Boolean bit = redisTemplate.opsForValue().getBit(actualKey, index);
                    if(Boolean.FALSE.equals(bit)) {
                        connection.closePipeline();
                        return false;
                    }
                }
                connection.closePipeline();
                return true;
            });

            return Boolean.TRUE.equals(execute);
        }
    }

    public BloomFilter getBloomFilter(long numApproxElements, double fpp) {
        return new BloomFilter(numApproxElements, fpp);
    }

}
