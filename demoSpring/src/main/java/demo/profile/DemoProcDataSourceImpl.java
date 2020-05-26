package demo.profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("proc")
public class DemoProcDataSourceImpl implements DemoDataSource {

    @Override
    public String getDataSourceName() {
        System.out.println(this.getClass().getSimpleName()+": proc");
        return "proc";
    }
}
