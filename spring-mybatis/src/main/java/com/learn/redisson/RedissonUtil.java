package com.learn.redisson;

import org.redisson.RedissonLock;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class RedissonUtil {

    private RedissonClient redisson;

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redisson = redissonClient;
    }

    public void simpleSet(String key, String value) {
        RBucket<String> keyObject = redisson.getBucket(key);
        if(!keyObject.isExists()) {
            keyObject.set(value);
        }
    }

    public String getValue(String key) {
        RBucket<String> keyObject = redisson.getBucket(key);
        return keyObject.get();
    }

    /**
     * 等待时间内尝试获取锁，获取到锁后进行上锁，到达过期时间后自动释放锁
     * @param lockKey 锁的key
     * @param timeUnit 时间单位
     * @param waitTime 尝试获取锁的等待时间
     * @param leaseTime 过期时间/续约时间
     * @return boolean
     */
    public boolean tryLock(String lockKey, TimeUnit timeUnit, int waitTime, int leaseTime) {
        RLock lock = redisson.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            System.out.println("获取锁："+lockKey+"失败");
            return false;
        }
    }

    public void doLock(String key) {
        RLock lock = redisson.getLock(key);
        lock.lock();    //lock()方法不传递任何参数，会去校验当前任务是否执行结束，如果没有执行结束，那么相应的就会延长锁的过期时间
        //使用 lock(long leaseTime, TimeUnit unit) 设置过期时间的，因为并不会去检查任务是否执行结束
        // 如果任务还没执行结束，然后锁的过期时间到了，线程中断，就会出现异常
    }

    public void unlock(String key) {
        RLock lock = redisson.getLock(key);
        lock.unlock();
    }

    public void multiTask() throws InterruptedException {
        int threadCount = 20;
        CountDownLatch count = new CountDownLatch(threadCount);
        ExecutorService execute = Executors.newFixedThreadPool(10);
        for (int i = 0; i < threadCount; i++) {
            execute.execute(() -> {
                try {
                    if(tryLock("lockKey", TimeUnit.SECONDS, 3, 30)) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " start ==========");
                            TimeUnit.SECONDS.sleep(2);
                            System.out.println(Thread.currentThread().getName() + " end ==========");
                        } catch (Exception e) {
                            System.out.println(Thread.currentThread().getName() + " error: " + e.getMessage());
                        }finally {
                            unlock("lockKey");
                        }
                    }else {
                        System.out.println(Thread.currentThread().getName() + " 无法获取锁");
                    }
                }catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + " 尝试获取锁失败: " + e.getMessage());
                }finally {
                    count.countDown();
                }
            });
        }
        count.await();
        System.out.println("======= multiTask 任务结束 =========");
    }

    public RedissonMultiLock getMultiLock(String... keys) {
        RLock[] rLocks = new RLock[keys.length];
        for (int i = 0; i < rLocks.length; i++) {
            RLock rLock = redisson.getLock(keys[i]);
            rLocks[i] = rLock;
        }
        return new RedissonMultiLock(rLocks);
    }

    /**
     * 几个锁会被当作一组锁，进行加锁和释放锁，可以设置过期时间，在过期时间到来时，所有的锁都会被主动释放，
     * 这可以预防，因为服务的崩溃，导致锁hang住不释放的情况
     * @param timeUnit
     * @param waitTime
     * @param leaseTime
     * @param keys
     * @return boolean
     */
    public boolean tryMultiLock(TimeUnit timeUnit, long waitTime, long leaseTime, String... keys) {
        RedissonMultiLock multiLock = getMultiLock(keys);
        try {
            return multiLock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
            return false;
        }
    }
}

