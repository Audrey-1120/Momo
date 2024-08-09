package com.osundosun.momo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MemberServiceImpl implements MemberService {
  
  @Value("${naver.client-id}")
  private String naverClientId;
  
  @Value("${naver.client-secret}")
  private String naverClientSecret;
  

  //네이버 로그인 요청 주소 생성해서 반환
  @Override
  public String getNaverLoginURL(HttpServletRequest request) {
    
    String redirectUri = "http://localhost:8080" + "/user/naver/getAccessToken.do";
    String state = new BigInteger(130, new SecureRandom()).toString();
    
    StringBuilder builder = new StringBuilder();
    builder.append("https://nid.naver.com/oauth2.0/authorize");
    builder.append("?response_type=code");
    builder.append("&client_id=" + naverClientId);
    builder.append("&redirect_uri=" + redirectUri);
    
    return builder.toString();
  }
  
  // 네이버로부터 Access Token을 발급 받아 반환
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) {
    
    // 네이버 로그인 첫 단계에서 전달한 redirect_uri 에서 동작한다.
    // code와 state 파라미터를 받아서 AccessToken을 발급 받을 때 사용한다.
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String spec = "https://nid.naver.com/oauth2.0/token";
    String grantType = "authorization_code";
    String clientId = naverClientId;
    String clientSecret = naverClientSecret;
    
    StringBuilder builder = new StringBuilder();
    builder.append(spec);
    builder.append("?grant_type=" + grantType);
    builder.append("&client_id=" + clientId);
    builder.append("&client_secret=" + clientSecret);
    builder.append("&code=" + code);
    builder.append("&state=" + state);
    
    HttpURLConnection con = null;
    ObjectMapper obj = null;
    String accessToken = null;
    
    try {
      
      // 요청
      URL url = new URL(builder.toString());
      con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      
      // 응답 스트림
      BufferedReader reader = null;
      int responseCode = con.getResponseCode();
      if(responseCode == HttpURLConnection.HTTP_OK) {
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      } else {
        reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      }
      
      // 응답 데이터 받기
      String line = null;
      StringBuilder responseBody = new StringBuilder();
      while((line = reader.readLine())!= null) {
        responseBody.append(line);
      }
      
      // 응답 데이터 JSON 객체 변환
      obj = new ObjectMapper();
      JsonNode jsonNode = obj.readTree(responseBody.toString());
      accessToken = jsonNode.get("access_token").asText();
      
      // 응답 스트림 닫기
      reader.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    con.disconnect();
    
    return accessToken;
  }
  
  
  
  
  
  
  
  
  
  

}
