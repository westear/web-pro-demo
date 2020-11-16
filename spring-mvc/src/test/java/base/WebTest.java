package base;

import com.westear.config.RootConfig;
import com.westear.config.WebAppInit;
import com.westear.config.WebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class, WebConfig.class})
public class WebTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void getHandlerMapping() {
        System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));

        RequestMappingHandlerMapping requestMappingHandlerMapping =
                (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (HandlerMethod handlerMethod : handlerMethods.values()) {
            System.out.println(handlerMethod.getBean() + ":" + handlerMethod.getMethod());
        }

        System.out.println();

        BeanNameUrlHandlerMapping beanNameUrlHandlerMapping = (BeanNameUrlHandlerMapping) applicationContext.getBean("beanNameUrlHandlerMapping");
        Map<String, Object> handlerMap = beanNameUrlHandlerMapping.getHandlerMap();
        for (Object bean : handlerMap.values()) {
            System.out.println(bean.getClass().getName());
        }
        for (String path : handlerMap.keySet()) {
            System.out.println(path);
        }
    }
}
