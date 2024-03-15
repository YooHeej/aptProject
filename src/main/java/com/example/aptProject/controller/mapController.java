package com.example.aptProject.controller;

import com.example.aptProject.entity.APIResult;
import com.example.aptProject.service.ApiService;
import com.example.aptProject.service.ApiServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class mapController {
    @Autowired private ApiService apiService;
    @GetMapping("getResult/{LAWD_CD}/{DEAL_YMD}")
    public String getResult(@PathVariable String LAWD_CD, @PathVariable String DEAL_YMD, HttpSession session, Model model) throws IOException {
        String totalCount = apiService.getTotalCount(LAWD_CD,DEAL_YMD);

        List<APIResult> resultList = apiService.getResultList(totalCount, LAWD_CD, DEAL_YMD);
        session.setAttribute("totalCount",totalCount);
        model.addAttribute("resultList", resultList);


        return "api/map";
    }
}





