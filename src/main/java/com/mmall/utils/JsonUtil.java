package com.mmall.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mmall.common.CommonDemo;
import org.apache.poi.ss.formula.functions.T;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyunchan
 * @date Created in 下午3:36 2018/10/24
 * @Modified By
 */

public class JsonUtil {

    public static String toJsonString(Object object){
        return JSONObject.toJSONString(object,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse);
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
    }
}
