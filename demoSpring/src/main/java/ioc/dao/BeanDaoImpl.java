package ioc.dao;

public class BeanDaoImpl implements BeanDao {

    @Override
    public void print(String str) {
        System.out.println(this.getClass().getSimpleName()+": "+str);
    }

    @Override
    public String query(String sql, Integer count) {
        System.out.println(this.getClass().getSimpleName()+": "+sql + "; count: " + count);
        return sql;
    }
}
