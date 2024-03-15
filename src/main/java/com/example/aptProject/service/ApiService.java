package com.example.aptProject.service;

import com.example.aptProject.entity.APIResult;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApiService {
    StringBuilder getAPIResult(String serviceKey, String pageNo, String numOfRows, String LAWD_CD, String DEAL_YMD) throws IOException ;
    List<APIResult> getResultList() throws IOException;
    Map<String, Double> getGeoCode(String addr) throws IOException, ParseException;

}