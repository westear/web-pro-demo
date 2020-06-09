package com.learn.mapper;

import com.learn.entity.UserExtInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface UserExtInfoMapper {

    @Select("select user_ext_id, user_id, sex, age, username from tb_extinfo where user_id = #{userId} and username = #{username}")
    @Results(id = "userExtInfoResult", value = {
            @Result(column = "user_ext_id", property = "userExtId", jdbcType = JdbcType.SMALLINT, id = true),
            @Result(column = "user_id", property = "userId", jdbcType = JdbcType.SMALLINT),
            @Result(column = "sex", property = "sex", jdbcType = JdbcType.TINYINT),
            @Result(column = "age", property = "age", jdbcType = JdbcType.TINYINT),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR)
    })
    UserExtInfo queryByUserId(@Param("userId") Integer userId, @Param("username") String username);
}
