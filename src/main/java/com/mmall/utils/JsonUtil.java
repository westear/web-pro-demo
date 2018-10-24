package com.mmall.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.mmall.common.CommonDemo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyunchan
 * @date Created in 下午3:36 2018/10/24
 * @Modified By
 */
@Slf4j
public class JsonUtil {

    public static String toJsonString(Object object){
        return JSONObject.toJSONString(object,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty);
    }

    public static String toJsonAsString(Object object) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        // 美化输出
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 允许序列化空的POJO类（否则会抛出异常）
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 把java.util.Date, Calendar输出为数字（时间戳）
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 强制JSON 空字符串("")转换为null对象值:
//        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        return objectMapper.writeValueAsString(object);
    }

    public static void main(String[] args) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("a",1);
        jsonMap.put("b","");
        jsonMap.put("c",null);
        jsonMap.put("d","dddd");
        Integer number = null;
        jsonMap.put("e",number);
        List<String> list = null;
        jsonMap.put("f",list);
        String str = null;
        jsonMap.put("g",str);
        Boolean flag = null;
        jsonMap.put("h",flag);

        System.out.println(JSONObject.toJSONString(jsonMap));
        System.out.println(JsonUtil.toJsonString(jsonMap));

        CommonDemo commonDemo = new CommonDemo(1L,"westear",null,null,null);
        System.out.println(JsonUtil.toJsonString(commonDemo));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String classJson = objectMapper.writeValueAsString(commonDemo);
            CommonDemo newCommonDemo = objectMapper.readValue(classJson,CommonDemo.class);
            System.out.println(newCommonDemo.getName());

            //存在同名文件则覆盖
            objectMapper.writeValue(new File("demo.json"),commonDemo);
            //从文件中获取
            CommonDemo newCommonDemo1 = objectMapper.readValue(new File("demo.json"),CommonDemo.class);
            System.out.println(newCommonDemo1.getId());


            String mapJson = JsonUtil.toJsonAsString(jsonMap);
            //集合映射
            Map<String, Object> map1 = objectMapper.readValue(mapJson,new TypeReference<Map<String, Object>>(){});
            System.out.println(map1);

            //处理json字符串
            JsonNode root = objectMapper.readTree(mapJson);
            String d = root.get("d").asText();
            System.out.println("字符串:"+d);
            Integer e = root.get("e").asInt();
            System.out.println("数字(is null,defaultValue = 0):"+e);
        } catch (JsonGenerationException e1){
            log.info("JsonGenerationException:",e1);
        } catch (JsonMappingException e2){
            log.info("JsonMappingException:", e2);
        } catch (JsonProcessingException e3){
            log.info("JsonProcessingException:", e3);
        } catch (IOException e){
            log.info("JsonProcessingException:", e);
        }
    }
}
