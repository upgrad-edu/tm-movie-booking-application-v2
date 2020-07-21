package com.upgrad.mtb.services;

import com.upgrad.mtb.entity.City;
import com.upgrad.mtb.entity.Language;
import com.upgrad.mtb.entity.Status;
import com.upgrad.mtb.entity.UserType;
import com.upgrad.mtb.daos.CityDAO;
import com.upgrad.mtb.daos.LanguageDAO;
import com.upgrad.mtb.daos.StatusDAO;
import com.upgrad.mtb.daos.UserTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("InitService")
public class InitServiceImpl implements InitService{
    @Autowired
    @Qualifier("cityDAO")
    CityDAO cityDAO;

    @Autowired
    @Qualifier("userTypeDAO")
    UserTypeDAO userTypeDAO;

    @Autowired
    @Qualifier("languageDAO")
    LanguageDAO languageDAO;

    @Autowired
    @Qualifier("statusDAO")
    StatusDAO statusDAO;

    List<City> cities = Arrays.asList(new City("Patna"), new City("Mumbai"), new City("Kolkata"), new City("Bangalore"));
    List<UserType> userTypes = Arrays.asList(new UserType("Customer"), new UserType("Admin"));
    List<Language> languages = Arrays.asList(new Language("English"), new Language("Hindi"), new Language("Bengali"));
    List<Status> statuses = Arrays.asList(new Status("Upcoming"), new Status("Released"), new Status("Blocked"));


    @Override
    public void init() {
        cities.forEach(city -> cityDAO.save(city));
        userTypes.forEach(userType -> userTypeDAO.save(userType));
        languages.forEach(language -> languageDAO.save(language));
        statuses.forEach(status -> statusDAO.save(status));
    }
}
