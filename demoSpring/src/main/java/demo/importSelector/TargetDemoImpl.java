package demo.importSelector;

public class TargetDemoImpl implements TargetDemo {

    @Override
    public void print() {
        System.out.println(TargetDemoImpl.class.getName());
    }
}
