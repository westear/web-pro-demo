package com.spring.demo.aop.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DemoDaoImpl implements DemoDao {

    @Override
    public void query(String sql){
        System.out.println("===" + sql + "===");
    }

    @Override
    public void query(String sql, Integer limitCount){
        System.out.println(this.getClass().getName());
        this.buildSql();
        System.out.println("===" + sql + "===" + "; limitCount: " + limitCount);
    }

    @Override
    public String getSql(String sql) {
        System.out.println(this.getClass().getName()+" get sql: " + sql);
        return sql;
    }

    private void buildSql(){
        System.out.println("select * from tableName where 1=1 ");
    }
}
