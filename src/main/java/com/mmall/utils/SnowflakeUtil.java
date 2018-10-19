package com.mmall.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 雪花算法：分布式UUID生成
 * @author Qinyunchan
 * @date Created in 上午10:44 2018/10/8
 * @Modified By
 */
public class SnowflakeUtil {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeUtil.class);

    private long workerId;
    private long datacenterId;
    private long sequence = 0;

    /**
     * 1bit:符号位，不作处理
     * 41bit:记录时间戳，可以用69年
     * 10bit:记录服务器ID，最多可以记录1024台，一般前5位代表数据中心，后5位代表某个数据中心的机器ID
     * 12bit:循环位，用来对同一个毫秒之内产生不同的ID，12位可以最多记录4095个，也就是在同一个机器同一毫秒最多记录4095个，多余的需要进行等待下毫秒
     */
    /**
     * 2018-10-19 15:31:08 毫秒，13位
     */
    private long twepoch = 1539934268000L;

    private long datacenterIdBits = 5L;
    private long workerIdBits = 5L;
    private long sequenceBits = 12L;

    /**
     * 每台机器产生12位循环位:2^12
     */
    private long workerIdShift = sequenceBits;
    /**
     * 每个数据中心有2^(12+5)
     */
    private long datacenterIdShift = sequenceBits + workerIdBits;
    /**
     * 全部数据中心所有机器一共可存储:2^(12+5+5)
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 同一个机器同一毫秒最多记录4095个
     * 得到0000000000000000000000000000000000000000000000000000111111111111 = 4095
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 同一毫秒生成的时间戳的上一次记录
     */
    private long lastTimestamp = -1L;


    public SnowflakeUtil(long workerId, long datacenterId){
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 生成下一个id
     * @return b4bit long
     */
    public synchronized long nextId() {
        logger.info("lastTimestamp={}",lastTimestamp);
        long timestamp = timeGen();
        //时间回拨，抛出异常
        //当前时间和上一次的时间进行判断，如果当前时间小于上一次的时间那么肯定是发生了回拨

        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        /**
         * 优化：
         *
         * 上面只是一个将64bit划分的标准，当然也不一定这么做，可以根据不同业务的具体场景来划分，比如下面给出一个业务场景：
         *
         * 服务目前QPS10万，预计几年之内会发展到百万。
         *
         * 当前机器三地部署，上海，北京，深圳都有。
         *
         * 当前机器10台左右，预计未来会增加至百台。
         *
         * 这个时候我们根据上面的场景可以再次合理的划分62bit,QPS几年之内会发展到百万，那么每毫秒就是千级的请求，
         *  目前10台机器那么每台机器承担百级的请求，为了保证扩展，后面的循环位可以限制到1024，也就是2^10，那么循环位10位就足够了。
         * 机器三地部署我们可以用3bit总共8来表示机房位置，当前的机器10台，为了保证扩展到百台那么可以用7bit
         *      128来表示，时间位依然是41bit,那么还剩下64-10-3-7-41-1 = 2bit,还剩下2bit可以用来进行扩展。
         *
         * 时间回拨优化：
         * 如果时间回拨时间较短，比如配置5ms以内，那么可以直接等待一定的时间，让机器的时间追上来。
         *
         * 如果时间的回拨时间较长，我们不能接受这么长的阻塞等待，那么又有两个策略:
         *
         * 直接拒绝，抛出异常，打日志，通知RD时钟回滚。
         *
         * 利用扩展位，上面我们讨论过不同业务场景位数可能用不到那么多，那么我们可以把扩展位数利用起来了，
         * 比如当这个时间回拨比较长的时候，我们可以不需要等待，直接在扩展位加1。2位的扩展位允许我们有3次大的时钟回拨，一般来说就够了，
         * 如果其超过三次我们还是选择抛出异常，打日志。
         */

//        if(timestamp < lastTimestamp){
//            if(timestamp - lastTimestamp <= 5){
//                //睡5ms，让机器时间追上
//                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
//                timestamp = timeGen();
//                //如果还小于，那么利用扩展字段加1
//                if(timestamp < lastTimestamp){
//                    //扩展字段+1
//                    extension += 1;
//                    if(extension > maxExtension){
//                        throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
//                                lastTimestamp - timestamp));
//                    }
//                }
//            }
//        }

        //同一毫秒如果生成相同时间戳，序列循环位+1，最多加4095个
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //如果超过4095这循环等待获取下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    /**
     * 当前ms已经满了,重试获取当前时间戳
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获得当前时间戳:13位
     * @return long
     */
    private long timeGen(){
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowflakeUtil worker = new SnowflakeUtil(1,1);
        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId());
        }
    }

}
