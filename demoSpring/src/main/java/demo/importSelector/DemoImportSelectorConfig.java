package demo.importSelector;

import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@PropertySource("classpath:selectorDemo.properties")
@EnableSpringStudy
//@EnableTransactionManagement  //开启事务支持
//@Import(ProxyDemoImpl.class)
public class DemoImportSelectorConfig {

    @Bean
    public StudySelectorBean studySelectorBean() {
        StudySelectorBean studySelectorBean = new StudySelectorBean();
        studySelectorBean.setId(10);
        studySelectorBean.setName("Ada");
        return studySelectorBean;
    }
}
