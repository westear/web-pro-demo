package demo.aop.cglib;

public class CglibTest {

    public static void main(String[] args) {
        CglibTarget target = (CglibTarget) new CGLIBProxyFactory().createProxyInstance(new CglibTarget());
        target.print();

        ConcreteClassNoInterface proxyObject = (ConcreteClassNoInterface) ConcreteClassCallbackFilter.createProxy();

        /*
            getConcreteMethodA对应CallbackFilter中定义的索引1，在Callback[]数组中使用的过滤为NoOp,因此直接执行了被代理方法。
            getConcreteMethodB对应CallbackFilter中定义的索引0，在Callback[]数组中使用的过滤为MethodInterceptor，因此执行了方法拦截器进行拦截。
            getConcreteMethodFixedValue对应CallbackFilter中定义的索引2，在Callback[]数组中使用的过滤为FixedValue，因此2次赋值128和256的调用其结果均被锁定为返回999。
         */
        System.out.println("*** NoOp Callback ***");
        proxyObject.getConcreteMethodA("abcde");

        System.out.println("*** MethodInterceptor Callback ***");
        proxyObject.getConcreteMethodB(1);

        System.out.println("*** FixedValue Callback ***");
        int fixed1=proxyObject.getConcreteMethodFixedValue(128);
        System.out.println("fixedValue1:"+fixed1);

        int fixed2=proxyObject.getConcreteMethodFixedValue(256);
        System.out.println("fixedValue2:"+fixed2);
    }
}
