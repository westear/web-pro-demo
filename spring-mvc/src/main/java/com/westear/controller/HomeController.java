package com.westear.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {

    @RequestMapping(value = {"/homepage"}, method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    /**
     * Restful 方式请求
     * 如果方法参数名碰巧与占位符的名称相同，则可以省略 @PathVariable 的 value 属性
     * @param itemId 资源id
     * @return String
     */
    //不要把uri请求参数放在第一级，否则影响其他类型的控制器根据url进行映射。
    // 比如：value={"/{itemId}"}, 会影响实现了 Controller 接口等其他的控制器以"/"开头命名的路径进行映射
    @RequestMapping(value = {"/page/{itemId}"}, method = RequestMethod.GET)
    public String itemPage(@PathVariable("itemId") long itemId){
        return String.valueOf(itemId);
    }

    @RequestMapping(value = "/watchStopTest", method = RequestMethod.GET)
    public String slowlyRequest() {
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e) {
            System.out.println("thread sleep 800ms exception");
        }
        return "request finish";
    }

    @RequestMapping(value = "/watchStopTestV2", method = RequestMethod.GET)
    public String watchStopTestV2() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("thread sleep 1000ms exception");
        }
        return "watchStopTestV2 request finish";
    }
}
