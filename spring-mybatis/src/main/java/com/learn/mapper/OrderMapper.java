package com.learn.mapper;

import com.learn.entity.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("select order_id, goods_id, goods_amount, user_id from tb_order where user_id = #{userId}")
    @Results(id = "orderResult", value = {
            @Result(column = "order_id", property = "orderId", jdbcType = JdbcType.SMALLINT, id = true),
            @Result(column = "goods_id", property = "goodsId", jdbcType = JdbcType.INTEGER),
            @Result(column = "goods_amount", property = "goodsAmount", jdbcType = JdbcType.INTEGER),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.INTEGER)
    })
    List<Order> queryByUserId(@Param("userId") Integer userId);
}
