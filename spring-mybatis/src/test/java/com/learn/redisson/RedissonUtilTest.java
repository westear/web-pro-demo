package com.learn.redisson;

import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



public class RedissonUtilTest extends BaseTest {

    @Autowired
    private RedissonUtil redissonUtil;

    @Test
    public void simpleSet() {
        redissonUtil.simpleSet("strKey", "strValue");
    }

    @Test
    public void getValue() {
        System.out.println(redissonUtil.getValue("lockKey"));
    }

    @Test
    public void multiTask() {
        try {
            redissonUtil.multiTask();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
