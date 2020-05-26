package ioc.dao;

public class ConstructBeanDaoImpl implements ConstructBeanDao {

    @Override
    public void print() {
        System.out.println(this.getClass().getClassLoader());
    }
}
