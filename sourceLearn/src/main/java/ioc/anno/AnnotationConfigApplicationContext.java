package ioc.anno;

import java.io.File;

/**
 * 模拟 annotationContext 根据配置类扫描上下文，并实例化注解声明的bean
 */
public class AnnotationConfigApplicationContext {

    public void scan(String basePackage){
        String rootPath = this.getClass().getResource("/").getPath();
        System.out.println("rootPath=" + rootPath);

        String  basePackagePath =basePackage.replaceAll("\\.","/");
        System.out.println("basePackagePath=" + basePackagePath);

        String filePath = rootPath+basePackagePath;
        System.out.println("filePath=" + filePath);

        File file = new File(filePath);
        String[] names =file.list();

        assert names != null;

        for (String name : names) {
            name=name.replaceAll(".class","");
            try {
                Class<?> clazz =  Class.forName(basePackage+"."+name);

                if(clazz.isAnnotationPresent(Component.class)){
                    Component component = clazz.getAnnotation(Component.class);
                    System.out.println(component.value());
                    System.out.println(clazz.newInstance());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ioc.service");
    }
}
