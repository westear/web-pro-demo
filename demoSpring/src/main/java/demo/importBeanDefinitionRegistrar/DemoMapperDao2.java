package demo.importBeanDefinitionRegistrar;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 模拟 mybatis-spring 的 Mapper 接口
 */
@Mapper
public interface DemoMapperDao2 {

    @Select("select data2 from table2 limit #{startNo},#{pageSize}")
    List<String> selectCount(@Param("startNo") Integer startNo, @Param("pageSize") Integer pageSize);
}
