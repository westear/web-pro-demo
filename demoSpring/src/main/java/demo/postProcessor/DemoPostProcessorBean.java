package demo.postProcessor;

public class DemoPostProcessorBean {

    static {
        System.out.println("static " + DemoPostProcessorBean.class.getSimpleName());
    }

    private Integer number;

    public DemoPostProcessorBean() {
        number = 5;
        System.out.println("construct DemoPostProcessorBean number:" + number);
    }

    public void print() {
        System.out.println("print " + DemoPostProcessorBean.class.getSimpleName() + " number: " + number);
    }

    public Integer getNumber() {
        return number;
    }
}
