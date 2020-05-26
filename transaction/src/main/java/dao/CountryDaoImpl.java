package dao;

import entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class CountryDaoImpl implements CountryDao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insert(String sql) {
        Map<String, Object> map = null;
        return jdbcTemplate.update(sql, map);
    }

    @Override
    public Country query(Integer id) {
        List<Country> list = jdbcTemplate.query("select country, last_update from country where country_id="+id,
                new BeanPropertyRowMapper<>(Country.class));
        return list.get(0);
    }
}
