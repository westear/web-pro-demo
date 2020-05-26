package demo.importBeanDefinitionRegistrar;

import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MapperProxyInvokerHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Select select = method.getAnnotation(Select.class);
        System.out.println(MapperProxyInvokerHandler.class.getSimpleName()+": "+Arrays.toString(select.value()));
        return null;
    }
}
