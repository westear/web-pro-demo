package com.spring.demo.lookup;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class LookupTestPrototypeSub1 extends LookupTestPrototype {

}
