package com.learn.redis;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 布隆过滤器帮助类
 */
@Component
public class BloomFilterHelper<T> {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private int numHashFunctions;

    private int bitSize;

    private Funnel<T> funnel;

    public void setFunnel(Funnel<T> funnel) {
        this.funnel = funnel;
    }

    public void setBitSize(int expectedInsertions, double fpp) {
        this.bitSize = optimalNumOfBits(expectedInsertions, fpp);
    }

    public void setNumHashFunctions(int expectedInsertions) {
        this.numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }



    /**
     * 计算bit数组长度
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算hash方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    /**
     * 连续hash，计算需要设置 1 的位数
     * @param value 需要计算的对象实例
     * @return 需要设置 1 的位数下标数组
     */
    private int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 根据给定的布隆过滤器添加值
     */
    public void addByBloomFilter(String key, T value) {
        int[] offset = murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     */
    public boolean includeByBloomFilter(String key, T value) {
        int[] offset = murmurHashOffset(value);
        for (int i : offset) {
            if (!Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(key, i))) {
                return false;
            }
        }
        return true;
    }
}
