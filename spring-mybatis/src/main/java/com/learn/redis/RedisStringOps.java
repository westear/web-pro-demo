package com.learn.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.SafeEncoder;

import java.io.Serializable;
import java.util.concurrent.Semaphore;


/**
 * Redis的setnx命令是当key不存在时设置key，但setnx不能同时完成expire设置失效时长，不能保证setnx和expire的原子性。
 * 我们可以使用set命令完成setnx和expire的操作，并且这种操作是原子操作。
 *
 * set key value [EX seconds] [PX milliseconds] [NX|XX]
 * EX seconds：设置失效时长，单位秒
 * PX milliseconds：设置失效时长，单位毫秒
 * NX：key不存在时设置value，成功返回OK，失败返回(nil)
 * XX：key存在时设置value，成功返回OK，失败返回(nil)
 *
 * 案例：设置name=p7+，失效时长100s，不存在时设置
 * 1.1.1.1:6379> set name p7+ ex 100 nx
 * OK
 * 1.1.1.1:6379> get name
 * "p7+"
 * 1.1.1.1:6379> ttl name
 * (integer) 94
 */
@Component
public class RedisStringOps {

    private static RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    private static RedisSerializer<Object> blobSerializer = new JdkSerializationRedisSerializer();

    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * RedisTemplate 装饰器, 单例
     **/
    private static class RedisTemplateHolder {

        /**
         * 最大有100个redis连接被使用，其他的连接要等待令牌释放
         * 令牌数量自己定义，这个令牌是为了避免高并发下，获取redis连接数时，抛出的异常
         * 在压力测试下，性能也很可观
         */
        private static Semaphore semaphore = new Semaphore(100);

        /**
         * 私有构造器
         */
        private RedisTemplateHolder() {

        }

        public static RedisTemplate<String, Object> getRedisTemplate(RedisTemplate<String,Object> redisTemplate) {
            try {
                semaphore.acquire();
                return redisTemplate;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void release() {
            semaphore.release();
        }

        public static Object execute(Statement statement, RedisTemplate<String, Object> redisTemplate) {
            try {
                return statement.prepare(getRedisTemplate(redisTemplate));
            } finally {
                RedisTemplateHolder.release();
            }
        }
    }

    private interface Statement {
        Object prepare(final RedisTemplate<String, Object> redisTemplate);
    }

    /**
     * 如果key不存在，set key and expire key
     *
     * @param key 键
     * @param value 值
     * @param expire 超时时间
     * @return boolean
     */
    public boolean setAndExpireIfAbsent(final String key, final Serializable value, final long expire) {

        return (Boolean) RedisTemplateHolder.execute(new Statement() {
            @Override
            public Object prepare(RedisTemplate<String, Object> redisTemplate) {
                //重点！！！可以简化成直接使用 redisTemplate.execute
                return redisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        Object obj = connection.execute("set", serialize(key), serialize(value),
                                SafeEncoder.encode("NX"),
                                SafeEncoder.encode("EX"),
                                Protocol.toByteArray(expire));
                        return obj != null;
                    }
                });
            }
        }, redisTemplate);
    }

    private <T> Jackson2JsonRedisSerializer<T> configuredJackson2JsonRedisSerializer(Class<T> clazz) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<T>(clazz);
        ObjectMapper objectMapper = new ObjectMapper();
        // json转实体忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 实体转json忽略null
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    private byte[] serialize(Object object) {
        return serialize(object, SerializeFormat.STRING);
    }

    private byte[] serialize(Object object, SerializeFormat sf) {
        if (object == null) {
            return new byte[0];
        }
        if (sf == SerializeFormat.BLOB) {
            return blobSerializer.serialize(object);
        }
        if (object instanceof String || isPrimitive(object.getClass())) {
            return stringSerializer.serialize(String.valueOf(object));
        } else {
            return configuredJackson2JsonRedisSerializer(object.getClass()).serialize(object);
        }
    }


    /**
     * 工具方法
     * 判定指定的 Class 对象是否表示一个基本类型或者包装器类型
     * @param clazz Class
     * @return boolean
     */
    @SuppressWarnings("rawtypes")
    private static boolean isPrimitive(Class clazz){
        if(clazz.isPrimitive()){
            return true;
        } else
            try {
                if(clazz.getField("TYPE") !=null &&
                        ((Class)(clazz.getField("TYPE").get(null))).isPrimitive()){
                    return true;
                }
            } catch (Exception e) {
            }
        return false;
    }

}
