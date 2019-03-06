package com.mmall.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Qinyunchan
 * @date Created in 下午10:24 2018/6/25
 * @Modified By
 */

public class UtilDemo {

    public static void main(String[] args) {
        String s = "1";
        String[] arr = StringUtils.split(s,",");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        List<String> list = new ArrayList<>();
        list.add("2");
        list.add("8");
        list.add(null);
        String str = String.join(",", list);
        System.out.println(str);

        String doubleStr = "1000.55";
        Long longNum = new Double(Double.parseDouble(doubleStr)).longValue();
        System.out.println(longNum);
    }
}
