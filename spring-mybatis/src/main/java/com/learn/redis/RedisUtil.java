package com.learn.redis;

import redis.clients.jedis.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class RedisUtil {

    /**
     * 默认锁过期时间5秒
     */
    private static final long DEFAULT_EXPIRE_MILLISECONDS = 5 * 1000;

    private Jedis jedis;

    public RedisUtil() {
        this.jedis = jedisPool().getResource();
    }

    private static Properties readProperty(){
        Properties properties = new Properties();
        InputStream inputStream = RedisUtil.class.getResourceAsStream("/redis.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }

    private static JedisPool jedisPool(){
        Properties properties = readProperty();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Integer.parseInt(Objects.requireNonNull(properties.getProperty("redis.maxIdle"))));
        jedisPoolConfig.setMaxTotal(Integer.parseInt(Objects.requireNonNull(properties.getProperty("redis.maxTotal"))));
        jedisPoolConfig.setMaxWaitMillis(Long.parseLong(Objects.requireNonNull(properties.getProperty("redis.maxWaitMillis"))));
        jedisPoolConfig.setTestOnBorrow(Boolean.TRUE);
        jedisPoolConfig.setTestOnReturn(Boolean.TRUE);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(-1L);
        return new JedisPool(
                jedisPoolConfig,
                properties.getProperty("redis.ip"),
                Integer.parseInt(Objects.requireNonNull(properties.getProperty("redis.port"))),
                Integer.parseInt(Objects.requireNonNull(properties.getProperty("redis.timeout"))),
                properties.getProperty("redis.pwd"));
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public String set(String key, String value) {
        return jedis.set(key, value);
    }

    /**
     * key不存在时，设置键值对; 此方法不能设置过期时间，设置键值和设置过期时间不是原子操作
     * @param key
     * @param value
     * @return true | false
     */
    public boolean setNx(String key, String value) {
        Long returnCount = 0L;
        try {
            returnCount = jedis.setnx(key, value);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return returnCount > 0;
    }

    /**
     * 开启事务设置键值对, 可以设置过期时间（秒、毫秒）
     * @param key
     * @param value
     * @param second
     * @param milliseconds
     * @return true | false
     */
    public boolean setMulti(String key, String value, Integer second, Long milliseconds) {
        Response<String> set = null;
        //开启事务
        Transaction multi = jedis.multi();
        try {
            set = multi.set(key, value);
            if(Objects.nonNull(milliseconds)){
                multi.pexpire(key,milliseconds);
            }else if(Objects.nonNull(second)){
                multi.expire(key, second);
            }else {
                multi.pexpire(key, DEFAULT_EXPIRE_MILLISECONDS);
            }
            multi.exec();
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常回滚
            multi.discard();
        }finally {
            jedis.close();
        }
        return Objects.nonNull(set) && !Objects.equals("", set.get());
    }

    /**
     * 不管key值是否存在，只要设置成功，都返回字符串: OK
     * @param key
     * @param value
     * @param second
     * @return 成功返回：OK
     */
    public String setEx(String key , String value, Integer second) {
        try {
            return jedis.setex(key, second, value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return "no item";
    }

    public long del(String key) {
        return jedis.del(key);
    }

}
