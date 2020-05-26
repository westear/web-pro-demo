package demo.aop.cglib;

import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * 实现过滤器CallbackFilter接口的类
 */
public class ConcreteClassCallbackFilter implements CallbackFilter {

    /**
     * 方法回调过滤器
     * @param method 被代理的方法
     * @return return值为被代理类的各个方法在回调数组Callback[]中的位置索引。
     */
    @Override
    public int accept(Method method) {
        if("getConcreteMethodB".equals(method.getName())){
            //在Callback[]数组中使用的过滤为MethodInterceptor，因此执行了方法拦截器进行拦截。
            return 0;//Callback callbacks[0]
        }else if("getConcreteMethodA".equals(method.getName())){
            //在Callback[]数组中使用的过滤为NoOp,因此直接执行了被代理方法
            return 1;//Callback callbacks[1]
        }else if("getConcreteMethodFixedValue".equals(method.getName())){
            //在Callback[]数组中使用的过滤为FixedValue，因此无论怎么赋值, 调用其结果均被锁定为返回999
            return 2;//Callback callbacks[2]
        }
        return 1;
    }

    public static Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteClassNoInterface.class);
        CallbackFilter filter = new ConcreteClassCallbackFilter();
        enhancer.setCallbackFilter(filter);

        //方法拦截器
        Callback interceptor = new ConcreteClassInterceptor();

        //这个NoOp表示no operator，即什么操作也不做，代理类直接调用被代理的方法不进行拦截
        Callback noOp = NoOp.INSTANCE;

        //表示锁定方法返回值，无论被代理类的方法返回什么值，回调方法都返回固定值。
        Callback fixedValue = new ConcreteClassFixedValue();

        Callback[] callbacks=new Callback[]{interceptor,noOp,fixedValue};
        enhancer.setCallbacks(callbacks);

        return (ConcreteClassNoInterface) enhancer.create();
    }

    static class ConcreteClassFixedValue implements FixedValue {
        public Object loadObject() {
            System.out.println("ConcreteClassFixedValue loadObject ...");
            return 999;
        }
    }
}
