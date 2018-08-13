package com.mmall.utils.excel.demo;

import java.util.concurrent.CountDownLatch;

/**
 * @author Qinyunchan
 * @date Created in 下午2:52 2018/8/13
 * @Modified By
 */

public class Employee implements Runnable {

    private CountDownLatch latch;
    private int employeeIndex;

    public Employee(CountDownLatch latch,int employeeIndex){
        this.latch = latch;
        this.employeeIndex = employeeIndex;
    }

    @Override
    public void run() {
        try {
            System.out.println("员工："+employeeIndex+"，正在前往公司大门口集合...");
            Thread.sleep(10);
            System.out.println("员工："+employeeIndex+"，已到达。");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //当前计算工作已结束，计数器减一
            latch.countDown();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //执行coutDown()之后，继续执行自己的工作，不受主线程的影响
            System.out.println("员工："+employeeIndex+"，吃饭、喝水、拍照。");
        }
    }
}
