package demo.autowired.typeOrName;

public class AutowiredImpl3 implements AutowiredInterface {

    @Override
    public void print() {
        System.out.println(this.getClass().getName());
    }
}
