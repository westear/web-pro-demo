package com.learn.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class LuaScriptService {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private DefaultRedisScript<List> getRedisScript;

    @PostConstruct
    public void init() {
        getRedisScript = new DefaultRedisScript<>();
        getRedisScript.setResultType(List.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/LimitLoadTimes.lua")));
    }

    /**
     * redis-cli -a aliyun-redis --eval LimitLoadTimes.lua "count" "limit" , '{"expire":10000,"times":10}'
     */
    public void redisAddScriptExec(){
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList<>();
        keyList.add("count");
        keyList.add("limit");

        /**
         * 用Map设置Lua的ARGV[1]
         */
        Map<String,Object> argvMap = new HashMap<>();
        argvMap.put("expire",10000);
        argvMap.put("times",10);


        /**
         * 调用脚本并执行
         */
        List result = redisTemplate.execute(getRedisScript,
                new FastJsonRedisSerializer<>(Map.class),
                new FastJsonRedisSerializer<>(List.class),
                keyList, argvMap);
        System.out.println(result);
    }

    class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

        private Class<T> clazz;

        public FastJsonRedisSerializer(Class<T> clazz){
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
               return objectMapper.writeValueAsString(t).getBytes();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//            return Optional.ofNullable(t).map(r->
//                    JSON.toJSONString(r, SerializerFeature.WriteClassName).getBytes(StandardCharsets.UTF_8))
//                    .orElseGet(()->new byte[0]);
            return null;
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return (T) objectMapper.readValue(new String(bytes), new TypeReference<Map<String, Object>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
//            return Optional.ofNullable(bytes)
//                    .map(t -> JSON.parseObject(new String(t, StandardCharsets.UTF_8), clazz))
//                    .orElse(null);
            return null;
        }
    }
}
