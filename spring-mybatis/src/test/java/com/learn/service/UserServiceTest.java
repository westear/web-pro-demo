package com.learn.service;


import com.learn.entity.Order;
import com.learn.entity.UserExtInfo;
import com.learn.mapper.OrderMapper;
import com.learn.mapper.UserExtInfoMapper;
import common.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserExtInfoMapper userExtInfoMapper;

    @Test
    public void getUserWithOrderByUserId() {
//        List<Order> orders = orderMapper.queryByUserId(1);
//        for (Order order : orders) {
//            System.out.println(order);
//        }
//        System.out.println(userExtInfoMapper.queryByUserId(1));
        userService.getUserWithOrderByUserId(1);
    }
}
