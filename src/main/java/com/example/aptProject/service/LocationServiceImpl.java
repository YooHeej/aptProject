package com.example.aptProject.service;

import com.example.aptProject.dao.LocationCodeDao;
import com.example.aptProject.entity.LocationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    private LocationCodeDao lDao;
    @Override
    public String getLocationName(int lCode) {
        String firstName, secondName, lastName = "";

        firstName = lDao.getLocationCode(lCode).getFirstName();

        secondName = lDao.getLocationCode(lCode).getSecondName();
        if(secondName == null){
            return firstName;
        }
        lastName = lDao.getLocationCode(lCode).getLastName();
        if(lastName == null){
            return firstName + " " + secondName;
        }

        return firstName + " " + secondName + " " + lastName;
    }
}
