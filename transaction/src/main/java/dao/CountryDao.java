package dao;

import entity.Country;

public interface CountryDao {

    int insert(String sql);

    Country query(Integer id);
}
