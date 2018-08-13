package com.mmall.utils.excel.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Qinyunchan
 * @date Created in 下午2:50 2018/8/13
 * @Modified By
 */

public class Company {

    public static void main(String[] args) throws InterruptedException {
        //员工数量
        int count = 5;
        //创建计数器
        //构造参数传入的数量值代表的是latch.countDown()调用的次数
        CountDownLatch latch = new CountDownLatch(count);

        //创建线程池，可以通过以下方式创建
        //ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1,1,60,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(count));
        ExecutorService threadPool =  Executors.newFixedThreadPool(count);

        System.out.println("公司发送通知，每一位员工在周六早上8点到公司大门口集合");
        for(int i =0;i<count ;i++){
            //将子线程添加进线程池执行
            Thread.sleep(10);
            threadPool.execute(new Employee(latch,i+1));
        }
        try {
            //阻塞当前线程，直到所有员工到达公司大门口之后才执行
            latch.await();
            // 使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断或超出了指定的等待时间。
            //latch.await(long timeout, TimeUnit unit)
            System.out.println("所有员工已经到达公司大门口，大巴车发动，前往活动目的地。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            //最后关闭线程池，但执行以前提交的任务，不接受新任务
            threadPool.shutdown();
            //关闭线程池，停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。
            //threadPool.shutdownNow();
        }
    }
}
