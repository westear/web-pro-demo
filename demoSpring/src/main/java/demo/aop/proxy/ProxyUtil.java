package demo.aop.proxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 模拟jdk proxy 动态代理部分功能
 */
public class ProxyUtil {

    /**
     *  content --->string
     *  .java  io
     * .class
     * .new   反射----》class
     * @return
     */
    public static Object newInstance(Object target){
        Object proxy=null;

        //假设 目标类 target implements Interface;
        Class targetInf = target.getClass().getInterfaces()[0];
        Method methods[] =targetInf.getDeclaredMethods();
        String line="\n";
        String tab ="\t";
        String infName = targetInf.getSimpleName();
        String content ="";
        //Proxy 的包名写固定值
        String packageContent = "package demo.aop.proxy;"+line;
        String importContent = "import "+targetInf.getName()+";"+line;
        String clazzFirstLineContent = "public class $Proxy implements "+infName+"{"+line;
        String filedContent  =tab+"private "+infName+" target;"+line;
        String constructorContent =tab+"public $Proxy ("+infName+" target){" +line
                +tab+tab+"this.target =target;"
                +line+tab+"}"+line;
        String methodContent = "";
        for (Method method : methods) {
            String returnTypeName = method.getReturnType().getSimpleName();
            String methodName =method.getName();
            // String.class String.class
            Class args[] = method.getParameterTypes();
            String argsContent = "";
            String paramsContent="";
            int flag =0;
            for (Class arg : args) {
                String temp = arg.getSimpleName();
                //String
                //String p0,Sting p1,
                argsContent+=temp+" p"+flag+",";
                paramsContent+="p"+flag+",";
                flag++;
            }
            if (argsContent.length()>0){
                argsContent=argsContent.substring(0,argsContent.lastIndexOf(",")-1);
                paramsContent=paramsContent.substring(0,paramsContent.lastIndexOf(",")-1);
            }

            methodContent+=tab+"public "+returnTypeName+" "+methodName+"("+argsContent+") {"+line
                    +tab+tab+"System.out.println(\"log\");"+line
                    +tab+tab;
                    if(!"void".equals(returnTypeName)){
                        methodContent += "return";
                    }
                    methodContent += " target."+methodName+"("+paramsContent+");"+line
                    +tab+"}"+line;

        }

        content=packageContent + importContent + clazzFirstLineContent + filedContent + constructorContent + methodContent+"}";

        File file =new File("D:\\IdeaProjects\\mmall_learning\\demoSpring\\src\\main\\java\\demo\\aop\\proxy\\$Proxy.java");
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
            Class clazz = urlClassLoader.loadClass("demo.aop.proxy.$Proxy");

            Constructor constructor = clazz.getConstructor(targetInf);

            proxy = constructor.newInstance(target);
            //clazz.newInstance();
            //Class.forName()
        }catch (Exception e){
            e.printStackTrace();
        }

        /**
         * public UserDaoLog(UserDao target){
         * 		this.target =target;
         *
         *        }
         */
        return proxy;
    }
}
