package demo.aop.dao;

public interface DemoDao {

    void query(String sql, Integer limitCount);

    void query(String sql);

    String getSql(String sql);
}
