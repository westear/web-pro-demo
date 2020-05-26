package entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Country {

    private Integer countryId;

    private String country;

    private Timestamp lastUpdate;

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", country='" + country + '\'' +
                ", lastUpdate=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdate.getTime()) +
                '}';
    }
}
