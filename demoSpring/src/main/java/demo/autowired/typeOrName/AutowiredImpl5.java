package demo.autowired.typeOrName;

import org.springframework.stereotype.Component;

@Component
public class AutowiredImpl5 implements AutowiredInterface {

    @Override
    public void print() {
        System.out.println(this.getClass().getName());
    }
}
