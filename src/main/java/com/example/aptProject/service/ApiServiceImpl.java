package com.example.aptProject.service;

import com.example.aptProject.entity.APIResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiServiceImpl implements ApiService{
    @Autowired LocationService lSvc;
    @Override
    public StringBuilder getAPIResult(String serviceKey, String pageNo, String numOfRows, String LAWD_CD, String DEAL_YMD) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + pageNo); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + numOfRows); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + LAWD_CD); /*지역코드*/
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + DEAL_YMD); /*계약월*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb;
    }

    @Override
    public List<APIResult> getResultList(String numOfRows, String LAWD_CD, String DEAL_YMD) throws IOException {
        String serviceKey = "rlpbGR9EbYg8iu0YftsAGmUeblmq9qJenXIk7WsVg0qr%2FRXALrab9zfstv0OkO5A15gR4aKR5aO%2FVFtjV6dkfA%3D%3D";
        String pageNo = "1";

        StringBuilder sb = getAPIResult(serviceKey, pageNo, numOfRows, LAWD_CD, DEAL_YMD);

        APIResult apiResult = null;
        List<APIResult> resultList = new ArrayList<>();

        try {
            // XML 문자열
            String xmlString = sb.toString();

            // XML 파서 생성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());

            // XML 파싱
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            Node totalCountNode = doc.getElementsByTagName("totalCount").item(0);
            String totalCount = totalCountNode.getTextContent().trim();

            // 각 항목 추출
            NodeList itemList = doc.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String 거래금액 = itemElement.getElementsByTagName("거래금액").item(0).getTextContent().trim();
                    String 건축년도 = itemElement.getElementsByTagName("건축년도").item(0).getTextContent().trim();
                    String 년 = itemElement.getElementsByTagName("년").item(0).getTextContent().trim();
                    String 도로명 = itemElement.getElementsByTagName("도로명").item(0).getTextContent().trim();
                    String 법정동 = itemElement.getElementsByTagName("법정동").item(0).getTextContent().trim();
                    String 아파트 = itemElement.getElementsByTagName("아파트").item(0).getTextContent().trim();
                    String 월 = itemElement.getElementsByTagName("월").item(0).getTextContent().trim();
                    String 일 = itemElement.getElementsByTagName("일").item(0).getTextContent().trim();
                    String 전용면적 = itemElement.getElementsByTagName("전용면적").item(0).getTextContent().trim();
                    String 지역코드 = itemElement.getElementsByTagName("지역코드").item(0).getTextContent().trim();
                    String 층 = itemElement.getElementsByTagName("층").item(0).getTextContent().trim();

                    String addr = "";
                    addr = lSvc.getLocationName(Integer.parseInt(지역코드)) + " " + 도로명;
                    Map<String, Double> map = getGeoCode(addr);

                    apiResult = new APIResult(년, 월, 일, 지역코드, 법정동, 도로명, 아파트, 층, 전용면적, 건축년도, 거래금액, totalCount, map.get("lon"), map.get("lat"));

                    System.out.println("년: " + 년);
                    System.out.println("월: " + 월);
                    System.out.println("일: " + 일);
                    System.out.println("지역코드: " + 지역코드);
                    System.out.println("법정동: " + 법정동);
                    System.out.println("도로명: " + 도로명);
                    System.out.println("아파트: " + 아파트);
                    System.out.println("층: " + 층);
                    System.out.println("전용면적(m^2): " + 전용면적);
                    System.out.println("건축년도: " + 건축년도);
                    System.out.println("거래금액(만): " + 거래금액);
                    System.out.println("totalCount: " + totalCount);
                    System.out.println("lat: " + map.get("lat"));
                    System.out.println("lon: " + map.get("lon"));
                    System.out.println("---");
                }
                resultList.add(apiResult);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;

    }

    @Override
    public Map<String, Double> getGeoCode(String addr) throws IOException, ParseException {
        String kakaoKey = "53f0f1441d99a7c87f4d29410ab21e7e";
        String query = URLEncoder.encode(addr, "utf-8");
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json"
                + "?query=" + query;

        URL url = new URL(apiUrl);
        // Header setting
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "KakaoAK " + kakaoKey);
//		conn.setRequestProperty("content-type", "application/json");
//		conn.setDoOutput(true);

        // 응답 결과 확인
        int responseCode = conn.getResponseCode();
//		System.out.println(responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while((line = br.readLine()) != null)
            sb.append(line);
        br.close();
//		System.out.println(sb.toString());

        // JSON 데이터에서 원하는 값 추출하기
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(sb.toString());
        JSONArray documents = (JSONArray) object.get("documents");
        JSONObject item = (JSONObject) documents.get(0);
//    	System.out.println(item.keySet());	// [address, address_type, x, y, address_name, road_address]
        String lon_ = (String) item.get("x");
        String lat_ = (String) item.get("y");
//    	System.out.println(lon_ + ", " + lat_);

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lon", Double.parseDouble(lon_));
        map.put("lat", Double.parseDouble(lat_));
        return map;
    }

    @Override
    public String getTotalCount(String LAWD_CD, String DEAL_YMD) throws IOException {
        List<APIResult> list = getResultList("1", LAWD_CD, DEAL_YMD);
        System.out.println("요기서는 이렇게나옴 " + list.get(0).getTotalCount());
        return list.get(0).getTotalCount();
    }

    //법정동 코드.txt파일 데이터 전처리 하고 insert 쿼리문으로 변경해주는 코드
    public static List<List<String>> filterAndParseDataFromFile(String filename) {
        List<List<String>> result = new ArrayList<>();
        Map<String, List<String>> codeMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "EUC-KR"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t"); // 탭으로 분리

                // "존재"를 포함하는 데이터만 처리
                if (parts.length >= 3 && parts[2].contains("존재")) {
                    String code = parts[0].substring(0, 5); // 지역 코드는 5자리로 자르기
                    // 중복된 지역 코드는 건너뜀
                    if (codeMap.containsKey(code)) {
                        continue;
                    }
                    List<String> entry = new ArrayList<>();
                    entry.add(code); // 지역 코드 추가
                    // 지역 이름을 공백 기준으로 나누어 저장
                    String[] regionNameParts = parts[1].split(" ");
                    for (String regionNamePart : regionNameParts) {
                        entry.add(regionNamePart);
                    }
                    result.add(entry); // 결과에 추가
                    codeMap.put(code, entry); // 지역 코드와 데이터 매핑
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String generateSQLQuery(List<List<String>> parsedData) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("insert into locationCode values\n");
        for (int i = 0; i < parsedData.size(); i++) {
            List<String> entry = parsedData.get(i);
            StringBuilder valuesBuilder = new StringBuilder();
            valuesBuilder.append("(");
            for (int j = 0; j < entry.size(); j++) {
                // 문자열인 경우 따옴표 추가
                if (!isNumeric(entry.get(j))) {
                    valuesBuilder.append("'");
                }
                valuesBuilder.append(entry.get(j));
                if (!isNumeric(entry.get(j))) {
                    valuesBuilder.append("'");
                }
                if (j < entry.size() - 1) {
                    valuesBuilder.append(", ");
                }
            }
            // 데이터 요소의 수를 테이블의 컬럼 수와 맞추기 위해 null 값 추가
            int numOfColumns = 4;
            if (entry.size() < numOfColumns) {
                for (int k = entry.size(); k < numOfColumns; k++) {
                    valuesBuilder.append(", null");
                }
            }
            valuesBuilder.append(")");
            queryBuilder.append(valuesBuilder);
            if (i < parsedData.size() - 1) {
                queryBuilder.append(",\n");
            }
        }
        queryBuilder.append(";");
        return queryBuilder.toString();
    }

    // 문자열이 숫자인지 확인하는 유틸리티 메서드
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
