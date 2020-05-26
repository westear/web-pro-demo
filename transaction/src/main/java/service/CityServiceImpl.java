package service;

import dao.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityServiceImpl implements CityService {

    private CityDao cityDao;

    private CountryService countryService;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Autowired
    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean update(String countrySql, String citySql) {
        countryService.insert(countrySql);
        //抛出异常
//        int a = 1/0;
        return cityDao.update(citySql);
    }
}
