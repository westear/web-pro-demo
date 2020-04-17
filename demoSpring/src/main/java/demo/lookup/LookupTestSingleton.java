package demo.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LookupTestSingleton {

    @Autowired
    LookupTestPrototype lookupTestPrototype;

    public void getObjectId() {
        System.out.println("Singleton Object Id:" + this + ", Prototype Object Id : " + lookupTestPrototype);
    }

}
