package com.example.aptProject.service;

import com.example.aptProject.entity.LocationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationCodeServiceImpl implements LocationCodeService{

    @Autowired private LocationCode Location;

    @Override
    public String getLocationCode(int lCode) {
        return Location.getlName();
    }

    @Override
    public int getLocationName(String lName) {
        return Location.getlCode();
    }
}
