package demo.factoryBean;

public class PluginFactory {

    private String value;

    private Integer count;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void print() {
        System.out.println(this.getClass().getSimpleName()+" value=" + this.value + "; count=" + this.count + ";");
    }
}
