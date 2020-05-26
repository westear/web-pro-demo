package demo.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public class DemoDevDataSourceImpl implements DemoDataSource {

    @Override
    public String getDataSourceName() {
        System.out.println(this.getClass().getSimpleName()+": dev");
        return "dev";
    }
}
