package test;

import entity.City;
import util.DBUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JdbcTest {

    public static void main(String[] args) throws InterruptedException {
//        DBUtil dbUtil = new DBUtil();
//        String insertSql = "insert into city (city,country_id,last_update) value ( '西安', 5, now() )";
//        dbUtil.insert(insertSql);

        //模拟 TRANSACTION_READ_UNCOMMITTED, 更新时休眠5秒，进行查询
        final CountDownLatch count = new CountDownLatch(2);

        Thread updateThread = new Thread(() -> {
            DBUtil dbUtil = new DBUtil();
            String updateSql = "update city set country_id=101 where city='西安'";
            dbUtil.update(updateSql);
            count.countDown();
        });

        Thread queryThread = new Thread(() -> {
            DBUtil dbUtil = new DBUtil();
            City city = dbUtil.query("select 'city',country_id,last_update from city where city_id=603");
            System.out.println(city.toString());
            count.countDown();
        });

        updateThread.start();
        queryThread.start();

        count.await();
        DBUtil dbUtil = new DBUtil();
        City city = dbUtil.query("select 'city',country_id,last_update from city where city_id=603");
        System.out.println(city.toString());
    }
}
