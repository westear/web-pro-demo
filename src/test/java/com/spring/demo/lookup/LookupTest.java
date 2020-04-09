package com.spring.demo.lookup;

import com.spring.demo.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class LookupTest {

    @Autowired
    private LookupTestSingleton lookupTestSingleton;

    @Autowired
    private LookupTestNonAbstractSingleton lookupTestNonAbstractSingleton;

    @Autowired
    private LookupTestAbstractSingleton lookupTestAbstractSingleton;

    @Test
    public void oldTest() {
        for (int i = 1; i <= 10; i++) {
            System.out.print("第"+i+"次调用的结果：");
            lookupTestSingleton.getObjectId();
        }
    }

    @Test
    public void newTest() {
        for (int i = 1; i <= 10; i++) {
            System.out.print("使用了@Lookup注解后，第"+i+"次调用的结果：");
            lookupTestNonAbstractSingleton.getObjectId();
        }
    }

    @Test
    public void abstractClassTest() {
        for (int i = 1; i <= 5; i++) {
            System.out.print("abstract Class/method 使用了@Lookup注解后，第"+i+"次调用的结果：");
            lookupTestAbstractSingleton.getObjectId();
        }
    }
}
