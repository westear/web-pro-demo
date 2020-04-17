package demo.aop.proxy;

import demo.aop.handler.MyDemoInvocationHandler;
import demo.aop.handler.MyInvocationHandler;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * 模拟JDK 动态代理生成代理类
 */
public class JdkDynamicProxyUtil {

    private static final String LINE = "\n";
    private static final String TAB = "\t";

    /**
     * 模拟JDK的方法: sun.misc.ProxyGenerator#generateProxyClass(java.lang.String, java.lang.Class[])
     * @param target 目标类接口
     * @param handler 代理类增强逻辑需要实现的处理接口：参考 @see java.lang.reflect.InvocationHandler
     * @return 生成代理类 固定写成 $ProxyHandler.java
     */
    public static Object generateProxyClass(Class target, MyInvocationHandler handler) {
        Object proxy = null;
        if(Objects.isNull(target) || Objects.isNull(handler)) {
            return proxy;
        }

        if(!target.isInterface()){
            return proxy;
        }
        Method[] targetMethods = target.getDeclaredMethods();
        String targetInterfaceSimpleName = target.getSimpleName();
        String content;
        String packageContent = "package demo.aop.proxy;" + LINE;
        String importContent = "import " + target.getName() + ";" + LINE
                                + "import demo.aop.handler.MyInvocationHandler;" + LINE
                                + "import java.lang.Exception;" + LINE
                                + "import java.lang.Exception;" + LINE
                                + "import java.lang.reflect.Method;" + LINE;
        String clazzFirstLineContent = "public class $ProxyHandler implements "+targetInterfaceSimpleName+"{"+LINE;
        String handlerField  =TAB+"private MyInvocationHandler h;"+LINE;
        String constructContent = TAB + "public $ProxyHandler (MyInvocationHandler h) {" + LINE
                                + TAB+TAB+"this.h = h;"+LINE
                                + TAB+"}"+LINE;
        String methodContent = "";

        for (Method method : targetMethods) {
            String returnTypeName = method.getReturnType().getSimpleName();
            String methodName = method.getName();
            Class[] argsClass = method.getParameterTypes();
            String argsTypeContent = "";
            String argContent = "";
            String paramsContent="";

            int flag = 1;
            for (Class argCls : argsClass) {
                String argType = argCls.getSimpleName();
                argsTypeContent += argType+".class,";
                argContent += argType + " param" + flag + ",";
                paramsContent+="param"+flag+",";
                flag++;
            }
            if(argsClass.length > 0) {
                argsTypeContent = argsTypeContent.substring(0, argsTypeContent.lastIndexOf(","));
                argContent = argContent.substring(0, argContent.lastIndexOf(","));
                paramsContent = paramsContent.substring(0, paramsContent.lastIndexOf(","));
            }
            methodContent += TAB + "public "+returnTypeName+" " + methodName + "("+argContent+") {"+LINE
                            +TAB+TAB+"try {"+LINE
                            +TAB+TAB+TAB+"Method method = Class.forName(\""+target.getName()+"\")"
                                        +".getDeclaredMethod(\""+methodName+"\",new Class[]{"+argsTypeContent+"});"+LINE
                            +TAB+TAB+TAB;
                            if(!"void".equals(returnTypeName)){
                                methodContent += "return ("+returnTypeName+") ";
                            }
                            methodContent += "h.invoke(method, new Object[]{"+paramsContent+"});"+LINE
                            +TAB+TAB+"} catch(Exception e) {"+LINE
                            +TAB+TAB+TAB+"e.printStackTrace();"+LINE
                            +TAB+TAB+"}"+LINE;
                            if(!"void".equals(returnTypeName)){
                                methodContent += TAB+TAB+"return null; "+LINE;
                            }
                            methodContent += TAB+"}"+LINE;
        }

        content = packageContent + importContent + clazzFirstLineContent + handlerField + constructContent + methodContent + "}";

        File file =new File("D:\\IdeaProjects\\mmall_learning\\demoSpring\\src\\main\\java\\demo\\aop\\proxy\\$ProxyHandler.java");
        try {
            if(!file.isDirectory()) {
                if(file.getParentFile().mkdirs() && !file.exists()) {
                    if(!file.createNewFile()){
                        return null;
                    }
                }
            }

            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();


            //将$Proxy.java 编译成 $Proxy.class
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
            Iterable units = fileMgr.getJavaFileObjects(file);

            JavaCompiler.CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
            t.call();
            fileMgr.close();



            //根据 $Proxy.class 生成 代理对象实例
            //继承自SecureClassLoader，支持从jar文件和文件夹中获取class，继承于 classload，加载时首先去 classload 里判断是否由 bootstrap classload 加载过
            URL[] urls = new URL[]{new URL("file:D:\\\\IdeaProjects\\\\mmall_learning\\\\demoSpring\\\\src\\\\main\\\\java\\\\")};
            URLClassLoader urlClassLoader = new URLClassLoader(urls);
            Class clazz = urlClassLoader.loadClass("demo.aop.proxy.$ProxyHandler");

            Constructor constructor = clazz.getConstructor(MyInvocationHandler.class);

            //传入具体的InvocationHandler实现类
            proxy = constructor.newInstance(handler);

        }catch (Exception e){
            e.printStackTrace();
        }

        return proxy;
    }
}
