package util;

import entity.City;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DBUtil {

    private Connection connection;

    public DBUtil() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
            properties.setProperty("useUnicode","true");
            properties.setProperty("characterEncoding","utf8");
            properties.setProperty("allowMultiQueries","true");
            properties.setProperty("useSSL","false");
            properties.setProperty("serverTimezone","UTC");
            properties.setProperty("user","root");
            properties.setProperty("password","qyc514");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", properties);
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String sql) {
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.execute();
            TimeUnit.SECONDS.sleep(5);
            connection.commit();

            pst.close();
            connection.close();
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insert(String sql) {
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.execute();
//            connection.commit();

            pst.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public City query(String sql) {
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            City city = new City();
            while (rs.next()) {
                city.setCity(rs.getString(1));
                city.setCountryId(rs.getInt(2));
                city.setLastUpdate(rs.getTimestamp(3));
            }
            rs.close();
            pst.close();
            connection.close();
            return city;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
