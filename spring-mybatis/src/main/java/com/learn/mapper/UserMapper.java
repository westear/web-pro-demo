package com.learn.mapper;

import com.learn.entity.User;
import com.learn.entity.UserExtInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select(" select user_id, username, password, tel, address from user where user_id=#{userId} ")
    @Results(id="userResult", value = {
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.SMALLINT ,id = true),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tel", property = "tel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "address", property = "address", jdbcType = JdbcType.VARCHAR),
            //一对多
            @Result(column = "user_id", property = "orderList", javaType = List.class,
                    many = @Many(select = "com.learn.mapper.OrderMapper.queryByUserId")),
            //一对一，且 多参数传递
            @Result(column = "{userId=user_id, username=username}",
                    property = "userExtInfo", javaType = UserExtInfo.class,
                    one = @One(select = "com.learn.mapper.UserExtInfoMapper.queryByUserId"))
    })
    User queryUserOrderById(@Param("userId") Integer userId);
}
