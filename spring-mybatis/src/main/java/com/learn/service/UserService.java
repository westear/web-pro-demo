package com.learn.service;

import com.learn.entity.Order;
import com.learn.entity.User;
import com.learn.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserWithOrderByUserId(Integer userId) {
        User user = userMapper.queryUserOrderById(userId);
        System.out.println(user);

        if(Objects.nonNull(user) && Objects.nonNull(user.getUserExtInfo())) {
            System.out.println(user.getUserExtInfo());
        }

        if(Objects.nonNull(user) && Objects.nonNull(user.getOrderList())) {
            List<Order> orderList = user.getOrderList();
            for (Order order : orderList) {
                System.out.println(order);
            }
        }
        return user;
    }
}
