package ioc.dao;

public interface BeanDao {

    void print(String str);

    String query(String sql, Integer count);
}
